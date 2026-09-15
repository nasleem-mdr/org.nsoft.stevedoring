package org.nsoft.stevedoring.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.model.MClient;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.nsoft.stevedoring.service.SoFFinanceService;

/**
 * Berita Acara (Statement of Fact) — akumulasi akhir dari seluruh
 * {@link MStevTallyLine} di bawah satu {@link MStevVesselSchedule}.
 *
 * Diimplementasikan sebagai dokumen penuh (implements {@link DocAction})
 * mengikuti pola standar iDempiere (seperti MOrder/MInOut), sehingga:
 *  - Tombol "Complete", "Void", "Close" muncul otomatis di toolbar.
 *  - Saat di-Complete, {@link #completeIt()} memanggil
 *    {@link SoFFinanceService#processCompletion(MStevStatementOfFact)}
 *    untuk menjalankan Auto-Adjustment & Delivery (M_InOut, C_Invoice,
 *    update QtyOrdered pada C_OrderLine).
 *
 * AD_Table.ClassName untuk tabel STEV_StatementOfFact HARUS diarahkan ke
 * kelas ini: org.nsoft.stevedoring.model.MStevStatementOfFact
 */
public class MStevStatementOfFact extends X_STEV_StatementOfFact implements DocAction
{
    private static final long serialVersionUID = 1L;

    private static final CLogger slog = CLogger.getCLogger(MStevStatementOfFact.class);

    /** Pesan proses, ditampilkan setelah Complete/Void dijalankan */
    private String m_processMsg = null;

    public static final String DOCSTATUS_Draft      = "DR";
    public static final String DOCSTATUS_InProgress = "IP";
    public static final String DOCSTATUS_Completed  = "CO";
    public static final String DOCSTATUS_Voided     = "VO";
    public static final String DOCSTATUS_Closed     = "CL";

    public MStevStatementOfFact(Properties ctx, int STEV_StatementOfFact_ID, String trxName)
    {
        super(ctx, STEV_StatementOfFact_ID, trxName);
    }

