package org.nsoft.stevedoring.process;

import org.compiere.process.DocAction;
import org.compiere.process.SvrProcess;
import org.nsoft.stevedoring.model.MStevStatementOfFact;

/**
 * Tombol eksplisit "Complete Statement of Fact" — TIDAK menduplikasi
 * business logic apa pun. Proses ini cuma memanggil ulang mekanisme
 * DocAction standar yang sama persis dengan yang dipicu lewat dropdown
 * "Document Action" bawaan iDempiere:
 *
 *   processIt(ACTION_Complete) -> DocumentEngine -> completeIt()
 *   -> SoFFinanceService.processCompletion(...)
 *
 * Dibuat karena field "Document Action" bawaan agak tersembunyi
 * (dropdown kecil di toolbar) — tombol Process biasa lebih mudah
 * ditemukan user lapangan/operasional yang jarang pakai iDempiere.
 *
 * Registrasi di Application Dictionary:
 *   AD_Process.Name      = "Complete Statement of Fact"
 *   AD_Process.ClassName = org.nsoft.stevedoring.process.STEV_CompleteStatementOfFact
 *   Dipasang sebagai Column bertipe Button (Virtual Column diisi dummy,
 *   lihat catatan di README) di Tab STEV_StatementOfFact.
 */
public class STEV_CompleteStatementOfFact extends SvrProcess
{
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
            throw new IllegalStateException("Statement of Fact " + sof.getDocumentNo() + " sudah Completed sebelumnya");

        // Jalur yang PERSIS SAMA dengan klik dropdown Document Action -> Complete.
        boolean ok = sof.processIt(DocAction.ACTION_Complete);
        sof.saveEx();

        if (!ok)
            throw new IllegalStateException(sof.getProcessMsg() != null
                    ? sof.getProcessMsg() : "Gagal Complete Statement of Fact " + sof.getDocumentNo());

        return sof.getProcessMsg() != null ? sof.getProcessMsg()
                : "Statement of Fact " + sof.getDocumentNo() + " berhasil di-Complete";
    }
}
