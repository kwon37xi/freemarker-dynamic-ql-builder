package qltest.simple;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.Query;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.slf4j.Logger;
import qltest.AbstractFreemarkerDynamicQlBuilderTest;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Simple Query Build Test
 */
public class SimpleQueryTest extends AbstractFreemarkerDynamicQlBuilderTest {
    private Logger log = getLogger(SimpleQueryTest.class);

    @Test
    public void only_text() throws Exception {
        Query query = processTemplate("simple/only_text");

        assertThat(query.getQueryString(),is("SELECT 1 FROM DUAL"));
        assertThat(query.getQueryParameters().size(), is(0));
    }

    @Test
    public void with_params() throws Exception {
        dataModel().put("luckyNumber", 21);
        dataModel().put("name", "FreemarkerDynamicQlBuilder");

        Query query = processTemplate("simple/with_params");

        assertThat(query.getQueryString(), is("SELECT * FROM somewhere WHERE column1 = ? AND column2 = ?"));
        assertThat(query.getQueryParameters().size(), is(2));
        assertThat(query.getQueryParameters(), hasItems((Object)21, "FreemarkerDynamicQlBuilder"));
    }
}
