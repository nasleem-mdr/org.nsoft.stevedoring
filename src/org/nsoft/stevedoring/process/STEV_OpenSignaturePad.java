package org.nsoft.stevedoring.process;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Window;
import org.compiere.process.SvrProcess;
import org.nsoft.stevedoring.form.STEV_SignaturePadPanel;
import org.nsoft.stevedoring.model.MStevStatementOfFact;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/**
 * Tombol "Tanda Tangan Digital" pada Tab STEV_StatementOfFact — membuka
 * dialog tanda tangan sebagai floating Window modal SUNGGUHAN di atas
 * window SoF yang memicunya (bukan tab baru di desktop, seperti yang
 * terjadi kalau memakai mekanisme AD_Process "Special Form").
 *
 * CATATAN ARSITEKTUR:
 * - Window & panel dibuat di dalam Executions.schedule(desktop, ...),
 *   supaya konstruksinya berjalan di UI/event thread ZK yang sudah
 *   attached ke Desktop — inilah yang membuat panggilan Window.doModal()
 *   aman (menghindari NullPointerException getAppDesktop() dari
 *   percobaan pertama, dan SuspendNotAllowedException kalau dipanggil
 *   sebelum attach).
 * - STEV_SignaturePadPanel yang di-append adalah PLAIN ZK component
 *   (bukan ADForm) — UI-nya selesai dibangun langsung di constructor,
 *   jadi tidak butuh lifecycle ADForm.init()/initForm() yang hanya
 *   dipanggil oleh ADForm.openForm(). Kalau kamu pakai STEV_SignaturePadForm
 *   (ADForm) di sini, hasilnya blank karena initForm() tidak pernah terpanggil.
 * - Yang dioper ke panel adalah ID record (int), BUKAN objek PO yang
 *   sudah di-load dengan get_TrxName(): transaksi proses ini akan
 *   commit/ditutup begitu doIt() selesai, jauh sebelum user benar-benar
 *   berinteraksi dengan dialog (apalagi klik Simpan). Panel me-load
 *   ulang record itu sendiri dengan trxName=null saat dibangun, supaya
 *   aman dipakai belakangan.
 */
public class STEV_OpenSignaturePad extends SvrProcess
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

        // Pengecekan validitas dilakukan di transaksi proses ini (read-only) —
        // aman, karena tidak disimpan/dipakai lagi setelah doIt() selesai.
        MStevStatementOfFact sof = new MStevStatementOfFact(getCtx(), p_STEV_StatementOfFact_ID, get_TrxName());
        if (sof.get_ID() <= 0)
            throw new IllegalStateException("Statement of Fact ID " + p_STEV_StatementOfFact_ID + " tidak valid");

        if (MStevStatementOfFact.DOCSTATUS_Completed.equals(sof.getDocStatus()))
            throw new IllegalStateException("SoF " + sof.getDocumentNo()
                    + " sudah Completed — tanda tangan tidak bisa diubah lagi");

        final String docNo = sof.getDocumentNo();
        final int soFRecordId = p_STEV_StatementOfFact_ID;

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
                    win.setTitle("Tanda Tangan Digital - " + docNo);
                    win.setClosable(true);
                    win.setSizable(true);
                    win.setBorder("normal");
                    win.setWidth("580px");
                    win.setHeight("480px");

                    // Plain component — UI langsung terbentuk di constructor,
                    // tidak perlu lifecycle ADForm.
                    STEV_SignaturePadPanel panel = new STEV_SignaturePadPanel(soFRecordId);
                    win.appendChild(panel);
                    win.setPage(desktop.getFirstPage());

                    // Baru aman dipanggil SETELAH attach (appendChild + setPage) —
                    // supaya kalau SoF ini sudah pernah ditandatangani, gambar
                    // lamanya langsung dimuat ke kanvas (kasus edit/update).
                    panel.loadExistingSignatureIntoCanvas();

                    win.doModal();
                }
                catch (Exception e)
                {
                    log.severe("Gagal menampilkan dialog signature: " + e.getMessage());
                }
            }
        }, null);

        return "@Success@";
    }
}