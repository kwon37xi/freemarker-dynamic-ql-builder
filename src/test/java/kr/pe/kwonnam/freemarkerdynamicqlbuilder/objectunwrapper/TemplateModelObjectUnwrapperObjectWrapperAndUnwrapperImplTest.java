package kr.pe.kwonnam.freemarkerdynamicqlbuilder.objectunwrapper;

import freemarker.template.Configuration;

public class TemplateModelObjectUnwrapperObjectWrapperAndUnwrapperImplTest extends AbstractTemplateModelObjectUnwrapperTest {

    @Override
    protected TemplateModelObjectUnwrapper populateUnwrapper(Configuration configuration) {
        return new TemplateModelObjectUnwrapperObjectWrapperAndUnwrapperImpl(configuration);
    }

}