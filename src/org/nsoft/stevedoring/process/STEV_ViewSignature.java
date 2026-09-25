package org.nsoft.stevedoring.process;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Window;
import org.compiere.process.SvrProcess;
import org.nsoft.stevedoring.model.MStevStatementOfFact;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Html;
import org.zkoss.zul.Vlayout;

/**
 * Tombol "Lihat Tanda Tangan" pada Tab STEV_StatementOfFact — menampilkan
 * isi kolom DigitalSignature (base64 PNG data URI, mis.
 * "data:image/png;base64,....") sebagai GAMBAR, bukan teks mentah.
 *
 * Tidak perlu konversi apa pun: data URI base64 memang format siap-pakai
 * untuk atribut src sebuah <img> — browser merendernya langsung sebagai
 * gambar.
 *
 * Memakai pola yang sama dengan STEV_OpenSignaturePad (Executions.schedule
 * + plain org.adempiere.webui.component.Window + doModal()), yang sudah
 * teruji aman dari NullPointerException/SuspendNotAllowedException akibat
 * background thread.
 */
public class STEV_ViewSignature extends SvrProcess
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

        final String docNo = sof.getDocumentNo();
        final String signatureDataUri = sof.getDigitalSignature();

        if (signatureDataUri == null || signatureDataUri.trim().isEmpty())
            throw new IllegalStateException("SoF " + docNo + " belum memiliki tanda tangan");

        final Desktop desktop = AEnv.getDesktop();
        if (desktop == null || !desktop.isAlive())
            throw new IllegalStateException("Sesi ZK Desktop tidak aktif.");

        Executions.schedule(desktop, new EventListener<Event>()
        {
            @Override
            public void onEvent(Event event) throws Exception
            {
                try
                {
                    Window win = new Window();
                    win.setTitle("Tanda Tangan - " + docNo);
                    win.setClosable(true);
                    win.setSizable(true);
                    win.setBorder("normal");
                    win.setWidth("560px");
                    win.setHeight("320px");

                    Vlayout root = new Vlayout();
                    root.setWidth("100%");
                    root.setSpacing("8px");
                    root.setStyle("padding:8px;");

                    root.appendChild(new Label("SoF " + docNo));

                    // Data URI base64 dipakai langsung sebagai src <img> —
                    // tidak perlu decode/konversi apa pun di sisi server.
                    Html img = new Html("<img src=\"" + signatureDataUri
                            + "\" style=\"max-width:100%;border:1px solid #ccc;background:#fff;\"/>");
                    root.appendChild(img);

                    win.appendChild(root);
                    win.setPage(desktop.getFirstPage());
                    win.doModal();
                }
                catch (Exception e)
                {
                    log.severe("Gagal menampilkan gambar tanda tangan: " + e.getMessage());
                }
            }
        }, null);

        return "@Success@";
    }
}
