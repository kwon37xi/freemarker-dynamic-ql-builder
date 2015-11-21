package kr.pe.kwonnam.fdqlbuilder;

import freemarker.template.Configuration;
import kr.pe.kwonnam.fdqlbuilder.objectunwrapper.TemplateModelObjectUnwrapper;
import kr.pe.kwonnam.fdqlbuilder.paramconverter.ParameterConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class FreemarkerDynamicQlBuilderFactoryTest {
    private Logger log = LoggerFactory.getLogger(FreemarkerDynamicQlBuilderFactoryTest.class);

    @Mock
    private Configuration freemarkerConfiguration;

    @Mock
    private ParameterConverter parameterConverter1;

    @Mock
    private ParameterConverter parameterConverter2;

    @Mock
    private TemplateModelObjectUnwrapper templateModelObjectUnwrapper;

    private FreemarkerDynamicQlBuilderFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new FreemarkerDynamicQlBuilderFactory(freemarkerConfiguration);
    }

    @Test
    public void constructor_freemarkerConfiguration_null() throws Exception {
        try {
            new FreemarkerDynamicQlBuilderFactory(null);
            fail("Must throw an exception");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), containsString("freemarkerConfiguration must not be null."));
        }
    }

    @Test
    public void qlDirectivePrefix_null() throws Exception {
        try {
            factory.qlDirectivePrefix(null);
            fail("Must throw an exception");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), containsString("qlDirectivePrefix must not be null or empty."));
        }
    }

    @Test
    public void qlDirectivePrefix_empty() throws Exception {
        try {
            factory.qlDirectivePrefix("");
            fail("Must throw an exception");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), containsString("qlDirectivePrefix must not be null or empty."));
        }
    }

    @Test
    public void paramMethodName_null() throws Exception {
        try {
            factory.paramMethodName(null);
            fail("Must throw an exception");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), containsString("paramMethodName must not be null or empty."));
        }
    }

    @Test
    public void paramMethodName_empty() throws Exception {
        try {
            factory.paramMethodName("");
            fail("Must throw an exception");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), containsString("paramMethodName must not be null or empty."));
        }
    }

    @Test
    public void queryTemplateNamePostfix_null() throws Exception {
        try {
            factory.queryTemplateNamePostfix(null);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), is("queryTemplateNamePostfix must not be null."));
        }
    }

    @Test
    public void templateModelObjectUnwrapper_null() throws Exception {
        try {
            factory.templateModelObjectUnwrapper(null);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), is("templateModelObjectUnwrapper must not be null."));
        }
    }

    @Test
    public void addParameterConverter_key_null() throws Exception {
        try {
            factory.addParameterConverter(null, parameterConverter1);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), is("parameterConverterName must not be null or empty."));
        }
    }

    @Test
    public void addParameterConverter_key_empty() throws Exception {
        try {
            factory.addParameterConverter("", parameterConverter1);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), is("parameterConverterName must not be null or empty."));
        }
    }

    @Test
    public void addParameterConverter_value_null() throws Exception {
        try {
            factory.addParameterConverter("pc1", null);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), is("parameterConverter must not be null."));
        }
    }

    @Test
    public void addParameterConverter_duplicated_key() throws Exception {
        factory.addParameterConverter("parameterConverterName", parameterConverter1);

        try {
            factory.addParameterConverter("parameterConverterName", parameterConverter2);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), is("parameterConverterName - parameterConverterName is duplicated."));
        }
    }

    @Test
    public void addAllParameterConverters_null() throws Exception {
        assertThat(factory.getParameterConverters().size(), is(0));

        factory.addAllParameterConverters(null);
        assertThat(factory.getParameterConverters().size(), is(0));
    }

    @Test
    public void addAllParameterConverters() throws Exception {
        assertThat(factory.getParameterConverters().size(), is(0));

        Map<String, ParameterConverter> otherParameterConverters = new HashMap<String, ParameterConverter>();
        otherParameterConverters.put("pc1", parameterConverter1);
        otherParameterConverters.put("pc2", parameterConverter2);

        factory.addAllParameterConverters(otherParameterConverters);

        assertThat(factory.getParameterConverters().size(), is(2));
        assertThat(factory.getParameterConverters().keySet(), hasItems("pc1", "pc2"));
    }

    @Test
    public void clearParameterConverters() throws Exception {
        factory.addParameterConverter("1", parameterConverter1);
        factory.addParameterConverter("2", parameterConverter2);
        factory.addParameterConverter("3", parameterConverter1);
        factory.addParameterConverter("4", parameterConverter2);
        assertThat(factory.getParameterConverters().size(), is(4));

        factory.clearParameterConverters();

        assertThat(factory.getParameterConverters().size(), is(0));
    }

    @Test
    public void getFreemarkerDynamicQlBuilder() throws Exception {
        FreemarkerDynamicQlBuilderImpl freemarkerDynamicQlBuilder = (FreemarkerDynamicQlBuilderImpl) factory
                .qlDirectivePrefix("Q")
                .paramMethodName("P")
                .queryTemplateNamePostfix(".QL.FTL")
                .templateModelObjectUnwrapper(templateModelObjectUnwrapper)
                .addParameterConverter("pc1", parameterConverter1)
                .addParameterConverter("pc2", parameterConverter2)
                .getFreemarkerDynamicQlBuilder();

        assertThat(freemarkerDynamicQlBuilder.getFreemarkerConfiguration(), sameInstance(freemarkerConfiguration));
        assertThat(freemarkerDynamicQlBuilder.getQlDirectivePrefix(), is("Q"));
        assertThat(freemarkerDynamicQlBuilder.getParamMethodName(), is("P"));
        assertThat(freemarkerDynamicQlBuilder.getQueryTemplateNamePostfix(), is(".QL.FTL"));
        assertThat(freemarkerDynamicQlBuilder.getTemplateModelObjectUnwrapper(), sameInstance(templateModelObjectUnwrapper));
        assertThat(freemarkerDynamicQlBuilder.getParameterConverters().size(), is(2));
        assertThat(freemarkerDynamicQlBuilder.getParameterConverters().values(), hasItems(parameterConverter1, parameterConverter2));
        assertThat(freemarkerDynamicQlBuilder.getParameterConverters().keySet(), hasItems("pc1", "pc2"));
    }
}