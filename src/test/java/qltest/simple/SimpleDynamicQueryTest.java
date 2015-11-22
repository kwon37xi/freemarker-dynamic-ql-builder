package qltest.simple;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.DynamicQuery;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.User;
import org.junit.Test;
import org.slf4j.Logger;
import qltest.AbstractFreemarkerDynamicQlBuilderTest;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Simple Query Build Test
 */
public class SimpleDynamicQueryTest extends AbstractFreemarkerDynamicQlBuilderTest {
    private Logger log = getLogger(SimpleDynamicQueryTest.class);

    @Test
    public void text_and_number() throws Exception {
        dataModel().put("num", 123456789);

        DynamicQuery dynamicQuery = processTemplate("simple/text_and_number");

        assertThat(dynamicQuery.getQueryString(), is("SELECT 123456789 FROM DUAL"));
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

    @Test
    public void with_conditions_and_null_first() throws Exception {
        User user = new User();
        user.setName("Jane");
        dataModel().put("user", user);

        DynamicQuery dynamicQuery = processTemplate("simple/with_conditions_and_null");
        assertThat(dynamicQuery.getQueryString(), containsString("AND name=?"));
        assertThat(dynamicQuery.getQueryString(), not(containsString("AND birthyear=?")));
        assertThat(dynamicQuery.getQueryParameters().size(), is(2));
        assertThat(dynamicQuery.getQueryParameters(), hasItems((Object) "Jane", null));
    }

    @Test
    public void with_conditions_and_null_second() throws Exception {
        User user = new User();
        user.setBirthyear(2015);
        dataModel().put("user", user);

        DynamicQuery dynamicQuery = processTemplate("simple/with_conditions_and_null");
        assertThat(dynamicQuery.getQueryString(), containsString("AND birthyear=?"));
        assertThat(dynamicQuery.getQueryString(), not(containsString("AND name=?")));
        assertThat(dynamicQuery.getQueryParameters().size(), is(2));
        assertThat(dynamicQuery.getQueryParameters(), hasItems((Object) null, 2015));
    }
}
