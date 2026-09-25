package org.nsoft.stevedoring.form;

import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Label;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.nsoft.stevedoring.model.MStevStatementOfFact;

import java.sql.Timestamp;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Html;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

/**
 * Panel UI tanda tangan digital untuk STEV_StatementOfFact — berisi
 * seluruh UI & logic (cari SoF, kanvas tanda tangan, simpan).
 *
 * Sengaja DIPISAH dari ADForm (lihat STEV_SignaturePadForm) supaya bisa
 * dipakai ulang di dua konteks:
 *  1. Dibuka lewat menu Application Dictionary / AD_Process dengan
 *     Special Form — dibungkus STEV_SignaturePadForm (ADForm), tampil
 *     sebagai tab baru di desktop.
 *  2. Dibuka lewat tombol proses (STEV_OpenSignaturePad) sebagai
 *     floating org.adempiere.webui.component.Window modal sungguhan di
 *     atas window pemicu. Di jalur ini panel di-instansiasi manual
 *     (bukan lewat ADForm.openForm()), sehingga TIDAK BOLEH bergantung
 *     pada lifecycle ADForm.init()/initForm() — makanya class ini
 *     adalah plain component: seluruh UI dibangun langsung di
 *     constructor, jadi selalu tampil terlepas dari bagaimana ia
 *     di-instansiasi.
 */
public class STEV_SignaturePadPanel extends Vlayout implements EventListener<Event>
{
    private static final long serialVersionUID = 1L;
    private static final CLogger log = CLogger.getCLogger(STEV_SignaturePadPanel.class);

    private final Textbox txtDocumentNo = new Textbox();
    private final Button btnLoad = new Button("Muat SoF");
    private final Button btnClear = new Button("Hapus");
    private final Button btnSave = new Button("Simpan Tanda Tangan");
    private final Label lblStatus = new Label();
    private final Textbox txtSignatureBridge = new Textbox();
    private MStevStatementOfFact currentSoF;
    private String pendingLoadImage;

    /** Panel kosong — user cari manual lewat Document No + tombol "Muat SoF". */
    public STEV_SignaturePadPanel()
    {
        this(0);
    }

    /**
     * Panel dengan SoF langsung di-prefill dari ID record.
     *
     * SENGAJA menerima ID (int), bukan objek MStevStatementOfFact yang
     * sudah di-load: kalau caller meng-load PO itu pakai transaksi
     * miliknya sendiri (mis. trxName dari SvrProcess), transaksi itu
     * kemungkinan besar sudah commit/ditutup pada saat user benar-benar
     * berinteraksi dengan dialog ini (yang terjadi jauh setelah doIt()
     * selesai). Di sini PO selalu di-load ulang segar dengan
     * trxName=null, supaya aman dipakai belakangan saat user klik
     * "Simpan Tanda Tangan".
     */
    public STEV_SignaturePadPanel(int prefillStevStatementOfFactId)
    {
        super();
        buildUI();
        if (prefillStevStatementOfFactId > 0)
            loadById(prefillStevStatementOfFactId);
    }

    /**
     * Panggil ini SETELAH panel benar-benar attached ke Page (di
     * STEV_SignaturePadForm: setelah appendChild(panel) di initForm();
     * di STEV_OpenSignaturePad: setelah win.appendChild(panel) +
     * win.setPage(...)). Kalau SoF yang dimuat sudah punya tanda
     * tangan sebelumnya, gambar lamanya akan dimuat ke kanvas —
     * supaya kasus EDIT/update tanda tangan mulai dari gambar lama,
     * bukan kanvas kosong.
     */
    public void loadExistingSignatureIntoCanvas()
    {
        if (pendingLoadImage == null)
            return;
        Events.echoEvent("onLoadSignatureImage", this, pendingLoadImage);
    }

