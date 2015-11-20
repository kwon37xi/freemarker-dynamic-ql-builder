package kr.pe.kwonnam.fdqlbuilder.objectunwrapper;

import freemarker.ext.util.WrapperTemplateModel;
import freemarker.template.*;

/**
 * Defautl {@link TemplateModel} Unwrapper.
 * <p/>
 * This version is not able to unwrap some dataModels. But most of them are also not possible to be set into prepared statement.
 */
public class TemplateModelObjectUnwrapperDefaultImpl implements TemplateModelObjectUnwrapper {
    @Override
    public Object unwrap(TemplateModel dataModel) throws TemplateModelException {
        if (dataModel instanceof SimpleScalar) {
            return ((SimpleScalar)dataModel).getAsString();
        }

        if (dataModel instanceof SimpleNumber) {
            return ((SimpleNumber)dataModel).getAsNumber();
        }

        if (dataModel instanceof SimpleDate) {
            return ((SimpleDate)dataModel).getAsDate();
        }

        if (dataModel instanceof WrapperTemplateModel) {
            return ((WrapperTemplateModel)dataModel).getWrappedObject();
        }
        throw new TemplateModelException(dataModel.getClass().getCanonicalName() + " is not supported.");
    }
}