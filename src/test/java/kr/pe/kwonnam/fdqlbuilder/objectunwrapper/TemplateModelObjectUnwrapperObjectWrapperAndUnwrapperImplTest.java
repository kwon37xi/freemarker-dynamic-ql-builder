package kr.pe.kwonnam.fdqlbuilder.objectunwrapper;

import freemarker.template.Configuration;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateModelObjectUnwrapperObjectWrapperAndUnwrapperImplTest extends AbstractTemplateModelObjectUnwrapperTest {

    @Override
    protected TemplateModelObjectUnwrapper populateUnwrapper(Configuration configuration) {
        return new TemplateModelObjectUnwrapperObjectWrapperAndUnwrapperImpl(configuration);
    }

}