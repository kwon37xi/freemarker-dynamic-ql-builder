package qltest.directives;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.DynamicQuery;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.EmployeeType;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.User;
import org.junit.Test;
import qltest.AbstractFreemarkerDynamicQlBuilderTest;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class WhereDirectiveDynamicQlBuilderTest extends AbstractFreemarkerDynamicQlBuilderTest {

    @Test
    public void whereDirective_no_conditions() throws Exception {
        dataModel().put("user", new User());
        dataModel().put("userIds", null);

        DynamicQuery dynamicQuery = processTemplate("directives/where");

        String queryString = dynamicQuery.getQueryString();
        assertThat(queryString, not(containsString("WHERE")));
        assertThat(queryString, not(containsString("name")));
        assertThat(queryString, not(containsString("birthyear")));
        assertThat(queryString, not(containsString("employeeType")));

        assertThat(dynamicQuery.getQueryParameters(), empty());
    }

    @Test
    public void whereDirective_with_conditions() throws Exception {
        User user = new User();
        user.setName(""); // empty on purpose
        user.setBirthyear(2015);
        user.setEmployeeType(EmployeeType.FULLTIME);

        dataModel().put("user", user);
        dataModel().put("userIds", new int[]{100, 200, 300});

        DynamicQuery dynamicQuery = processTemplate("directives/where");

        String queryString = dynamicQuery.getQueryString();
        assertThat(queryString, containsString("WHERE birthyear = ?"));
        assertThat(queryString, containsString("AND employeeType = ?"));
        assertThat(queryString, containsString("AND userId IN (?,?,?)"));

        assertThat(dynamicQuery.getQueryParameters(), hasItems((Object) 2015, "FULLTIME", 100, 200, 300));
    }
}
