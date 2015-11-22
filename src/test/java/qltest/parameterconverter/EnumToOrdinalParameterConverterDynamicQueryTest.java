
package qltest.parameterconverter;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.DynamicQuery;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.EmployeeType;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import qltest.AbstractFreemarkerDynamicQlBuilderTest;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class EnumToOrdinalParameterConverterDynamicQueryTest extends AbstractFreemarkerDynamicQlBuilderTest {
    private Logger log = getLogger(EnumToOrdinalParameterConverterDynamicQueryTest.class);

    @Before
    public void setUpDateModels() throws Exception {
        dataModel().put("nullValue", null);
        dataModel().put("enumFulltime", EmployeeType.FULLTIME);
        dataModel().put("enumParttime", EmployeeType.PARTTIME);
    }

    @Test
    public void boolean_to_string_converter() throws Exception {
        DynamicQuery dynamicQuery = processTemplate("parameterconverter/enum_to_ordinal");
        List<Object> queryParameters = dynamicQuery.getQueryParameters();

        assertThat(queryParameters.size(), is(3));
        assertThat(queryParameters.get(0), is((Object) null));
        assertThat(queryParameters.get(1), is((Object) 0));
        assertThat(queryParameters.get(2), is((Object) 1));
    }
}