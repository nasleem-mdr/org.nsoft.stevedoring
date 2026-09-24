package org.nsoft.stevedoring.process;

import org.adempiere.webui.panel.ADForm;
import org.compiere.model.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.nsoft.stevedoring.form.STEV_SignaturePadForm;
import org.nsoft.stevedoring.model.MStevStatementOfFact;

/**
 * The "Digital Signature" button on the STEV_StatementOfFact tab opens
 * STEV_SignaturePadForm with the currently open SoF Document No.
 * This is automatically pre-populated (via the Env context session)
 * so the user does not need to type it in manually.
 *
 * VERIFICATION NOTE (differs from other proven processes in this plugin):
 * Calling ADForm.openForm() from *within* a Process's doIt() method
 * (rather than directly from a menu click) has not yet been tested.
 * If it fails to render or throws an error, threading adjustments
 * (e.g., wrapping it in Executions.schedule()) may be required.
 * Please report any errors so adjustments can be made.
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
