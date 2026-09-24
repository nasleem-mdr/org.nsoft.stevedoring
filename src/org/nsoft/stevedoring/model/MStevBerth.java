package org.nsoft.stevedoring.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Berth Data Master. Use as reference table by
 * STEV_VesselSchedule.STEV_Berth_ID 
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

    /**
     * Bandingkan kapasitas dermaga ini terhadap kapal tertentu (panjang
     * LOA vs LengthMeter, Draft vs DepthMeter). Mengembalikan teks
     * peringatan berisi perbandingan angka kalau ADA potensi masalah,
     * atau null kalau aman / data tidak lengkap (tidak diblokir otomatis
     * saat data belum lengkap, supaya tidak menghalangi input awal).
     */
    public String getCapacityWarning(MStevVessel vessel)
    {
        if (vessel == null)
            return null;

        StringBuilder warning = new StringBuilder();

        if (getDepthMeter() != null && vessel.getDraftMeter() != null
                && getDepthMeter().compareTo(vessel.getDraftMeter()) < 0)
        {
            warning.append(String.format(
                    "Berth depth (%.2fm) is shallower than vessel draft (%.2fm). ",
                    getDepthMeter(), vessel.getDraftMeter()));
        }

        if (getLengthMeter() != null && vessel.getLOA() != null
                && getLengthMeter().compareTo(vessel.getLOA()) < 0)
        {
            warning.append(String.format(
		    "Berth length (%.2fm) is shorter than vessel LOA (%.2fm). ",
                    getLengthMeter(), vessel.getLOA()));
        }

        return warning.length() > 0 ? warning.toString().trim() : null;
    }

    @Override
    protected boolean beforeSave(boolean newRecord)
    {
        if (getLengthMeter() != null && getLengthMeter().signum() < 0)
        {
            log.saveError("Error", "Berth length can't be negative");
            return false;
        }
        return true;
    }
}
