package kr.pe.kwonnam.freemarkerdynamicqlbuilder;

import freemarker.template.Configuration;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.methods.ParamMethod;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.objectunwrapper.TemplateModelObjectUnwrapper;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter.ParameterConverter;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FreemarkerDynamicQlBuilderImplTest {
    public static final String TEST_QL_DIRECTIVE_PREFIX = "Q";
    public static final String TEST_PARAM_METHOD_NAME = "P";
    public static final String TEST_QUERY_TEMPLATE_NAME_POSTFIX = ".QL.FTL";

    private Logger log = LoggerFactory.getLogger(FreemarkerDynamicQlBuilderImplTest.class);

    @Mock
    private Configuration freemarkerConfiguration;

    @Mock
    private TemplateModelObjectUnwrapper templateModelObjectUnwrapper;

    @Mock
    private ParameterConverter parameterConverter1;

    @Mock
    private ParameterConverter parameterConverter2;

    @Mock
    private Template template;

    private FreemarkerDynamicQlBuilderImpl builder;

    private Map<String, ParameterConverter> parameterConverters;

    private Map<String, Object> dataModel;

    @Before
    public void setUp() throws Exception {
        parameterConverters = new HashMap<String, ParameterConverter>();
        parameterConverters.put("pc1", parameterConverter1);
        parameterConverters.put("pc2", parameterConverter2);

        builder = new FreemarkerDynamicQlBuilderImpl();
        builder.setFreemarkerConfiguration(freemarkerConfiguration);
        builder.setQlDirectivePrefix(TEST_QL_DIRECTIVE_PREFIX);
        builder.setParamMethodName(TEST_PARAM_METHOD_NAME);
        builder.setQueryTemplateNamePostfix(TEST_QUERY_TEMPLATE_NAME_POSTFIX);
        builder.setTemplateModelObjectUnwrapper(templateModelObjectUnwrapper);
        builder.setParameterConverters(parameterConverters);

        dataModel = new HashMap<String, Object>();
    }

    @Test
    public void buildQuery_name_null() throws Exception {
        try {
            builder.buildQuery(null, dataModel);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), is("queryTemplateName must not be null or empty."));
        }
    }

    @Test
    public void buildQuery_name_empty() throws Exception {
        try {
            builder.buildQuery("", dataModel);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), is("queryTemplateName must not be null or empty."));
        }
    }

    @Test
    public void buildQuery_dataModel_null() throws Exception {
        try {
            builder.buildQuery("hello/ql", null);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), is("dataModel must not be null."));
        }
    }

    @Test
    public void buildQuery_paramName_collision() throws Exception {
        dataModel.put(TEST_PARAM_METHOD_NAME, "userId");

        try {
            builder.buildQuery("/users/selectByUserId", dataModel);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), is("dataModel must not contain paramMethodName(P) as a key."));
        }
    }

    @Test
    public void buildQuery_qlDiretivePrefix_collision() throws Exception {
        dataModel.put(TEST_QL_DIRECTIVE_PREFIX, "someThingElse");

        try {
            builder.buildQuery("hello/world", dataModel);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), is("dataModel must not contain qlDirectivePrefix(Q) as a key."));
        }
    }

    @Test
    public void buildQuery_success() throws Exception {
        final String queryTemplateName = "/users/select";

        when(freemarkerConfiguration.getTemplate(queryTemplateName + TEST_QUERY_TEMPLATE_NAME_POSTFIX)).thenReturn(template);
        when(templateModelObjectUnwrapper.unwrap(any(SimpleScalar.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                SimpleScalar simpleScalaro = (SimpleScalar) invocation.getArguments()[0];
                return simpleScalaro.getAsString();
            }
        });

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();

                @SuppressWarnings("unchecked")
                Map<String, Object> finalDataModel = (Map<String, Object>) arguments[0];

                ParamMethod paramMethod = (ParamMethod) finalDataModel.get(TEST_PARAM_METHOD_NAME);
                assertThat(paramMethod, notNullValue());

                StringWriter out = (StringWriter) arguments[1];
                paramMethod.exec(args(new SimpleScalar((String) finalDataModel.get("firstKey"))));
                paramMethod.exec(args(new SimpleScalar((String) finalDataModel.get("secondKey"))));
                out.append("SELECT * FROM SOMEWHERE id IN (?,?)");
                return null;
            }
        }).when(template).process(any(Map.class), any(StringWriter.class));

        dataModel.put("firstKey", "hello");
        dataModel.put("secondKey", "world");

        DynamicQuery dynamicQuery = builder.buildQuery(queryTemplateName, dataModel);
        log.debug("buildQuery result : {}", dynamicQuery);

        assertThat(dynamicQuery.getQueryString(), is("SELECT * FROM SOMEWHERE id IN (?,?)"));
        assertThat(dynamicQuery.getQueryParameters().size(), is(2));

        assertThat(dynamicQuery.getQueryParameters().contains("hello"), is(true));
        assertThat(dynamicQuery.getQueryParameters().contains("world"), is(true));
    }

    private List<Object> args(Object... args) {
        return new ArrayList<Object>(Arrays.asList(args));
    }

    @Test
    public void buildQuery_getTemplate_throws_exception() throws Exception {
        final String queryTemplateName = "/users/update";

        IOException expectedException = new IOException("something happen!");
        doThrow(expectedException).when(freemarkerConfiguration).getTemplate(queryTemplateName + TEST_QUERY_TEMPLATE_NAME_POSTFIX);

        try {
            builder.buildQuery(queryTemplateName, dataModel);
            fail("Must throw an exception - IllegalStateException");
        } catch (IllegalStateException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), is("Can not create freemarker template - /users/update.QL.FTL."));
            assertThat(ex.getCause(), CoreMatchers.<Throwable>sameInstance(expectedException));
        }
    }

    @Test
    public void buildQuery_template_process_throws_exception() throws Exception {
        final String queryTemplateName = "/post/select";

        when(freemarkerConfiguration.getTemplate(queryTemplateName + TEST_QUERY_TEMPLATE_NAME_POSTFIX)).thenReturn(template);
        IOException expectedException = new IOException("something happens when processing template");
        doThrow(expectedException).when(template).process(anyObject(), any(StringWriter.class));

        try {
            builder.buildQuery(queryTemplateName, dataModel);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalStateException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), is("Can not process freemarker template - /post/select.QL.FTL."));
            assertThat(ex.getCause(), CoreMatchers.<Throwable>sameInstance(expectedException));
        }
    }
}