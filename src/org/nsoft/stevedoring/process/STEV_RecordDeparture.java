package org.nsoft.stevedoring.process;

import java.sql.Timestamp;

import org.compiere.process.SvrProcess;
import org.nsoft.stevedoring.model.MStevVesselSchedule;

/**
 * Process tombol "Kapal Berangkat" pada window STEV_VesselSchedule.
 * Hanya bisa dijalankan setelah ATB (Mulai Sandar) tercatat.
 *
 * Tidak otomatis mengubah DocStatus menjadi Completed di sini — status
 * Completed pada Vessel Schedule tetap dipicu dari sisi
 * StevedoringDocumentValidator saat STEV_StatementOfFact di-Complete
 * (lihat validator/StevedoringDocumentValidator.java), supaya "selesai
 * operasional" (kapal berangkat) dan "selesai administratif" (SoF
 * disetujui & ditandatangani) tetap dua hal yang terpisah.
 */
public class STEV_RecordDeparture extends SvrProcess
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

        if (schedule.getATB() == null)
            throw new IllegalStateException("Kapal belum tercatat sandar (ATB kosong) — "
                    + "jalankan proses 'Mulai Sandar' terlebih dahulu");

        if (schedule.getATD() != null)
            throw new IllegalStateException("ATD sudah tercatat sebelumnya ("
                    + schedule.getATD() + ") — tidak bisa dicatat ulang");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        schedule.setATD(now);
        schedule.saveEx();

        return "Kapal tercatat berangkat pada " + now;
    }
}
