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
import java.util.Properties;

/**
 * Berth Data Master. Use as reference table by
 * STEV_VesselSchedule.STEV_Berth_ID 
 */
public class MStevBerth extends X_STEV_Berth
{
    private static final long serialVersionUID = 1L;

    public MStevBerth(Properties ctx, int STEV_Berth_ID, String trxName)
    {
        super(ctx, STEV_Berth_ID, trxName);
    }

    public MStevBerth(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

    /** Cek apakah kapal dengan draft tertentu aman sandar di dermaga ini */
    public boolean isDraftSafe(java.math.BigDecimal vesselDraftMeter)
    {
        if (getDepthMeter() == null || vesselDraftMeter == null)
            return true; // data tidak lengkap, tidak diblokir otomatis
        return getDepthMeter().compareTo(vesselDraftMeter) >= 0;
    }

    /**
     * Compares this berth's capacity against a specific vessel (LOA vs. LengthMeter,
     * Draft vs. DepthMeter). Returns a warning message containing the numerical
     * comparison if there is a potential issue, or null if safe or data is incomplete
     * (automatic blocking does not occur when data is incomplete, so as not to
     * hinder initial data entry).
     */
    public String getCapacityWarning(MStevVessel vessel)
    {
        if (vessel == null)
            return null;

        StringBuilder warning = new StringBuilder();

        if (getDepthMeter() != null && vessel.getDraftMeter() != null
                && getDepthMeter().compareTo(vessel.getDraftMeter()) < 0)
        {
            warning.append(String.format(
                    "Berth depth (%.2fm) is shallower than vessel draft (%.2fm). ",
                    getDepthMeter(), vessel.getDraftMeter()));
        }

        if (getLengthMeter() != null && vessel.getLOA() != null
                && getLengthMeter().compareTo(vessel.getLOA()) < 0)
        {
            warning.append(String.format(
		    "Berth length (%.2fm) is shorter than vessel LOA (%.2fm). ",
                    getLengthMeter(), vessel.getLOA()));
        }

        return warning.length() > 0 ? warning.toString().trim() : null;
    }

    @Override
    protected boolean beforeSave(boolean newRecord)
    {
        if (getLengthMeter() != null && getLengthMeter().signum() < 0)
        {
            log.saveError("Error", "Berth length can't be negative");
            return false;
        }
        return true;
    }
}
