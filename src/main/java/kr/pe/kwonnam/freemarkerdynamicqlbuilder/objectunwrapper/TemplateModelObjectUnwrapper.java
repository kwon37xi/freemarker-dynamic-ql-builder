package kr.pe.kwonnam.freemarkerdynamicqlbuilder.objectunwrapper;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Get java Object from  freemarker's {@link freemarker.template.TemplateModel}.
 */
public interface TemplateModelObjectUnwrapper {
    /**
     * unwrap freemarker {@link freemarker.template.TemplateModel}.
     * @param dataModel freemarker dataModel
     * @return If dataModel is an instance of {@link freemarker.template.TemplateModel} unwrapped java object will be returned.
     * If dataModel is null or not an instance of {@link freemarker.template.TemplateModel}, return dataModel itself.
     */
    Object unwrap(TemplateModel dataModel) throws TemplateModelException;
}
