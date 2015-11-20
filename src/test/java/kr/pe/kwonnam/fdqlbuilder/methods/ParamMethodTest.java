package kr.pe.kwonnam.fdqlbuilder.methods;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import kr.pe.kwonnam.fdqlbuilder.EmployeeType;
import kr.pe.kwonnam.fdqlbuilder.User;
import kr.pe.kwonnam.fdqlbuilder.objectunwrapper.TemplateModelObjectUnwrapperDefaultImpl;
import kr.pe.kwonnam.fdqlbuilder.paramconverter.ParameterConverter;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.slf4j.LoggerFactory.getLogger;


@RunWith(MockitoJUnitRunner.class)
public class ParamMethodTest {
    private Logger log = getLogger(ParamMethodTest.class);

    private Configuration configuration;

    private StringTemplateLoader templateLoader;

    private TemplateModelObjectUnwrapperDefaultImpl unwrapper;

    private ParamMethod paramMethod;

    @Mock
    private ParameterConverter booleanToYn;

    @Mock
    private ParameterConverter dateToTime;

    private Map<String, ParameterConverter> parameterConverters;
    private Map<String,Object> dataModel;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration(Configuration.VERSION_2_3_23);

        templateLoader = new StringTemplateLoader();

        configuration.setTemplateLoader(templateLoader);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setNumberFormat("0.######");

        parameterConverters = new HashMap<String, ParameterConverter>();
        parameterConverters.put("booleanToYn", booleanToYn);
        parameterConverters.put("dateToTime", dateToTime);

        unwrapper = new TemplateModelObjectUnwrapperDefaultImpl();
        paramMethod = new ParamMethod(unwrapper, parameterConverters);

        dataModel = new HashMap<String, Object>();
    }

    private String processTemplate(String templateSource) throws IOException, TemplateException {
        Template template = new Template("template", templateSource, configuration);
        StringWriter out = new StringWriter();

        dataModel.put("param", paramMethod);
        template.process(dataModel, out);

        return out.toString();
    }


    @Test
    public void constructor_unrwrapper_null() throws Exception {
        try {
            new ParamMethod(null, parameterConverters);
            fail("Must throw IllegalArgumentException.");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), containsString("templateModelObjectUnwrapper must not be null."));
        }
    }

    @Test
    public void constructor_parameterConverters_null() throws Exception {
        try {
            new ParamMethod(unwrapper, null);
            fail("Must throw IllegalArgumentException.");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), containsString("parameterConverters must not be null."));
        }
    }

    @Test
    public void exec_arguments_empty() throws Exception {
        try {
            processTemplate("${param()}");
            fail("Must throw IllegalArgumentException when arguments are empty.");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), is("Query parameter required."));
        }
    }

    @Test
    public void exec_query_param_null() throws Exception {
        String result = processTemplate("${param(someNotExistValue)}");
        assertThat(result, is("?"));
        List<Object> parameters = paramMethod.getParameters();
        assertThat(parameters.size(), is(1));
        assertThat(parameters.get(0), nullValue());
    }

    @Test
    public void exec_query_param_exists() throws Exception {
        dataModel.put("user", new User("World", 2001, EmployeeType.FULLTIME));
        String result = processTemplate("${param(user.name)}/${param(user.birthyear)}/${param(user.employeeType)}");

        assertThat(result, is("?/?/?"));

        List<Object> parameters = paramMethod.getParameters();
        assertThat(parameters.size(), is(3));
        assertThat(parameters.get(0), CoreMatchers.<Object>is("World"));
        assertThat(parameters.get(1), CoreMatchers.<Object>is(2001));
        assertThat(parameters.get(2), CoreMatchers.<Object>is(EmployeeType.FULLTIME));
    }

    @Test
    public void exec_query_param_exists_and_null() throws Exception {
        dataModel.put("user", new User("John", 2002, EmployeeType.PARTTIME));
        String result = processTemplate("${param(user.name)}|${param(user.somenotexistfield)}|${param(user.birthyear)}");

        assertThat(result, is("?|?|?"));

        List<Object> parameters = paramMethod.getParameters();
        assertThat(parameters.size(),is(3));
        assertThat(parameters.get(0), CoreMatchers.<Object>is("John"));
        assertThat(parameters.get(1), nullValue());
        assertThat(parameters.get(2), CoreMatchers.<Object>is(2002));
    }

    @Test
    public void exec_parameterConverterName_null() throws Exception {
        dataModel.put("converter", null);
        try {
            String result = processTemplate("${param(123, converter)}");
            fail("When converter is null, must throw an IllegalArgumentException.");
        } catch (IllegalArgumentException ex) {
            log.error(ex.getMessage());
            assertThat(ex.getMessage(), is("parameterConverterName must not be null."));
        }
    }

    @Test
    public void exec_parameterConverterName_not_string() throws Exception {
        dataModel.put("converter", 123);
        try {
            String result = processTemplate("${param(123, converter)}");
            fail("When converter is not String, must throw an ClassCastException.");
        } catch (ClassCastException ex) {
            log.error(ex.getMessage(), ex);
            assertThat(ex.getMessage(), is("freemarker.template.SimpleNumber cannot be cast to freemarker.template.SimpleScalar"));
        }
    }

    @Test
    public void exec_parameterConverter_not_exist() throws Exception {
        try {
            String result = processTemplate("${param(123, 'notExistConverter')}");
            fail("When converter does not exist, must throw an IllegalArgumentException.");
        } catch (IllegalArgumentException ex) {
            log.error(ex.getMessage(), ex);
            assertThat(ex.getMessage(), is("parameterConverter - notExistConverter does not exist."));
        }
    }

    @Test
    public void exec_parameterConverter() throws Exception {
        Date date = new Date();
        dataModel.put("available", true);
        dataModel.put("date", date);

        Time time = new Time(date.getTime());

        when(booleanToYn.convert(true)).thenReturn("Y");
        when(dateToTime.convert(date)).thenReturn(time);

        String result = processTemplate("${param(available, 'booleanToYn')},${param(date,'dateToTime')}");
        assertThat(result, is("?,?"));

        List<Object> parameters = paramMethod.getParameters();
        assertThat(parameters.size(), is(2));
        assertThat(parameters.get(0), CoreMatchers.<Object>is("Y"));
        assertThat(parameters.get(1), CoreMatchers.<Object>is(time));
    }

    @Test
    public void exec_more_than_2_arguments() throws Exception {
        try {
            processTemplate("${param(1,'converter',3)}");
            fail("paramMethod's argument must be 1 or 2 but 3.");
        } catch(IllegalArgumentException ex) {
            log.error(ex.getMessage(), ex);
            assertThat(ex.getMessage(), is("paramMethod's argument must be 1 or 2 but 3."));
        }
    }
}