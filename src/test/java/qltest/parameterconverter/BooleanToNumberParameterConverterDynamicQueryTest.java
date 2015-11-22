package qltest.parameterconverter;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.DynamicQuery;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import qltest.AbstractFreemarkerDynamicQlBuilderTest;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class BooleanToNumberParameterConverterDynamicQueryTest extends AbstractFreemarkerDynamicQlBuilderTest {

    private Logger log = getLogger(BooleanToNumberParameterConverterDynamicQueryTest.class);

    @Before
    public void setUpDateModels() throws Exception {
        dataModel().put("nullValue", null);
        dataModel().put("booleanTrue", true);
        dataModel().put("booleanFalse", false);
    }

    @Test
    public void boolean_to_number_converter() throws Exception {
        DynamicQuery dynamicQuery = processTemplate("parameterconverter/boolean_to_number");
        List<Object> queryParameters = dynamicQuery.getQueryParameters();
        assertThat(queryParameters.size(), is(3));
        assertThat(queryParameters.get(0), is((Object) null));
        assertThat(queryParameters.get(1), is((Object) 1));
        assertThat(queryParameters.get(2), is((Object) 0));
    }
}
