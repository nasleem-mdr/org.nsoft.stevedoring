package org.nsoft.stevedoring.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Detail kepemilikan & spesifikasi alat berat — satelit 1:1 dari
 * S_Resource. Menangani dua skenario:
 *   - OWN  : alat milik sendiri, wajib terhubung ke A_Asset (dimensi
 *            akuntansi NF13 / depresiasi).
 *   - RENT : alat sewaan pihak ketiga, wajib terhubung ke C_BPartner
 *            (vendor) + data kontrak sewa.
 */
public class MStevEquipmentDetail extends X_STEV_EquipmentDetail
{
    private static final long serialVersionUID = 1L;

    public static final String OWNERSHIPTYPE_Owned  = "OWN";
    public static final String OWNERSHIPTYPE_Rented = "RENT";

    public MStevEquipmentDetail(Properties ctx, int STEV_EquipmentDetail_ID, String trxName)
    {
        super(ctx, STEV_EquipmentDetail_ID, trxName);
    }

    public MStevEquipmentDetail(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

    public boolean isOwned()
    {
        return OWNERSHIPTYPE_Owned.equals(getOwnershipType());
    }

    public boolean isRented()
    {
        return OWNERSHIPTYPE_Rented.equals(getOwnershipType());
    }

    @Override
    protected boolean beforeSave(boolean newRecord)
    {
        if (isOwned())
        {
            if (getA_Asset_ID() <= 0)
            {
                log.saveError("Error", "Alat berstatus 'Milik Sendiri' wajib terhubung ke A_Asset");
                return false;
            }
            if (getC_BPartner_ID() > 0)
            {
                log.saveError("Error", "Alat berstatus 'Milik Sendiri' tidak boleh punya vendor sewa (C_BPartner_ID)");
                return false;
            }
        }
        else if (isRented())
        {
            if (getC_BPartner_ID() <= 0)
            {
                log.saveError("Error", "Alat berstatus 'Sewa' wajib diisi vendor (C_BPartner_ID)");
                return false;
            }
            if (getA_Asset_ID() > 0)
            {
                log.saveError("Error", "Alat berstatus 'Sewa' tidak boleh terhubung ke A_Asset milik sendiri");
                return false;
            }
            if (getRentalEndDate() != null && getRentalStartDate() != null
                    && getRentalEndDate().before(getRentalStartDate()))
            {
                log.saveError("Error", "RentalEndDate tidak boleh sebelum RentalStartDate");
                return false;
            }
        }
        return true;
    }
}
