package org.nsoft.stevedoring.process;

import org.adempiere.webui.panel.ADForm;
import org.compiere.model.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.nsoft.stevedoring.form.STEV_SignaturePadForm;
import org.nsoft.stevedoring.model.MStevStatementOfFact;

/**
 * Tombol "Tanda Tangan Digital" pada Tab STEV_StatementOfFact — membuka
 * STEV_SignaturePadForm dengan Document No SoF yang sedang dibuka
 * SUDAH otomatis disiapkan (lewat Env context session), supaya user
 * tidak perlu ketik ulang manual.
 *
 * CATATAN VERIFIKASI (beda dari process lain di plugin ini yang sudah
 * teruji): memanggil ADForm.openForm() dari DALAM doIt() sebuah Process
 * (bukan dari klik menu langsung) belum pernah ditest — kalau tidak
 * langsung render/muncul error, kemungkinan perlu penyesuaian
 * threading (mis. dibungkus Executions.schedule()). Kabari errornya
 * kalau ada, supaya bisa disesuaikan.
 */
public class STEV_OpenSignaturePad extends SvrProcess
{
    /** Key context session — dibaca STEV_SignaturePadForm.init() untuk auto pre-fill */
    public static final String CTX_KEY_TARGET_DOCNO = "STEV_SIGNATURE_TARGET_DOCNO";

    private int p_STEV_StatementOfFact_ID = 0;

    @Override
    protected void prepare()
    {
        p_STEV_StatementOfFact_ID = getRecord_ID();
    }

    @Override
    protected String doIt() throws Exception
    {
        if (p_STEV_StatementOfFact_ID <= 0)
            throw new IllegalStateException("Record Statement of Fact tidak ditemukan");

        MStevStatementOfFact sof = new MStevStatementOfFact(getCtx(), p_STEV_StatementOfFact_ID, get_TrxName());
        if (sof.get_ID() <= 0)
            throw new IllegalStateException("Statement of Fact ID " + p_STEV_StatementOfFact_ID + " tidak valid");

        if (MStevStatementOfFact.DOCSTATUS_Completed.equals(sof.getDocStatus()))
            throw new IllegalStateException("SoF " + sof.getDocumentNo()
                    + " sudah Completed — tanda tangan tidak bisa diubah lagi");

        // Simpan Document No ke context session (BUKAN parameter method,
        // supaya Form yang dibuka setelah ini bisa membacanya lewat
        // Env.getContext() tanpa perlu koneksi langsung antar objek).
        Env.setContext(getCtx(), CTX_KEY_TARGET_DOCNO, sof.getDocumentNo());

        int adFormId = findFormId(STEV_SignaturePadForm.class.getName());
        if (adFormId <= 0)
            throw new IllegalStateException("AD_Form untuk " + STEV_SignaturePadForm.class.getName()
                    + " belum terdaftar — buat dulu record AD_Form di Application Dictionary "
                    + "(Classname harus persis sama)");

        ADForm.openForm(adFormId);

        return "Form Tanda Tangan Digital dibuka untuk SoF " + sof.getDocumentNo();
    }

    private int findFormId(String className)
    {
        org.compiere.model.PO po = new Query(getCtx(), "AD_Form", "Classname=?", get_TrxName())
                .setParameters(className)
                .first();
        return po != null ? po.get_ID() : 0;
    }
}
