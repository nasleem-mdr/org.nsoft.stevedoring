package org.nsoft.stevedoring.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Master data kapal.
 * NOTE: X_STEV_Vessel adalah superclass hasil "Generate Model" iDempiere
 * setelah AD_Table STEV_Vessel disinkronkan dari tabel fisik (lihat
 * sql/01_DDL_STEV_TABLES.sql). Kelas ini hanya menambahkan helper/validasi
 * ringan di atasnya.
 */
public class MStevVessel extends X_STEV_Vessel
{
    private static final long serialVersionUID = 1L;

    public MStevVessel(Properties ctx, int STEV_Vessel_ID, String trxName)
    {
        super(ctx, STEV_Vessel_ID, trxName);
    }

    public MStevVessel(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

    @Override
    protected boolean beforeSave(boolean newRecord)
    {
        if (getLOA() != null && getLOA().signum() < 0)
        {
            log.saveError("Error", "LOA tidak boleh negatif");
            return false;
        }
        return true;
    }
}