    private void buildUI()
    {
        setWidth("100%");
        setSpacing("8px");

        Hlayout searchBar = new Hlayout();
        searchBar.setSpacing("6px");
        txtDocumentNo.setWidth("220px");
        txtDocumentNo.setPlaceholder("Document No Statement of Fact");
        btnLoad.addEventListener(Events.ON_CLICK, this);
        searchBar.appendChild(new Label("Document No SoF:"));
        searchBar.appendChild(txtDocumentNo);
        searchBar.appendChild(btnLoad);
        appendChild(searchBar);

        lblStatus.setStyle("color:#888;");
        appendChild(lblStatus);

        appendChild(new Html(buildCanvasHtml()));

        txtSignatureBridge.setStyle("display:none;");
        txtSignatureBridge.addEventListener(Events.ON_CHANGE, this);
        appendChild(txtSignatureBridge);
        this.addEventListener("onLoadSignatureImage", this);

        Hlayout actionBar = new Hlayout();
        actionBar.setSpacing("6px");
        btnClear.addEventListener(Events.ON_CLICK, this);
        btnSave.addEventListener(Events.ON_CLICK, this);
        btnSave.setDisabled(true);
        actionBar.appendChild(btnClear);
        actionBar.appendChild(btnSave);
        appendChild(actionBar);

        Label lblHint = new Label(
                "Catatan: setelah tersimpan, kembali ke Tab Statement of Fact dan klik Refresh "
                        + "untuk melihat Tanda Tangan & Tanggal ter-update.");
        lblHint.setStyle("color:#888; font-size:11px; font-style:italic;");
        appendChild(lblHint);
    }

    private String buildCanvasHtml()
    {
        return "<canvas id=\"stevSigPad\" width=\"500\" height=\"180\" "
                + "style=\"border:1px solid #999;touch-action:none;background:#fff;\"></canvas>"
                + "<script>\n"
                + "(function() {\n"
                + "  var canvas = document.getElementById('stevSigPad');\n"
                + "  if (!canvas) return;\n"
                + "  var ctx = canvas.getContext('2d');\n"
                + "  var drawing = false;\n"
                + "  function pos(e) {\n"
                + "    var rect = canvas.getBoundingClientRect();\n"
                + "    var p = e.touches ? e.touches[0] : e;\n"
                + "    return { x: p.clientX - rect.left, y: p.clientY - rect.top };\n"
                + "  }\n"
                + "  function start(e) { drawing = true; var p = pos(e); ctx.beginPath(); ctx.moveTo(p.x, p.y); }\n"
                + "  function move(e) {\n"
                + "    if (!drawing) return;\n"
                + "    var p = pos(e);\n"
                + "    ctx.lineTo(p.x, p.y); ctx.stroke();\n"
                + "    e.preventDefault();\n"
                + "  }\n"
                + "  function end() { drawing = false; }\n"
                + "  canvas.addEventListener('mousedown', start);\n"
                + "  canvas.addEventListener('mousemove', move);\n"
                + "  window.addEventListener('mouseup', end);\n"
                + "  canvas.addEventListener('touchstart', start);\n"
                + "  canvas.addEventListener('touchmove', move);\n"
                + "  canvas.addEventListener('touchend', end);\n"
                + "  window.stevClearSignature = function() { ctx.clearRect(0, 0, canvas.width, canvas.height); };\n"
                + "  window.stevLoadSignature = function(dataUri) {\n"
                + "    var img = new Image();\n"
                + "    img.onload = function() {\n"
                + "      ctx.clearRect(0, 0, canvas.width, canvas.height);\n"
                + "      ctx.drawImage(img, 0, 0, canvas.width, canvas.height);\n"
                + "    };\n"
                + "    img.src = dataUri;\n"
                + "  };\n"
                + "  window.stevSendSignature = function(bridgeUuid) {\n"
                + "    var data = canvas.toDataURL('image/png');\n"
                + "    console.log('STEV signature: captured data, length=' + data.length);\n"
                + "    var w = zk.Widget.$(bridgeUuid);\n"
                + "    if (w) { w.setValue(data); w.fire('onChange', {value: data}); console.log('STEV signature: sent to bridge widget ' + bridgeUuid); }\n"
                + "    else { console.error('STEV signature bridge widget not found: ' + bridgeUuid); }\n"
                + "  };\n"
                + "})();\n"
                + "</script>";
    }

