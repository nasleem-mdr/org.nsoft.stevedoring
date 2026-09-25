package org.nsoft.stevedoring.form;

import org.adempiere.webui.panel.ADForm;
import org.compiere.util.Env;
import org.nsoft.stevedoring.model.MStevStatementOfFact;

/**
 * Wrapper ADForm tipis di atas STEV_SignaturePadPanel — dipakai supaya
 * form ini tetap bisa dibuka lewat menu Application Dictionary atau
 * AD_Process dengan "Special Form" (tampil sebagai tab di desktop).
 *
 * Untuk floating dialog modal sungguhan di atas window pemicu, LIHAT
 * STEV_OpenSignaturePad — jalur itu memakai STEV_SignaturePadPanel
 * langsung (plain component), TIDAK lewat class ini sama sekali,
 * karena class ini (ADForm) hanya berfungsi normal kalau dibuka lewat
 * ADForm.openForm() milik framework (yang memanggil initForm() pada
 * waktu yang tepat).
 */
public class STEV_SignaturePadForm extends ADForm
{
    private static final long serialVersionUID = 1L;

    @Override
    protected void initForm()
    {
        setWidth("560px");
        setHeight("440px");
        setTitle("Tanda Tangan Digital");
        setBorder("normal");
        setClosable(true);
        setSizable(true);

        int soFRecordId = Env.getContextAsInt(Env.getCtx(), getWindowNo(),
                MStevStatementOfFact.Table_Name + "_ID");

        STEV_SignaturePadPanel panel = new STEV_SignaturePadPanel(soFRecordId);
        this.appendChild(panel);
        panel.loadExistingSignatureIntoCanvas();
    }
}