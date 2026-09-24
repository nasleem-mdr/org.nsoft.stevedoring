package org.nsoft.stevedoring.base;

import org.adempiere.webui.factory.IFormFactory;
import org.adempiere.webui.panel.ADForm;
import org.nsoft.stevedoring.form.STEV_SignaturePadForm;

/**
 * Registrasi Form custom Stevedoring lewat OSGi IFormFactory — pola
 * yang sama persis dengan StevedoringProcessFactory/StevedoringModelFactory
 * (mengisi AD_Form.Classname manual di GUI saja TIDAK cukup di OSGi,
 * sama seperti kasus AD_Process yang sudah kita perbaiki sebelumnya).
 *
 * CATATAN: interface IFormFactory di versi iDempiere ini hanya punya
 * SATU method — newFormInstance(String). Tidak ada method
 * newFormController(String)/tipe IFormController di versi ini —
 * kalau ditambahkan manual, akan gagal compile karena tidak ada
 * supertype method yang cocok untuk di-override, dan tipe
 * IFormController sendiri tidak dikenal di classpath.
 */
public class StevedoringFormFactory implements IFormFactory
{
    @Override
    public ADForm newFormInstance(String className)
    {
        if (STEV_SignaturePadForm.class.getName().equals(className))
            return new STEV_SignaturePadForm();
        return null;
    }
}