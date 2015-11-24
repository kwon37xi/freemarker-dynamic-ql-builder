package qltest.complicated;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.DynamicQuery;
import org.junit.Test;
import qltest.AbstractFreemarkerDynamicQlBuilderTest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class InClauseWithListAndItemsSepDirectiveDynamicQueryTest extends AbstractFreemarkerDynamicQlBuilderTest {

    @Test
    public void in_cluase() throws Exception {
        dataModel().put("userIds", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        DynamicQuery dynamicQuery = processTemplate("complicated/in_clause_with_list");

        String queryString = dynamicQuery.getQueryString();
        assertThat(queryString, containsString("user_id in (?,?,?,?,?,?,?,?,?,?)"));
        assertThat(dynamicQuery.getQueryParameters().size(), is(10));
        assertThat(dynamicQuery.getQueryParameters(), hasItems((Object) 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    @Test
    public void in_cluase_empty_userIds() throws Exception {
        dataModel().put("userIds", null);

        DynamicQuery dynamicQuery = processTemplate("complicated/in_clause_with_list");

        String queryString = dynamicQuery.getQueryString();
        assertThat(queryString, containsString("user_id IS NOT NULL"));
        assertThat(queryString, not(containsString("user_id in (")));
        assertThat(queryString, not(containsString("?")));
        assertThat(queryString, not(containsString(")")));
        assertThat(dynamicQuery.getQueryParameters().size(), is(0));
    }
}
