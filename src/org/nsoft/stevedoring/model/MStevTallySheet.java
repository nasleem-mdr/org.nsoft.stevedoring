package org.nsoft.stevedoring.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.Query;

/**
 * Header pencatatan tally per shift di lapangan. Baris detail ada di
 * {@link MStevTallyLine} (per palka/alat berat/produk).
 */
public class MStevTallySheet extends X_STEV_TallySheet
{
    private static final long serialVersionUID = 1L;

    public MStevTallySheet(Properties ctx, int STEV_TallySheet_ID, String trxName)
    {
        super(ctx, STEV_TallySheet_ID, trxName);
    }

    public MStevTallySheet(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

    /** Semua baris tally milik tally sheet ini */
    public MStevTallyLine[] getLines()
    {
        java.util.List<MStevTallyLine> list = new Query(getCtx(), MStevTallyLine.Table_Name,
                MStevTallyLine.COLUMNNAME_STEV_TallySheet_ID + "=?", get_TrxName())
                .setParameters(getSTEV_TallySheet_ID())
                .setOrderBy(MStevTallyLine.COLUMNNAME_Line)
                .list();
        return list.toArray(new MStevTallyLine[0]);
    }

    /** Total QtyMoved seluruh baris di tally sheet ini */
    public BigDecimal getTotalQtyMoved()
    {
        BigDecimal total = BigDecimal.ZERO;
        for (MStevTallyLine line : getLines())
        {
            if (line.getQtyMoved() != null)
                total = total.add(line.getQtyMoved());
        }
        return total;
    }
}
