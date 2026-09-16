package org.nsoft.stevedoring.process;

import java.sql.Timestamp;

import org.compiere.process.SvrProcess;
import org.nsoft.stevedoring.model.MStevVesselSchedule;

/**
 * Process tombol "Kapal Tiba" pada window STEV_VesselSchedule.
 *
 * Sengaja TIDAK membiarkan field ATA diisi lewat edit manual di form —
 * process ini mengisi ATA dengan waktu klik yang sebenarnya (now()),
 * supaya:
 *  - Timestamp akurat, tidak bergantung ketik manual petugas lapangan.
 *  - Bisa dibatasi lewat Role/Process Access (mis. hanya role "Port Ops").
 *  - Validasi urutan (di beforeSave MStevVesselSchedule) jadi lapis kedua,
 *    bukan satu-satunya penjaga — tombol berikutnya (Mulai Sandar) baru
 *    aktif kalau ATA sudah terisi.
 *
 * Registrasi di Application Dictionary:
 *   AD_Process.Name       = "Kapal Tiba (Record Vessel Arrival)"
 *   AD_Process.ClassName  = org.nsoft.stevedoring.process.STEV_RecordVesselArrival
 *   Dipasang sebagai Process button di tab STEV_VesselSchedule
 *   (parameter standar Record_ID otomatis tersedia dari context tab).
 */
public class STEV_RecordVesselArrival extends SvrProcess
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

        if (schedule.getATA() != null)
            throw new IllegalStateException("ATA sudah tercatat sebelumnya ("
                    + schedule.getATA() + ") — tidak bisa dicatat ulang");

        if (MStevVesselSchedule.DOCSTATUS_Voided.equals(schedule.getDocStatus())
                || MStevVesselSchedule.DOCSTATUS_Completed.equals(schedule.getDocStatus()))
            throw new IllegalStateException("Vessel Schedule berstatus " + schedule.getDocStatus()
                    + " — tidak bisa mencatat kedatangan kapal");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        schedule.setATA(now);
        schedule.setDocStatus(MStevVesselSchedule.DOCSTATUS_InProgress);
        schedule.saveEx();

        return "Kapal tercatat tiba pada " + now;
    }
}
