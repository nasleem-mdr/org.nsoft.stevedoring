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
 * Vessel berthing schedules and actuals (ETA/ETB/ETD, ATA/ATB/ATD), linked to
 * the SPK (C_Order) as the initial contract.
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

    /** SPK (Sales Order/Work Order) which is the initial contract */
    public MOrder getOrder()
    {
        return new MOrder(getCtx(), getC_Order_ID(), get_TrxName());
    }

    public MStevBerth getBerth()
    {
        int berthId = get_ValueAsInt("STEV_Berth_ID");
        return berthId > 0 ? new MStevBerth(getCtx(), berthId, get_TrxName()) : null;
    }

    public MStevVessel getVessel()
    {
        int vesselId = getSTEV_Vessel_ID();
        return vesselId > 0 ? new MStevVessel(getCtx(), vesselId, get_TrxName()) : null;
    }

    public boolean isVesselBerthed()
    {
        return getATB() != null;
    }

    @Override
    protected boolean beforeSave(boolean newRecord)
    {
        String err = validateSequence(getETA(), getETB(), "ETB must not be earlier than ETA.");
        if (err != null) { log.saveError("Error", err); return false; }

        err = validateSequence(getETB(), getETD(), "ETD must not be earlier than ETB.");
        if (err != null) { log.saveError("Error", err); return false; }

        err = validateSequence(getETA(), getETD(), "ETD must not be earlier than ETA");
        if (err != null) { log.saveError("Error", err); return false; }

        // --- Validasi urutan waktu realisasi (Actual): ATA -> ATB -> ATD ---
        err = validateSequence(getATA(), getATB(), "ATB (starting berth) must not be earlier than ATA (arrival)");
        if (err != null) { log.saveError("Error", err); return false; }

        err = validateSequence(getATB(), getATD(), "ATD (depature) must not be earlier than ATB (starting berth)");
        if (err != null) { log.saveError("Error", err); return false; }

        err = validateSequence(getATA(), getATD(), "ATD (depature) must not be earlier than ATA (arrival)");
        if (err != null) { log.saveError("Error", err); return false; }

        / --- Cross-validation: a realization entry must not populate a stage
        //     if the preceding stage is not yet filled (e.g., ATB is filled but ATA is empty
        //     implies the vessel is considered berthed without ever having arrived) ---
        if (getATB() != null && getATA() == null)
        {
            log.saveError("Error", "ATB must not be filled before ATA (arriving ship) is filled");
            return false;
        }
        if (getATD() != null && getATB() == null)
        {
            log.saveError("Error", "ATD must not be entered before ATB (start of berthing) is entered.");
            return false;
        }

       // --- Check berth vs. vessel capacity — WARNING only; does NOT
        //     block the save operation. beforeSave() is synchronous and
        //     cannot display an interactive "Proceed/Cancel" dialog
        //     mid-save (that requires a hook at the ZK client level,
        //     not in this model layer)—so the record is saved regardless;
        //     the user is notified via a warning message to ensure
        //     awareness of the risk and can correct the Berth/Vessel
        //     selection later if necessary.
        MStevBerth berth = getBerth();
        MStevVessel vessel = getVessel();
        if (berth != null && vessel != null)
        {
            String capacityWarning = berth.getCapacityWarning(vessel);
            if (capacityWarning != null)
            {
                log.saveWarning("Warning", "Dermaga '" + berth.getName() + "' potentially UNABLE to accommodate the ship '"
                        + vessel.getName() + "': " + capacityWarning
                        + "Data remains saved — please review your choice of terminal/vessel.");
            }
        }

        return true;
    }

    /**
     * Ensure `first` is not after `second` if both are populated.
     * Returns an error message, or null if valid or if one is empty
     * (the Actual field is permitted to be empty mid-process).
     */
    private String validateSequence(Timestamp first, Timestamp second, String errorMessage)
    {
        if (first != null && second != null && second.before(first))
            return errorMessage;
        return null;
    }
}

