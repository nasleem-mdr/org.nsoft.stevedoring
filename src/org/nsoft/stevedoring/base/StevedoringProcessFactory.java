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

package org.nsoft.stevedoring.base;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;
import org.nsoft.stevedoring.process.STEV_CompleteStatementOfFact;
import org.nsoft.stevedoring.process.STEV_RecordBerthing;
import org.nsoft.stevedoring.process.STEV_RecordDeparture;
import org.nsoft.stevedoring.process.STEV_RecordVesselArrival;
import org.nsoft.stevedoring.process.STEV_OpenSignaturePad;
import org.nsoft.stevedoring.process.STEV_ViewSignature;

/**
* Custom Process Stevedoring registration factory via OSGi (IProcessFactory).
* Required so that the iDempiere ClassLoader can resolve classes
* from this bundle (prevents the 'Failed to create new process instance' error).
*/
public class StevedoringProcessFactory implements IProcessFactory
{
    @Override
    public ProcessCall newProcessInstance(String className)
    {
        if (STEV_RecordVesselArrival.class.getName().equals(className))
            return new STEV_RecordVesselArrival();
        if (STEV_RecordBerthing.class.getName().equals(className))
            return new STEV_RecordBerthing();
        if (STEV_RecordDeparture.class.getName().equals(className))
            return new STEV_RecordDeparture();
        if (STEV_CompleteStatementOfFact.class.getName().equals(className))
            return new STEV_CompleteStatementOfFact();
        if (STEV_OpenSignaturePad.class.getName().equals(className))
            return new STEV_OpenSignaturePad();
        if (STEV_ViewSignature.class.getName().equals(className))
            return new STEV_ViewSignature();

        return null; // biarkan factory lain (default core) yang coba, kalau ada
    }
}
