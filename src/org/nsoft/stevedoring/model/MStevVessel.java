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
 * Master data kapal.
 * NOTE: X_STEV_Vessel is superclass "Generate Model" reuslt from iDempiere
 * after AD_Table STEV_Vessel synchronize by physical table(see sql/01_DDL_STEV_TABLES.sql). 
 * This class only add easy helper/validation
*/
public class MStevVessel extends X_STEV_Vessel
{
    private static final long serialVersionUID = 1L;

    public MStevVessel(Properties ctx, int STEV_Vessel_ID, String trxName)
    {
        super(ctx, STEV_Vessel_ID, trxName);
    }

    public MStevVessel(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
    }

    @Override
    protected boolean beforeSave(boolean newRecord)
    {
        if (getLOA() != null && getLOA().signum() < 0)
        {
            log.saveError("Error", "LOA cannot be negative");
            return false;
        }
        return true;
    }
}
