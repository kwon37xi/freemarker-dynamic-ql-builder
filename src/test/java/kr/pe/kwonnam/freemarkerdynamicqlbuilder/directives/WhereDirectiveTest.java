package kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class WhereDirectiveTest extends AbstractQlDirectiveTest {

    @Test
    public void where() throws Exception {
        assertThat(processTemplate("<@ql.where/>"), is(""));
        assertThat(processTemplate("<@ql.where>   \r \n \t </@ql.where>"), is(""));
    }

    @Test
    public void where_prefix_only() throws Exception {
        assertThat(processTemplate("<@ql.where>  \r \t \n  hello world \r \t \n </@ql.where>"), is("WHERE hello world"));
    }

    @Test
    public void where_with_prefix_overrides() throws Exception {
        assertThat(processTemplate("<@ql.where>   AND hello world \n </@ql.where>"), is("WHERE hello world"));
        assertThat(processTemplate("<@ql.where>   and hello world \n </@ql.where>"), is("WHERE hello world"));
        assertThat(processTemplate("<@ql.where>   OR hello world \n </@ql.where>"), is("WHERE hello world"));
        assertThat(processTemplate("<@ql.where>   or hello world \n </@ql.where>"), is("WHERE hello world"));

        assertThat(processTemplate("<@ql.where>   and or hello world \n </@ql.where>"), is("WHERE or hello world"));
    }
}
