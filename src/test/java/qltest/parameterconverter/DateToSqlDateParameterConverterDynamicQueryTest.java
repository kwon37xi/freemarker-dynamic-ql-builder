package qltest.parameterconverter;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.DynamicQuery;
import org.junit.Before;
import org.junit.Test;
import qltest.AbstractFreemarkerDynamicQlBuilderTest;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class DateToSqlDateParameterConverterDynamicQueryTest extends AbstractFreemarkerDynamicQlBuilderTest {

    @Test
    public void date_to_sqldate_converter() throws Exception {
        Date now = new Date();
        dataModel().put("nullValue", null);
        dataModel().put("now", now);

        DynamicQuery dynamicQuery = processTemplate("parameterconverter/date_to_sqldate");
        List<Object> queryParameters = dynamicQuery.getQueryParameters();
        assertThat(queryParameters.size(), is(2));
        assertThat(queryParameters.get(0), is((Object) null));
        assertThat(queryParameters.get(1), is((Object) new java.sql.Date(now.getTime())));
    }
}
