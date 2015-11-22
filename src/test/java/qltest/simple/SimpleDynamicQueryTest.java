package qltest.simple;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.DynamicQuery;
import org.junit.Test;
import org.slf4j.Logger;
import qltest.AbstractFreemarkerDynamicQlBuilderTest;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Simple Query Build Test
 */
public class SimpleDynamicQueryTest extends AbstractFreemarkerDynamicQlBuilderTest {
    private Logger log = getLogger(SimpleDynamicQueryTest.class);

    @Test
    public void only_text() throws Exception {
        DynamicQuery dynamicQuery = processTemplate("simple/only_text");

        assertThat(dynamicQuery.getQueryString(), is("SELECT 1 FROM DUAL"));
        assertThat(dynamicQuery.getQueryParameters().size(), is(0));
    }

    @Test
    public void with_params() throws Exception {
        dataModel().put("luckyNumber", 21);
        dataModel().put("name", "FreemarkerDynamicQlBuilder");

        DynamicQuery dynamicQuery = processTemplate("simple/with_params");

        assertThat(dynamicQuery.getQueryString(), is("SELECT * FROM somewhere WHERE column1 = ? AND column2 = ?"));
        assertThat(dynamicQuery.getQueryParameters().size(), is(2));
        assertThat(dynamicQuery.getQueryParameters(), hasItems((Object) 21, "FreemarkerDynamicQlBuilder"));
    }
}
