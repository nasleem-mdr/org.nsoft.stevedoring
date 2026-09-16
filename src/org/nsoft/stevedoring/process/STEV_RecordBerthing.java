package org.nsoft.stevedoring.process;

import java.sql.Timestamp;

import org.compiere.process.SvrProcess;
import org.nsoft.stevedoring.model.MStevVesselSchedule;

/**
 * Process tombol "Mulai Sandar" pada window STEV_VesselSchedule.
 * Hanya bisa dijalankan setelah ATA (Kapal Tiba) tercatat — menjaga
 * urutan realisasi ATA -> ATB -> ATD tetap konsisten dari sisi proses,
 * bukan cuma validasi di beforeSave.
 */
public class STEV_RecordBerthing extends SvrProcess
{
    private int p_STEV_VesselSchedule_ID = 0;

    @Override
    protected void prepare()
    {
        p_STEV_VesselSchedule_ID = getRecord_ID();
    }

    @Override
    protected String doIt() throws Exception
    {
        if (p_STEV_VesselSchedule_ID <= 0)
            throw new IllegalStateException("Record Vessel Schedule tidak ditemukan");

        MStevVesselSchedule schedule = new MStevVesselSchedule(getCtx(), p_STEV_VesselSchedule_ID, get_TrxName());
        if (schedule.get_ID() <= 0)
            throw new IllegalStateException("Vessel Schedule ID " + p_STEV_VesselSchedule_ID + " tidak valid");

        if (schedule.getATA() == null)
            throw new IllegalStateException("Kedatangan kapal (ATA) belum dicatat — "
                    + "jalankan proses 'Kapal Tiba' terlebih dahulu");

        if (schedule.getATB() != null)
            throw new IllegalStateException("ATB sudah tercatat sebelumnya ("
                    + schedule.getATB() + ") — tidak bisa dicatat ulang");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        schedule.setATB(now);
        schedule.saveEx();

        return "Kapal tercatat mulai sandar pada " + now;
    }
}
