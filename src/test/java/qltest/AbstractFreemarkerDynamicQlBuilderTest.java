package qltest;

import freemarker.template.Configuration;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.DynamicQuery;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.FreemarkerDynamicQlBuilder;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.FreemarkerDynamicQlBuilderFactory;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractFreemarkerDynamicQlBuilderTest {
    private Logger log = getLogger(AbstractFreemarkerDynamicQlBuilderTest.class);

    private static FreemarkerDynamicQlBuilder builder;

    private Map<String, Object> dataModel;

    protected FreemarkerDynamicQlBuilder getBuilder() {
        return builder;
    }

    protected Map<String, Object> dataModel() {
        return dataModel;
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_23);
        freemarkerConfiguration.setClassForTemplateLoading(AbstractFreemarkerDynamicQlBuilderTest.class, "/META-INF/qltest");
        freemarkerConfiguration.setDefaultEncoding("UTF-8");
        freemarkerConfiguration.setNumberFormat("0.######");

        builder = new FreemarkerDynamicQlBuilderFactory(freemarkerConfiguration)
                .addParameterConverter("booleanTo10", new BooleanToNumberParameterConverter(1, 0))
                .addParameterConverter("booleanToYN", new BooleanToStringParameterConverter("Y", "N"))
                .addParameterConverter("booleanToTF", new BooleanToStringParameterConverter("T", "F"))
                .addParameterConverter("dateToString", new DateToStringParameterConverter("yyyy-MM-dd HH:mm:ss"))
                .addParameterConverter("enumToName", new EnumToNameParameterConverter())
                .addParameterConverter("enumToOrdinal", new EnumToOrdirnalParameterConverter())
                .addParameterConverter("dateToSqlDate", new DateToSqlDateParameterConverter())
                .addParameterConverter("dateToSqlTime", new DateToSqlTimeParameterConverter())
                .getFreemarkerDynamicQlBuilder();
    }

    @Before
    public void setUp() throws Exception {
        dataModel = new HashMap<String, Object>();
    }

    protected DynamicQuery processTemplate(String queryTemplateName) {
        DynamicQuery dynamicQuery = builder.buildQuery(queryTemplateName, dataModel);
        log.info("QueryTemplate \"{}\" resuitl : {}", queryTemplateName, dynamicQuery);
        return dynamicQuery;
    }
}