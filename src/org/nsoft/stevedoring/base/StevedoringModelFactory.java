package org.nsoft.stevedoring.base;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.Properties;

import org.adempiere.base.IModelFactory;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.nsoft.stevedoring.model.MStevStatementOfFact;
import org.nsoft.stevedoring.model.MStevTallyLine;
import org.nsoft.stevedoring.model.MStevTallySheet;
import org.nsoft.stevedoring.model.MStevVessel;
import org.nsoft.stevedoring.model.MStevVesselSchedule;

/**
 * Mendaftarkan model class custom Stevedoring ke iDempiere lewat OSGi
 * IModelFactory, sebagai alternatif dari mengisi AD_Table.ClassName
 * lewat GUI. Dengan cara ini, plugin tetap "self-contained": cukup
 * deploy bundle ini, tidak perlu langkah manual tambahan di Application
 * Dictionary untuk pemetaan class (registrasi AD_Table/AD_Column via
 * DB.Synchronize tetap diperlukan, hanya pemetaan class-nya yang otomatis).
 */
public class StevedoringModelFactory implements IModelFactory
{
    @Override
    public Class<?> getClass(String tableName)
    {
        switch (tableName)
        {
            case MStevVessel.Table_Name:
                return MStevVessel.class;
            case MStevVesselSchedule.Table_Name:
                return MStevVesselSchedule.class;
            case MStevTallySheet.Table_Name:
                return MStevTallySheet.class;
            case MStevTallyLine.Table_Name:
                return MStevTallyLine.class;
            case MStevStatementOfFact.Table_Name:
                return MStevStatementOfFact.class;
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
            throw new RuntimeException("Gagal instantiate " + clazz.getName(), e);
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
            throw new RuntimeException("Gagal instantiate " + clazz.getName(), e);
        }
    }
}
