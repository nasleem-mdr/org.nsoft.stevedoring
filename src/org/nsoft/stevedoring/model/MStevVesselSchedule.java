/***********************************************************************
 * This file is part of iDempiere ERP Open Source                      *
 * http://www.idempiere.org                                            *
 *                                                                     *
 * Copyright (C) Contributors                                          *
 *                                                                     *
 * This program is free software; you can redistribute it and/or       *
 * modify it under the terms of the GNU General Public License         *
 * as published by the Free Software Foundation; either version 2      *
 * of the License, or (at your option) any later version.              *
 *                                                                     *
 * This program is distributed in the hope that it will be useful,     *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of      *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
 * GNU General Public License for more details.                        *
 *                                                                     *
 * You should have received a copy of the GNU General Public License   *
 * along with this program; if not, write to the Free Software         *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
 * MA 02110-1301, USA.                                                 *
 *                                                                     *
 * Contributors:                                                       *
 * - Nasleem - NSoft - IDempiere                                       *
 **********************************************************************/

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
        // --- Validasi urutan waktu rencana (Estimated): ETA -> ETB -> ETD ---
        String err = validateSequence(getETA(), getETB(), "ETB cannot be before ETA");
        if (err != null) { log.saveError("Error", err); return false; }

        err = validateSequence(getETB(), getETD(), "ETD cannot be before ETB");
        if (err != null) { log.saveError("Error", err); return false; }

        err = validateSequence(getETA(), getETD(), "ETD cannot be before ETA");
        if (err != null) { log.saveError("Error", err); return false; }

        // --- Validasi urutan waktu realisasi (Actual): ATA -> ATB -> ATD ---
        err = validateSequence(getATA(), getATB(), "ATB (start berthing) cannot be before ATA (ship arrive)");
        if (err != null) { log.saveError("Error", err); return false; }

        err = validateSequence(getATB(), getATD(), "ATD (depature) cannot be before ATB (start berthing)");
        if (err != null) { log.saveError("Error", err); return false; }

        err = validateSequence(getATA(), getATD(), "ATD (depature) cannot be before ATA (ship arrive)");
        if (err != null) { log.saveError("Error", err); return false; }

        
        if (getATB() != null && getATA() == null)
        {
            log.saveError("Error", "ATB tidak boleh diisi sebelum ATA (kapal tiba) diisi");
            return false;
        }
        if (getATD() != null && getATB() == null)
        {
            log.saveError("Error", "ATD tidak boleh diisi sebelum ATB (mulai sandar) diisi");
            return false;
        }

        return true;
    }

    /**
     * Pastikan `first` tidak sesudah `second` bila keduanya terisi.
     * Mengembalikan pesan error, atau null jika valid / salah satu kosong
     * (field Actual memang boleh belum terisi di tengah proses).
     */
    private String validateSequence(Timestamp first, Timestamp second, String errorMessage)
    {
        if (first != null && second != null && second.before(first))
            return errorMessage;
        return null;
    }
}