    @Override
    public void onEvent(Event event) throws Exception
    {
        if ("onLoadSignatureImage".equals(event.getName()))
        {
            Object data = event.getData();
            if (data != null)
            {
                Clients.evalJavaScript("if (window.stevLoadSignature) window.stevLoadSignature('"
                        + data.toString() + "');");
            }
            return;
        }
        else if (event.getTarget() == btnLoad)
        {
            loadSoF();
        }
        else if (event.getTarget() == btnClear)
        {
            Clients.evalJavaScript("if (window.stevClearSignature) window.stevClearSignature();");
        }
        else if (event.getTarget() == btnSave)
        {
            Clients.evalJavaScript("if (window.stevSendSignature) window.stevSendSignature('"
                    + txtSignatureBridge.getUuid() + "');");
        }
        else if (event.getTarget() == txtSignatureBridge)
        {
            Object val = txtSignatureBridge.getRawValue();
            String data = val != null ? val.toString() : "";
            log.info("STEV signature bridge onChange diterima server, panjang data=" + data.length());
            saveSignature(data);
        }
    }

    private void loadById(int id)
    {
        try
        {
            MStevStatementOfFact sof = new MStevStatementOfFact(Env.getCtx(), id, null);
            if (sof.get_ID() > 0)
                applySoF(sof);
        }
        catch (Exception e)
        {
            lblStatus.setValue("Gagal memuat SoF: " + e.getMessage());
        }
    }

    private void loadSoF()
    {
        Object val = txtDocumentNo.getRawValue();
        String docNo = val != null ? val.toString() : "";

        if (docNo.trim().isEmpty())
        {
            lblStatus.setValue("Isi Document No SoF terlebih dahulu");
            return;
        }

        MStevStatementOfFact sof = new Query(Env.getCtx(), MStevStatementOfFact.Table_Name,
                "DocumentNo=?", null)
                .setParameters(docNo.trim())
                .first();

        if (sof == null)
        {
            currentSoF = null;
            lblStatus.setValue("SoF dengan Document No '" + docNo + "' tidak ditemukan");
            btnSave.setDisabled(true);
            return;
        }

        applySoF(sof);
    }

    private void applySoF(MStevStatementOfFact sof)
    {
        currentSoF = sof;
        txtDocumentNo.setRawValue(sof.getDocumentNo());
        pendingLoadImage = (sof.getDigitalSignature() != null && !sof.getDigitalSignature().trim().isEmpty())
                ? sof.getDigitalSignature() : null;

        if (MStevStatementOfFact.DOCSTATUS_Completed.equals(sof.getDocStatus()))
        {
            lblStatus.setValue("SoF " + sof.getDocumentNo()
                    + " sudah Completed — tanda tangan tidak bisa diubah lagi");
            btnSave.setDisabled(true);
            return;
        }

        lblStatus.setValue("SoF " + sof.getDocumentNo()
                + (pendingLoadImage != null ? " sudah pernah ditandatangani — gambar ulang untuk update" : " siap ditandatangani")
                + " (DocStatus: " + sof.getDocStatus() + ")");
        btnSave.setDisabled(false);
    }

    private void saveSignature(String base64Png)
    {
        if (currentSoF == null)
        {
            lblStatus.setValue("Muat SoF terlebih dahulu sebelum menyimpan tanda tangan");
            return;
        }
        if (base64Png == null || base64Png.trim().isEmpty())
        {
            lblStatus.setValue("Kanvas masih kosong — gambar tanda tangan dulu");
            return;
        }

        try
        {
            currentSoF.setDigitalSignature(base64Png);
            currentSoF.setSignedDate(new Timestamp(System.currentTimeMillis()));
            currentSoF.saveEx();

            lblStatus.setValue("Tanda tangan tersimpan untuk SoF " + currentSoF.getDocumentNo()
                    + " — kembali ke Tab Statement of Fact dan refresh untuk melihat hasilnya");
        }
        catch (Exception e)
        {
            log.log(java.util.logging.Level.SEVERE, "Gagal menyimpan tanda tangan SoF " + currentSoF.getDocumentNo(), e);
            lblStatus.setStyle("color:#c00;");
            lblStatus.setValue("Gagal menyimpan: " + e.getMessage());
        }
    }
}