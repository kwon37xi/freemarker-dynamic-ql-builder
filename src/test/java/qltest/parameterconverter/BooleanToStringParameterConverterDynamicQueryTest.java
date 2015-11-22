
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

public class BooleanToStringParameterConverterDynamicQueryTest extends AbstractFreemarkerDynamicQlBuilderTest {
    private Logger log = getLogger(BooleanToStringParameterConverterDynamicQueryTest.class);

    @Before
    public void setUpDateModels() throws Exception {
        dataModel().put("nullValue", null);
        dataModel().put("booleanTrue", true);
        dataModel().put("booleanFalse", false);
//        dataModel().put("now", new Date());
//        dataModel().put("enumFulltime", EmployeeType.FULLTIME);
//        dataModel().put("enumParttime", EmployeeType.PARTTIME);
    }

    @Test
    public void boolean_to_string_converter() throws Exception {
        DynamicQuery dynamicQuery = processTemplate("parameterconverter/boolean_to_string");
        List<Object> queryParameters = dynamicQuery.getQueryParameters();
        assertThat(queryParameters.size(), is(6));
        assertThat(queryParameters.get(0), is((Object) null));
        assertThat(queryParameters.get(1), is((Object) "Y"));
        assertThat(queryParameters.get(2), is((Object) "N"));
        assertThat(queryParameters.get(3), is((Object) null));
        assertThat(queryParameters.get(4), is((Object) "T"));
        assertThat(queryParameters.get(5), is((Object) "F"));
    }
}