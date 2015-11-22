package kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SetDirectiveTest extends AbstractQlDirectiveTest {

    @Test
    public void set() throws Exception {
        assertThat(processTemplate("<@ql.set/>"), is(""));
        assertThat(processTemplate("<@ql.set>  \r \n \t </@ql.set>"), is(""));
    }

    @Test
    public void set_prefix_only() throws Exception {
        assertThat(processTemplate("<@ql.set>  \r \t \n hello world  </@ql.set>"), is("SET hello world"));
    }

    @Test
    public void set_with_prefix_overrides() throws Exception {
        assertThat(processTemplate("<@ql.set> a = b, c = ?,  \r \n \t</@ql.set>"), is("SET a = b, c = ?"));
    }
}
