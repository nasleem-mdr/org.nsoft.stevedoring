package org.nsoft.stevedoring.model;

import java.sql.ResultSet;
import java.util.Properties;

/**
 * Detail transaksi ritase/bongkar-muat: per shift (di header TallySheet),
 * per palka (HatchNo), per alat berat (Equipment_ID / S_Resource), dan
 * per produk/komoditas (M_Product_ID).
 */
public class MStevTallyLine extends X_STEV_TallyLine
{
    private static final long serialVersionUID = 1L;

    public MStevTallyLine(Properties ctx, int STEV_TallyLine_ID, String trxName)
    {
        super(ctx, STEV_TallyLine_ID, trxName);
    }

    public MStevTallyLine(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

    @Override
    protected boolean beforeSave(boolean newRecord)
    {
        if (getQtyMoved() != null && getQtyMoved().signum() <= 0)
        {
            log.saveError("Error", "QtyMoved harus lebih besar dari 0");
            return false;
        }
        if (getTimeStart() != null && getTimeEnd() != null && getTimeEnd().before(getTimeStart()))
        {
            log.saveError("Error", "TimeEnd tidak boleh sebelum TimeStart");
            return false;
        }
        return true;
    }
}
