package kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.*;
import org.junit.Before;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class AbstractQlDirectiveTest {
    private Configuration freemarkerConfiguration;

    private StringTemplateLoader templateLoader;

    private Map<String, Object> dataModel;

    @Before
    public void setUp() throws Exception {
        freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_23);

        templateLoader = new StringTemplateLoader();

        freemarkerConfiguration.setTemplateLoader(templateLoader);
        freemarkerConfiguration.setDefaultEncoding("UTF-8");
        freemarkerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarkerConfiguration.setNumberFormat("0.######");

        Map<String, TemplateDirectiveModel> qlDirectives = new HashMap<String, TemplateDirectiveModel>();
        qlDirectives.put("trim", new TrimDirective());

        freemarkerConfiguration.setSharedVariable("ql", qlDirectives);

        dataModel = new HashMap<String, Object>();
    }

    protected String processTemplate(String templateSource) throws IOException, TemplateException {
        Template template = new Template("template", templateSource, freemarkerConfiguration);
        StringWriter out = new StringWriter();

        template.process(dataModel, out);

        return out.toString();
    }

}
