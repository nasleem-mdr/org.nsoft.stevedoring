package org.nsoft.stevedoring.process;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.session.SessionManager;
import org.compiere.model.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.nsoft.stevedoring.form.STEV_SignaturePadForm;
import org.nsoft.stevedoring.model.MStevStatementOfFact;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Desktop;

/**
 * Tombol "Tanda Tangan Digital" pada Tab STEV_StatementOfFact — membuka
 * STEV_SignaturePadForm dengan Document No SoF yang sedang dibuka.
 * 
 * Diperbaiki agar aman dari NullPointerException SessionManager.getAppDesktop()
 * dengan memastikan eksekusi form dibuka melalui jalur UI Desktop ZK yang aktif.
 */
public class STEV_OpenSignaturePad extends SvrProcess
{
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

        // Simpan Document No ke context session
        Env.setContext(getCtx(), CTX_KEY_TARGET_DOCNO, sof.getDocumentNo());

        final int adFormId = findFormId(STEV_SignaturePadForm.class.getName());
        if (adFormId <= 0)
            throw new IllegalStateException("AD_Form untuk " + STEV_SignaturePadForm.class.getName()
                    + " belum terdaftar — buat dulu record AD_Form di Application Dictionary");

        // Cek apakah kita berada di lingkungan WebUI dengan Desktop ZK aktif
        final Desktop desktop = Executions.getCurrent() != null ? Executions.getCurrent().getDesktop() : null;
        
        if (desktop != null && desktop.isAlive())
        {
            // Jika desktop aktif, jalankan pembukaan form langsung di thread UI ZK
            Executions.schedule(desktop, new org.zkoss.zk.ui.event.EventListener<org.zkoss.zk.ui.event.Event>() {
                @Override
                public void onEvent(org.zkoss.zk.ui.event.Event event) throws Exception {
                    try {
                        ADForm.openForm(adFormId);
                    } catch (Exception e) {
                        log.severe("Gagal membuka form signature pad: " + e.getLocalizedMessage());
                    }
                }
            }, null);
        }
        else 
        {
            // Fallback jika dipanggil murni dari background server/scheduler tanpa ZK Desktop session
            if (SessionManager.getAppDesktop() != null) {
                AEnv.executeAsync(new Runnable() {
                    @Override
                    public void run() {
                        ADForm.openForm(adFormId);
                    }
                });
            } else {
                throw new IllegalStateException("Tidak dapat membuka form: Sesi WebUI (Desktop) tidak ditemukan. Pastikan dijalankan dari client WebUI.");
            }
        }

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
