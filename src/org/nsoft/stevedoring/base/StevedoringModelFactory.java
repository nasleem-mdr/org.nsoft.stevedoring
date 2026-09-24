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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.util.Properties;

import org.adempiere.base.IModelFactory;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.nsoft.stevedoring.model.MStevBerth;
import org.nsoft.stevedoring.model.MStevEquipmentDetail;
import org.nsoft.stevedoring.model.MStevStatementOfFact;
import org.nsoft.stevedoring.model.MStevTallyLine;
import org.nsoft.stevedoring.model.MStevTallySheet;
import org.nsoft.stevedoring.model.MStevVessel;
import org.nsoft.stevedoring.model.MStevVesselSchedule;
import org.nsoft.stevedoring.model.MStevStatementOfFactLine;
/**
* Registers a custom Stevedoring model class to iDempiere via OSGi
* IModelFactory, as an alternative to populating AD_Table.ClassName
* via the GUI. This way, the plugin remains "self-contained": simply
* deploy this bundle, no additional manual steps are required in the Application
* Dictionary for class mapping (registering AD_Table/AD_Column via
* DB.Synchronize is still required, only the class mapping is automatic).
*/
public class StevedoringModelFactory implements IModelFactory
{
    private static final CLogger log = CLogger.getCLogger(StevedoringModelFactory.class);

    @Override
    public Class<?> getClass(String tableName)
    {
        switch (tableName)
        {
            case MStevVessel.Table_Name:
                return MStevVessel.class;
            case MStevBerth.Table_Name:
                return MStevBerth.class;
            case MStevEquipmentDetail.Table_Name:
                return MStevEquipmentDetail.class;
            case MStevVesselSchedule.Table_Name:
                return MStevVesselSchedule.class;
            case MStevTallySheet.Table_Name:
                return MStevTallySheet.class;
            case MStevTallyLine.Table_Name:
                return MStevTallyLine.class;
            case MStevStatementOfFact.Table_Name:
                return MStevStatementOfFact.class;
            case MStevStatementOfFactLine.Table_Name:
                return MStevStatementOfFactLine.class;
            default:
                return null;
        }
    }

    @Override
    public PO getPO(String tableName, int Record_ID, String trxName)
    {
        Class<?> clazz = getClass(tableName);
        if (clazz == null)
            return null;
        try
        {
            Constructor<?> c = clazz.getConstructor(Properties.class, int.class, String.class);
            return (PO) c.newInstance(Env.getCtx(), Record_ID, trxName);
        }
        catch (Exception e)
        {
            throw wrapWithRootCause(clazz, e);
        }
    }

    @Override
    public PO getPO(String tableName, ResultSet rs, String trxName)
    {
        Class<?> clazz = getClass(tableName);
        if (clazz == null)
            return null;
        try
        {
            Constructor<?> c = clazz.getConstructor(Properties.class, ResultSet.class, String.class);
            return (PO) c.newInstance(Env.getCtx(), rs, trxName);
        }
        catch (Exception e)
        {
            throw wrapWithRootCause(clazz, e);
        }
    }

    
    private RuntimeException wrapWithRootCause(Class<?> clazz, Exception e)
    {
        Throwable rootCause = (e instanceof InvocationTargetException && e.getCause() != null)
                ? e.getCause()
                : e;
        String message = "Failed to instantiate " + clazz.getName() + " — root cause: "
                + rootCause.getClass().getSimpleName() + ": " + rootCause.getMessage();

        log.severe(message);
        rootCause.printStackTrace();

        return new RuntimeException(message, rootCause);
    }
}

