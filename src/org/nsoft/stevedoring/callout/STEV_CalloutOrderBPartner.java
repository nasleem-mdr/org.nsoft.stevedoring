package org.nsoft.stevedoring.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MOrder;
import org.compiere.util.Env;

/**
 * Callout pada field C_Order_ID di Tab STEV_VesselSchedule: begitu user
 * memilih SPK (C_Order), otomatis isi C_BPartner_ID (Agen Shipping /
 * Customer) dari C_Order yang dipilih — supaya user tidak perlu isi
 * ulang manual data yang sebenarnya sudah ada di SPK-nya.
 *
 * Registrasi: TIDAK lewat field "Callout" di Application Dictionary,
 * tapi lewat IMappedColumnCalloutFactory (mekanisme resmi OSGi iDempiere
 * sejak NF9) — didaftarkan di {@link org.nsoft.stevedoring.base.Activator}
 * saat bundle start. Tidak ada konfigurasi manual apa pun di GUI yang
 * dibutuhkan untuk callout ini.
 */
public class STEV_CalloutOrderBPartner implements IColumnCallout
{
    private static final String COLUMN_C_BPARTNER_ID = "C_BPartner_ID";

    @Override
    public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue)
    {
        // C_Order_ID dikosongkan (mis. user hapus pilihan SPK) -> ikut kosongkan C_BPartner_ID
        if (value == null)
        {
            mTab.setValue(COLUMN_C_BPARTNER_ID, null);
            return "";
        }

        int orderId;
        try
        {
            orderId = Integer.parseInt(value.toString());
        }
        catch (NumberFormatException e)
        {
            return ""; // nilai tidak dikenali, jangan blokir input user, cukup diamkan
        }

        if (orderId <= 0)
        {
            mTab.setValue(COLUMN_C_BPARTNER_ID, null);
            return "";
        }

        MOrder order = new MOrder(ctx, orderId, null);
        if (order.get_ID() != orderId)
            return "Error: SPK (C_Order) dengan ID " + orderId + " tidak ditemukan";

        int bpartnerId = order.getC_BPartner_ID();
        mTab.setValue(COLUMN_C_BPARTNER_ID, bpartnerId > 0 ? bpartnerId : null);

        return "";
    }
}
