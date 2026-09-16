package org.nsoft.stevedoring.base;

import org.adempiere.base.IModelValidatorFactory;
import org.compiere.model.ModelValidator;
import org.nsoft.stevedoring.validator.StevedoringDocumentValidator;

//@Component(property = {"service.ranking:Integer=100"}, service = org.adempiere.base.IModelValidatorFactory.class)
public class StevedoringValidatorFactory implements IModelValidatorFactory 
{
    @Override
    public ModelValidator newModelValidatorInstance(String className) 
    {
        if (StevedoringDocumentValidator.class.getName().equals(className)) 
        {
            return new StevedoringDocumentValidator();
        }
        return null;
    }
}