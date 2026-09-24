package org.nsoft.stevedoring.form;

import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.panel.ADForm;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.nsoft.stevedoring.model.MStevStatementOfFact;
import org.nsoft.stevedoring.process.STEV_OpenSignaturePad;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Html;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

public class STEV_SignaturePadForm extends ADForm implements EventListener<Event>
{
    private static final long serialVersionUID = 1L;

    private final Textbox txtDocumentNo = new Textbox();
    private final Button btnLoad = new Button("Muat SoF");
    private final Button btnClear = new Button("Hapus");
    private final Button btnSave = new Button("Simpan Tanda Tangan");
    private final Label lblStatus = new Label();
    private final Textbox txtSignatureBridge = new Textbox();
    private MStevStatementOfFact currentSoF;

    @Override
    protected void initForm()
    {
        setWidth("560px");
        setHeight("440px");
        setClosable(true);
        setSizable(true);

        Vlayout root = new Vlayout();
        root.setWidth("100%");
        root.setSpacing("8px");

        Hlayout searchBar = new Hlayout();
        searchBar.setSpacing("6px");
        txtDocumentNo.setWidth("220px");
        txtDocumentNo.setPlaceholder("Document No Statement of Fact");
        btnLoad.addEventListener(Events.ON_CLICK, this);
        searchBar.appendChild(new Label("Document No SoF:"));
        searchBar.appendChild(txtDocumentNo);
        searchBar.appendChild(btnLoad);
        root.appendChild(searchBar);

        lblStatus.setStyle("color:#888;");
        root.appendChild(lblStatus);

        root.appendChild(new Html(buildCanvasHtml()));

        txtSignatureBridge.setVisible(false);
        txtSignatureBridge.addEventListener(Events.ON_CHANGE, this);
        root.appendChild(txtSignatureBridge);

        Hlayout actionBar = new Hlayout();
        actionBar.setSpacing("6px");
        btnClear.addEventListener(Events.ON_CLICK, this);
        btnSave.addEventListener(Events.ON_CLICK, this);
        btnSave.setDisabled(true);
        actionBar.appendChild(btnClear);
        actionBar.appendChild(btnSave);
        root.appendChild(actionBar);

        this.appendChild(root);

        // Auto pre-fill & auto-load menggunakan setRawValue (Aman dari Exception)
        String targetDocNo = Env.getContext(Env.getCtx(), STEV_OpenSignaturePad.CTX_KEY_TARGET_DOCNO);
        if (targetDocNo != null && !targetDocNo.trim().isEmpty())
        {
            try
            {
                txtDocumentNo.setRawValue(targetDocNo); // Diganti dari setValue() ke setRawValue()
                loadSoF();
            }
            catch (Exception e)
            {
                lblStatus.setValue("Document No dari context tidak valid: " + e.getMessage());
            }
            finally
            {
                Env.getCtx().remove(STEV_OpenSignaturePad.CTX_KEY_TARGET_DOCNO);
            }
        }
    }

    private String buildCanvasHtml()
    {
        String bridgeUuid = txtSignatureBridge.getUuid();
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
                + "  window.stevSendSignature = function() {\n"
                + "    var data = canvas.toDataURL('image/png');\n"
                + "    var w = zk.Widget.$('" + bridgeUuid + "');\n"
                + "    if (w) { w.setValue(data); w.fire('onChange', {}); }\n"
                + "  };\n"
                + "})();\n"
                + "</script>";
    }

    @Override
    public void onEvent(Event event) throws Exception
    {
        if (event.getTarget() == btnLoad)
        {
            loadSoF();
        }
        else if (event.getTarget() == btnClear)
        {
            Clients.evalJavaScript("if (window.stevClearSignature) window.stevClearSignature();");
        }
        else if (event.getTarget() == btnSave)
        {
            Clients.evalJavaScript("if (window.stevSendSignature) window.stevSendSignature();");
        }
        else if (event.getTarget() == txtSignatureBridge)
        {
            Object val = txtSignatureBridge.getRawValue();
            saveSignature(val != null ? val.toString() : "");
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

        currentSoF = new Query(Env.getCtx(), MStevStatementOfFact.Table_Name,
                "DocumentNo=?", null)
                .setParameters(docNo.trim())
                .first();

        if (currentSoF == null)
        {
            lblStatus.setValue("SoF dengan Document No '" + docNo + "' tidak ditemukan");
            btnSave.setDisabled(true);
            return;
        }

        if (MStevStatementOfFact.DOCSTATUS_Completed.equals(currentSoF.getDocStatus()))
        {
            lblStatus.setValue("SoF " + docNo + " sudah Completed — tanda tangan tidak bisa diubah lagi");
            btnSave.setDisabled(true);
            return;
        }

        lblStatus.setValue("SoF " + docNo + " siap ditandatangani (DocStatus: " + currentSoF.getDocStatus() + ")");
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

        currentSoF.setDigitalSignature(base64Png);
        currentSoF.saveEx();

        lblStatus.setValue("Tanda tangan tersimpan untuk SoF " + currentSoF.getDocumentNo()
                + " — kembali ke Tab Statement of Fact dan refresh untuk melihat hasilnya");
    }
}