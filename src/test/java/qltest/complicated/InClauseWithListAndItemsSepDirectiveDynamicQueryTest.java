package qltest.complicated;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.DynamicQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import qltest.AbstractFreemarkerDynamicQlBuilderTest;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class InClauseWithListAndItemsSepDirectiveDynamicQueryTest extends AbstractFreemarkerDynamicQlBuilderTest {

    private boolean withPositionalIndex;

    public InClauseWithListAndItemsSepDirectiveDynamicQueryTest(boolean withPositionalIndex) {
        this.withPositionalIndex = withPositionalIndex;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testParameters() {
        return Arrays.asList(new Object[][]{{Boolean.FALSE}, {Boolean.TRUE}});
    }

    @Test
    public void in_cluase() throws Exception {
        dataModel().put("userIds", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});

        DynamicQuery dynamicQuery = processTemplate("complicated/in_clause_with_list", withPositionalIndex);

        String queryString = dynamicQuery.getQueryString();
        if (withPositionalIndex) {
            assertThat(queryString, containsString("user_id in (?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12)"));
        } else {
            assertThat(queryString, containsString("user_id in (?,?,?,?,?,?,?,?,?,?,?,?)"));
        }
        assertThat(dynamicQuery.getQueryParameters().size(), is(12));
        assertThat(dynamicQuery.getQueryParameters(), hasItems((Object) 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
    }

    @Test
    public void in_cluase_empty_userIds() throws Exception {
        dataModel().put("userIds", null);

        DynamicQuery dynamicQuery = processTemplate("complicated/in_clause_with_list", withPositionalIndex);

        String queryString = dynamicQuery.getQueryString();
        assertThat(queryString, containsString("user_id IS NOT NULL"));
        assertThat(queryString, not(containsString("user_id in (")));
        assertThat(queryString, not(containsString("?")));
        assertThat(queryString, not(containsString(")")));
        assertThat(dynamicQuery.getQueryParameters().size(), is(0));
    }
}
