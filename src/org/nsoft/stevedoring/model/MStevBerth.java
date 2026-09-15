package org.nsoft.stevedoring.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Master data dermaga. Dipakai sebagai Table Reference oleh
 * STEV_VesselSchedule.STEV_Berth_ID (menggantikan kolom free-text
 * BerthLocation).
 */
public class MStevBerth extends X_STEV_Berth
{
    private static final long serialVersionUID = 1L;

    public MStevBerth(Properties ctx, int STEV_Berth_ID, String trxName)
    {
        super(ctx, STEV_Berth_ID, trxName);
    }

    public MStevBerth(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

    /** Cek apakah kapal dengan draft tertentu aman sandar di dermaga ini */
    public boolean isDraftSafe(java.math.BigDecimal vesselDraftMeter)
    {
        if (getDepthMeter() == null || vesselDraftMeter == null)
            return true; // data tidak lengkap, tidak diblokir otomatis
        return getDepthMeter().compareTo(vesselDraftMeter) >= 0;
    }

    @Override
    protected boolean beforeSave(boolean newRecord)
    {
        if (getLengthMeter() != null && getLengthMeter().signum() < 0)
        {
            log.saveError("Error", "Panjang dermaga tidak boleh negatif");
            return false;
        }
        return true;
    }
}
