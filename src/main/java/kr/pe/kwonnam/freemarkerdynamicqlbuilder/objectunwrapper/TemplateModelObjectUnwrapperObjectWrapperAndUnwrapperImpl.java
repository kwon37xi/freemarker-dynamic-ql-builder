package kr.pe.kwonnam.freemarkerdynamicqlbuilder.objectunwrapper;

import freemarker.template.*;

/**
 * {@link ObjectWrapperAndUnwrapper} based TemplateModel Unwrapper.
 * <p>
 * {@link ObjectWrapperAndUnwrapper} is currently(freemerker 2.3.23) experimental feature.
 * So this is just for test implementation, DO NOT use in production.
 */
public class TemplateModelObjectUnwrapperObjectWrapperAndUnwrapperImpl implements TemplateModelObjectUnwrapper {

    private ObjectWrapperAndUnwrapper objectWrapperAndUnwrapper;

    public TemplateModelObjectUnwrapperObjectWrapperAndUnwrapperImpl(Configuration configuration) {
        // ObjectWrapper must implement ObjectWrapperAndUnwrapper interface
        this.objectWrapperAndUnwrapper = (ObjectWrapperAndUnwrapper) configuration.getObjectWrapper();
    }

    @Override
    public Object unwrap(TemplateModel dataModel) throws TemplateModelException {
        Object unwrapped = objectWrapperAndUnwrapper.unwrap(dataModel);

        if (unwrapped instanceof TemplateModelAdapter) {
            throw new TemplateModelException(dataModel.getClass().getCanonicalName() + " is not supported.");
        }
        return unwrapped;
    }
}
