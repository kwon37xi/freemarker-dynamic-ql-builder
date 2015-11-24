package qltest.directives;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.DynamicQuery;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.EmployeeType;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.User;
import org.junit.Test;
import qltest.AbstractFreemarkerDynamicQlBuilderTest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class SetDirectiveDynamicQlBuilderTest extends AbstractFreemarkerDynamicQlBuilderTest {

    @Test
    public void setDirective_no_conditions() throws Exception {
        dataModel().put("user", new User());
        dataModel().put("userId", 1101);

        DynamicQuery dynamicQuery = processTemplate("directives/set");

        String queryString = dynamicQuery.getQueryString();
        assertThat(queryString, not(containsString("SET")));
        assertThat(queryString, not(containsString("name = ?")));
        assertThat(queryString, not(containsString("birthyear = ?")));
        assertThat(queryString, not(containsString("employeeType = ?")));

        assertThat(dynamicQuery.getQueryParameters().size(), is(1));
        assertThat(dynamicQuery.getQueryParameters(), hasItem(1101));

    }

    @Test
    public void setDirective_with_conditions() throws Exception {
        User user = new User();
        user.setName("freemarker-ql");
        user.setBirthyear(2015);

        dataModel().put("user", user);
        dataModel().put("userId", 9876);

        DynamicQuery dynamicQuery = processTemplate("directives/set");

        String queryString = dynamicQuery.getQueryString();
        assertThat(queryString, containsString("SET "));
        assertThat(queryString, containsString("name = ?,"));
        assertThat(queryString, containsString("birthyear = ?"));
        assertThat(queryString, not(containsString("birthyear = ?,")));

        assertThat(dynamicQuery.getQueryParameters().size(), is(3));
        assertThat(dynamicQuery.getQueryParameters(), hasItems((Object) "freemarker-ql", 2015, 9876));
    }
}
