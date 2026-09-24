package org.nsoft.stevedoring.base;

import org.adempiere.base.Core;
import org.nsoft.stevedoring.callout.STEV_CalloutOrderBPartner;
import org.nsoft.stevedoring.model.MStevVesselSchedule;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator
{
    @Override
    public void start(BundleContext context) throws Exception
    {
        // Registrasi service (ModelFactory, ModelValidator) dilakukan lewat
        // deklarasi Declarative Services di OSGI-INF/, tidak perlu manual
        // context.registerService() di sini.

        // Column Callout: mekanisme resmi OSGi iDempiere (NF9) untuk
        // mendaftarkan IColumnCallout tanpa perlu isi field "Callout" di
        // Application Dictionary — cukup mapping tableName+columnName ke
        // instance callout lewat IMappedColumnCalloutFactory bawaan.
        Core.getMappedColumnCalloutFactory().addMapping(
                MStevVesselSchedule.Table_Name,
                MStevVesselSchedule.COLUMNNAME_C_Order_ID,
                STEV_CalloutOrderBPartner::new);
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
    }
}