    public MStevStatementOfFact(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

    public MStevVesselSchedule getVesselSchedule()
    {
        return new MStevVesselSchedule(getCtx(), getSTEV_VesselSchedule_ID(), get_TrxName());
    }

    /**
     * Hitung ulang TotalQtyRealized dari seluruh STEV_TallyLine yang
     * tally-sheet-nya mengarah ke Vessel Schedule yang sama dengan SoF ini.
     * Dipanggil di beforeSave supaya nilai selalu konsisten sebelum SoF
     * di-complete, tapi user tetap bisa override manual sebelum disimpan
     * final jika memang berbeda dari hasil hitung otomatis.
     */
    public BigDecimal recalculateTotalQtyRealized()
    {
        StringBuilder sql = new StringBuilder()
                .append("SELECT COALESCE(SUM(tl.QtyMoved),0) ")
                .append("FROM STEV_TallyLine tl ")
                .append("JOIN STEV_TallySheet ts ON ts.STEV_TallySheet_ID = tl.STEV_TallySheet_ID ")
                .append("WHERE ts.STEV_VesselSchedule_ID = ? AND tl.IsActive='Y' AND ts.IsActive='Y'");

        BigDecimal total = org.compiere.util.DB.getSQLValueBD(get_TrxName(), sql.toString(),
                getSTEV_VesselSchedule_ID());
        return total == null ? BigDecimal.ZERO : total;
    }

    /** Semua tally line yang menyusun akumulasi SoF ini (untuk cetak lampiran) */
    public List<MStevTallyLine> getSourceTallyLines()
    {
        String whereClause = "STEV_TallySheet_ID IN (SELECT STEV_TallySheet_ID FROM STEV_TallySheet "
                + "WHERE STEV_VesselSchedule_ID=?)";
        return new Query(getCtx(), MStevTallyLine.Table_Name, whereClause, get_TrxName())
                .setParameters(getSTEV_VesselSchedule_ID())
                .setOrderBy(MStevTallyLine.COLUMNNAME_Line)
                .list();
    }

    @Override
    protected boolean beforeSave(boolean newRecord)
    {
        // Auto-hitung total realisasi tiap kali disimpan selagi masih Draft,
        // supaya nilai yang dilihat user sebelum Complete sudah akurat.
        if (DOCSTATUS_Draft.equals(getDocStatus()) || DOCSTATUS_InProgress.equals(getDocStatus()))
        {
            setTotalQtyRealized(recalculateTotalQtyRealized());
        }

        if (DOCSTATUS_Completed.equals(getDocStatus()) && getDigitalSignature() == null)
        {
            log.saveError("Error", "Tanda tangan digital Master Kapal wajib diisi sebelum Complete");
            return false;
        }
        return true;
    }

    // =====================================================================
    // Implementasi org.compiere.process.DocAction
    // =====================================================================

    @Override
    public boolean processIt(String processAction) throws Exception
    {
        return new DocumentEngine(this, getDocStatus()).processIt(processAction, getDocAction());
    }

    @Override
    public boolean unlockIt()
    {
        setProcessing(false);
        return true;
    }

    @Override
    public boolean invalidateIt()
    {
        setDocAction(DOCACTION_Complete);
        return true;
    }

    @Override
    public String prepareIt()
    {
        // Validasi minimal sebelum Complete: harus ada minimal 1 tally line
        // dan total realisasi > 0.
        setTotalQtyRealized(recalculateTotalQtyRealized());
        if (getTotalQtyRealized() == null || getTotalQtyRealized().signum() <= 0)
        {
            m_processMsg = "Tidak ada realisasi tonase (TallyLine kosong) — tidak bisa Complete";
            return DocAction.STATUS_Invalid;
        }
        if (getDigitalSignature() == null || getDigitalSignature().isEmpty())
        {
            m_processMsg = "Tanda tangan digital Master Kapal wajib diisi";
            return DocAction.STATUS_Invalid;
        }
        setDocAction(DOCACTION_Complete);
        return DocAction.STATUS_InProgress;
    }

    @Override
    public boolean approveIt()
    {
        setIsApproved(true);
        return true;
    }

    @Override
    public boolean rejectIt()
    {
        setIsApproved(false);
        return true;
    }

    /**
     * Inti proses: Auto-Adjustment & Delivery. Delegasi penuh ke
     * SoFFinanceService supaya logic finance/inventory terpusat, mudah
     * diuji, dan tidak menggembungkan model class.
     */
    @Override
    public String completeIt()
    {
        String status = prepareIt();
        if (!DocAction.STATUS_InProgress.equals(status))
            return status;

        try
        {
            SoFFinanceService service = new SoFFinanceService();
            service.processCompletion(this);
            setProcessed(true);
            setDocAction(DOCACTION_Close);
            setDocStatus(DOCSTATUS_Completed);
            m_processMsg = "Statement of Fact selesai diproses — QtyOrdered, M_InOut, dan draft Invoice sudah dibuat";
            return DocAction.STATUS_Completed;
        }
        catch (Exception e)
        {
            slog.severe("Gagal memproses SoF completion: " + e.getMessage());
            m_processMsg = "Error saat Auto-Adjustment & Delivery: " + e.getMessage();
            return DocAction.STATUS_Invalid;
        }
    }

    @Override
    public boolean voidIt()
    {
        if (DOCSTATUS_Completed.equals(getDocStatus()))
        {
            m_processMsg = "SoF yang sudah Completed (dan sudah memicu Invoice/InOut) tidak bisa di-Void langsung — "
                    + "gunakan Reverse Correct atau void dokumen turunannya terlebih dahulu";
            return false;
        }
        setDocStatus(DOCSTATUS_Voided);
        setDocAction(DOCACTION_None);
        return true;
    }

    @Override
    public boolean closeIt()
    {
        setDocStatus(DOCSTATUS_Closed);
        setDocAction(DOCACTION_None);
        return true;
    }

    @Override
    public boolean reverseCorrectIt()
    {
        // Dokumen turunan (M_InOut/C_Invoice) direverse secara manual oleh
        // finance sesuai kebijakan perusahaan; SoF hanya ditandai voided
        // setelah itu, untuk menjaga jejak audit tetap eksplisit.
        m_processMsg = "Reverse dokumen turunan (M_InOut/C_Invoice) terlebih dahulu sebelum reverse SoF ini";
        return false;
    }

    @Override
    public boolean reverseAccrualIt()
    {
        return false;
    }

    @Override
    public boolean reActivateIt()
    {
        setDocStatus(DOCSTATUS_InProgress);
        setDocAction(DOCACTION_Complete);
        return true;
    }

    @Override
    public String getSummary()
    {
        StringBuilder sb = new StringBuilder(getDocumentNo());
        if (m_processMsg != null)
            sb.append(" - ").append(m_processMsg);
        return sb.toString();
    }

    @Override
    public String getDocumentInfo()
    {
        return "Statement of Fact " + getDocumentNo();
    }

    @Override
    public java.io.File createPDF()
    {
        return null; // Cetak Berita Acara ditangani lewat JasperReports terpisah
    }

    @Override
    public String getProcessMsg()
    {
        return m_processMsg;
    }

    @Override
    public int getC_Currency_ID()
    {
        return -1; // SoF tidak punya nilai moneter langsung
    }

    @Override
    public int getDoc_User_ID()
    {
        return getCreatedBy();
    }

    @Override
    public int getAD_Client_ID()
    {
        return super.getAD_Client_ID();
    }

    @Override
    public boolean isApproved()
    {
        return getIsApproved();
    }

    @Override
    public String toString()
    {
        return "MStevStatementOfFact[" + getSTEV_StatementOfFact_ID() + ", DocumentNo=" + getDocumentNo() + "]";
    }
}
