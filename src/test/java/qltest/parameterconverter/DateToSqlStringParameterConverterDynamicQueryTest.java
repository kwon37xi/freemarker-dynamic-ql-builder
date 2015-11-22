package qltest.parameterconverter;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.DynamicQuery;
import org.junit.Test;
import qltest.AbstractFreemarkerDynamicQlBuilderTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class DateToSqlStringParameterConverterDynamicQueryTest extends AbstractFreemarkerDynamicQlBuilderTest {

    @Test
    public void date_to_string_converter() throws Exception {
        Date now = new Date();
        dataModel().put("nullValue", null);
        dataModel().put("now", now);

        DynamicQuery dynamicQuery = processTemplate("parameterconverter/date_to_string");
        List<Object> queryParameters = dynamicQuery.getQueryParameters();
        assertThat(queryParameters.size(), is(2));
        assertThat(queryParameters.get(0), is((Object) null));
        assertThat(queryParameters.get(1), is((Object) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now)));
    }
}
