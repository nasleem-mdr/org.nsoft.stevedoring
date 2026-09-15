package org.nsoft.stevedoring.base;

import org.adempiere.base.IModelValidatorFactory;
import org.compiere.model.ModelValidator;
import org.nsoft.stevedoring.validator.StevedoringDocumentValidator;

public class StevedoringValidatorFactory implements IModelValidatorFactory
{
    @Override
    public ModelValidator[] getModelValidators(int AD_Client_ID)
    {
        return new ModelValidator[] { new StevedoringDocumentValidator() };
    }

    @Override
    public ModelValidator[] getGlobalValidators()
    {
        return null;
    }
}
