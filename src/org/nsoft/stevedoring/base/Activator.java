package org.nsoft.stevedoring.base;

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
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
    }
}
