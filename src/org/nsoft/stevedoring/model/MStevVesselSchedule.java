package org.nsoft.stevedoring.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.model.MOrder;

/**
 * Jadwal & realisasi sandar kapal (ETA/ETB/ETD, ATA/ATB/ATD), terhubung ke
 * SPK (C_Order) sebagai kontrak awal.
 */
public class MStevVesselSchedule extends X_STEV_VesselSchedule
{
    private static final long serialVersionUID = 1L;

    public static final String DOCSTATUS_Draft      = "DR";
    public static final String DOCSTATUS_InProgress = "IP";
    public static final String DOCSTATUS_Completed  = "CO";
    public static final String DOCSTATUS_Voided     = "VO";

    public MStevVesselSchedule(Properties ctx, int STEV_VesselSchedule_ID, String trxName)
    {
        super(ctx, STEV_VesselSchedule_ID, trxName);
    }

    public MStevVesselSchedule(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

    /** SPK (Sales Order/Surat Perintah Kerja) yang menjadi kontrak awal */
    public MOrder getOrder()
    {
        return new MOrder(getCtx(), getC_Order_ID(), get_TrxName());
    }

    /** Dermaga tempat kapal sandar (STEV_Berth_ID, menggantikan free-text BerthLocation) */
    public MStevBerth getBerth()
    {
        int berthId = get_ValueAsInt("STEV_Berth_ID");
        return berthId > 0 ? new MStevBerth(getCtx(), berthId, get_TrxName()) : null;
    }

    /** Menandai kapal sudah benar-benar sandar (ATB terisi) */
    public boolean isVesselBerthed()
    {
        return getATB() != null;
    }

    @Override
    protected boolean beforeSave(boolean newRecord)
    {
        Timestamp eta = getETA();
        Timestamp etd = getETD();
        if (eta != null && etd != null && etd.before(eta))
        {
            log.saveError("Error", "ETD tidak boleh sebelum ETA");
            return false;
        }
        return true;
    }
}
