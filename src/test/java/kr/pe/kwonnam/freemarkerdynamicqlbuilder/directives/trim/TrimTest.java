package kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives.trim;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TrimTest {
    private Logger log = LoggerFactory.getLogger(TrimTest.class);

    private Trim trim;

    @Before
    public void setUp() throws Exception {
        trim = new Trim();
    }

    public List<String> list(String... str) {
        return Arrays.asList(str);
    }

    @Test
    public void trim_null() throws Exception {
        assertThat(trim.trim(null), nullValue());
    }

    @Test
    public void trim_to_empty() throws Exception {
        assertThat(trim.trim(""), is(""));
        assertThat(trim.trim("  \t \n \r  "), is(""));
    }

    @Test
    public void trim_no_options() throws Exception {
        assertThat(trim.trim("  \r \t \n hello \r \t \n world \t \r \n  "), is("hello \r \t \n world"));
        assertThat(trim.trim("  \r \n AND username = ? AND email = ?  \r \n  \t  "), is("AND username = ? AND email = ?"));
    }

    @Test
    public void trim_all_options_empty() throws Exception {
        trim.setPrefix("");
        trim.setSuffix("");
        trim.setPrefixOverrides(new ArrayList<String>());
        trim.setSuffixOverrides(new ArrayList<String>());

        assertThat(trim.trim("  \r \t \n hello \r \t \n world \t \r \n  "), is("hello \r \t \n world"));
        assertThat(trim.trim("  \r \n AND username = ? AND email = ?  \r \n  \t  "), is("AND username = ? AND email = ?"));
    }

    @Test
    public void trim_prefix_trimmed_empfy() throws Exception {
        trim.setPrefix("WHERE ");
        assertThat(trim.trim(" \r \t \n"), is(""));
    }

    @Test
    public void trim_prefix_trimmed_not_empty() throws Exception {
        trim.setPrefix("WHERE ");
        assertThat(trim.trim(" \r \t username = ? \n"), is("WHERE username = ?"));

        trim.setPrefix("[");
        assertThat(trim.trim(" \r \t username = ? \n"), is("[username = ?"));
    }

    @Test
    public void trim_prefixOverrides_nothing_to_override() throws Exception {
        trim.setPrefixOverrides(list("and ", "or "));
        assertThat(trim.trim(" something = ?  \n"), is("something = ?"));
    }

    @Test
    public void trim_prefixOverrides_first_item() throws Exception {
        trim.setPrefixOverrides(list("and ", "or "));
        assertThat(trim.trim("  and or etc... "), is("or etc..."));
    }

    @Test
    public void trim_prefixOverrides_last_item() throws Exception {
        trim.setPrefixOverrides(list("AND ", "ELSE ", "OR "));
        assertThat(trim.trim("\t OR something=? AND somewhere=? \n"), is("something=? AND somewhere=?"));
    }

    @Test
    public void trim_prfix_and_prefixOverrides_same() throws Exception {
        trim.setPrefix("WHERE ");
        trim.setPrefixOverrides(list("WHERE "));

        assertThat(trim.trim("\t something=? AND somewhere=? \n"), is("WHERE something=? AND somewhere=?"));
    }

    @Test
    public void trim_suffix_trimmed_empty() throws Exception {
        trim.setSuffix(" END");
        assertThat(trim.trim("\t \r \n "), is(""));
    }

    @Test
    public void trim_suffix_trimmed_not_empty() throws Exception {
        trim.setSuffix(" END");
        assertThat(trim.trim("  \t a=?, b=? \n "), is("a=?, b=? END"));

        trim.setSuffix("]");
        assertThat(trim.trim("  \t a=?, b=? \n "), is("a=?, b=?]"));
    }

    @Test
    public void trim_suffixOverrides_nothing_to_override() throws Exception {
        trim.setSuffixOverrides(list(",", "|"));
        assertThat(trim.trim(" \t a=?, b=c  "), is("a=?, b=c"));
    }

    @Test
    public void trim_suffixOverides_first_item() throws Exception {
        trim.setSuffixOverrides(list(",", "|"));
        assertThat(trim.trim("  a=?, b=c, \t\r\n"), is("a=?, b=c"));
    }

    @Test
    public void trim_suffixOverrides_last_item() throws Exception {
        trim.setSuffixOverrides(list(",", "===", "|"));
        assertThat(trim.trim(" \t a=? | b=c | d=?,|"), is("a=? | b=c | d=?,"));
    }

    @Test
    public void trim_suffix_and_suffixOverrides_same() throws Exception {
        trim.setSuffix("]");
        trim.setSuffixOverrides(list("]"));

        assertThat(trim.trim("[hello world"), is("[hello world]"));
    }

    @Test
    public void trim_all_options() throws Exception {
        trim.setPrefix("[ ");
        trim.setPrefixOverrides(list("AND ", "OR "));
        trim.setSuffix(" ]");
        trim.setSuffixOverrides(list(",", "|"));

        assertThat(trim.trim("  \t AND hello=world,  \r\n "), is("[ hello=world ]"));
        assertThat(trim.trim("  \t OR hello=world|  \r\n "), is("[ hello=world ]"));
    }
}