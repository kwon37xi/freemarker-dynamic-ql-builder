package kr.pe.kwonnam.fdqlbuilder.examples;

import freemarker.template.*;
import kr.pe.kwonnam.fdqlbuilder.methods.ParamMethod;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Dynamic QL Examples
 */
public class DynamicQlExamples {

    private Logger log = getLogger(DynamicQlExamples.class);

    private Configuration cfg;

    private ParamMethod paramMethod;

    @Before
    public void setUp() throws Exception {
        cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setClassForTemplateLoading(getClass(), "");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setNumberFormat("0.######");  // now it will print 1000000

        paramMethod = new ParamMethod((ObjectWrapperAndUnwrapper) cfg.getObjectWrapper(), null);
    }

    private String processTmplate(String templateName, Map<String,Object> root) throws IOException, TemplateException {
        Template template = cfg.getTemplate(templateName);
        StringWriter out = new StringWriter();
        Map<String, Object> dataModel = new HashMap<String, Object>();
        if (root != null) {
            dataModel.putAll(root);
        }
        dataModel.put("param", paramMethod);
        template.process(dataModel, out);
        return out.toString();
    }
    @Test
    public void only_text() throws Exception {
        String result = processTmplate("only_text.ftl", null);
        log.debug("Only text : {}", result);
        assertThat(result).isEqualTo("SELECT 1 FROM DUAL");
    }

    @Test
    public void with_params() throws Exception {
        Map<String,Object> root = new HashMap<String, Object>();
        root.put("luckyNumber", 777);
        root.put("myname", "KwonNam");

        String result = processTmplate("with_params.ftl", root);
        assertThat(result).isEqualTo("SELECT * FROM somewhere WHERE column1 = ? AND column2 = ?");

        List<Object> parameters = paramMethod.getParameters();
        assertThat(parameters).hasSize(2).contains(777, "KwonNam");
    }

    @Test
    public void with_not_scalar_model() throws Exception {
        Map<String,Object> root = new HashMap<String, Object>();
        User user = new User();
        user.setName("KwonNam");
        user.setBirthyear(1977);
        root.put("me", user);

        String result = processTmplate("with_not_scalar_model.ftl", root);
        List<Object> parameters = paramMethod.getParameters();

        assertThat(result).isEqualTo("SELECT * FROM somewhere WHERE column1 = ?");
        assertThat(parameters).hasSize(1).containsExactly(user);
    }



    public static class User {
        private String name;
        private int birthyear;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getBirthyear() {
            return birthyear;
        }

        public void setBirthyear(int birthyear) {
            this.birthyear = birthyear;
        }
    }
}
