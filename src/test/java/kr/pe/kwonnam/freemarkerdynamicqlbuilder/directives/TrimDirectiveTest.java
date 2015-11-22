package kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TrimDirectiveTest extends AbstractQlDirectiveTest {
    @Test
    public void trim() throws Exception {
        assertThat(processTemplate("<@ql.trim/>"), is(""));
        assertThat(processTemplate("<@ql.trim>  \n \r \t \t\t \r\n </@ql.trim>"), is(""));
        assertThat(processTemplate("<@ql.trim>  \n \r hello world \t \t\t \r\n </@ql.trim>"), is("hello world"));
    }

    @Test
    public void trim_suffix_prefix_with_empty_body() throws Exception {
        assertThat(processTemplate("<@ql.trim prefix='[' suffix=']'/>"), is(""));
        assertThat(processTemplate("<@ql.trim prefix='[' suffix=']'></@ql.trim>"), is(""));
    }

    @Test
    public void trim_suffix_prefix_with_not_empty_body() throws Exception {
        assertThat(processTemplate("<@ql.trim prefix='[' suffix=']'> \t \r \n hello world \t \r \n \t</@ql.trim>"), is("[hello world]"));
        assertThat(processTemplate("<@ql.trim prefix='BEGIN ' suffix=' END'> \t \r \n hello world \t \r \n \t</@ql.trim>"), is("BEGIN hello world END"));
    }

    @Test
    public void trim_prefix_not_string() throws Exception {
        try {
            processTemplate("<@ql.trim prefix=123>hello</@ql.trim>");
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), CoreMatchers.is("prefix must be string."));
        }
    }

    @Test
    public void trim_suffix_not_string() throws Exception {
        try {
            processTemplate("<@ql.trim suffix=987>hello</@ql.trim>");
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), CoreMatchers.is("suffix must be string."));
        }
    }

    @Test
    public void trim_prefixOverrides_not_match() throws Exception {
        assertThat(processTemplate("<@ql.trim prefixOverrides=['AND ', 'OR ']></@ql.trim>"), is(""));
        assertThat(processTemplate("<@ql.trim prefixOverrides=['AND ', 'OR ']> hello world </@ql.trim>"), is("hello world"));
    }

    @Test
    public void trim_prefixOverrides_match() throws Exception {
        assertThat(processTemplate("<@ql.trim prefixOverrides=['AND ', 'OR ']> \r \t AND OR hello world </@ql.trim>"), is("OR hello world"));
        assertThat(processTemplate("<@ql.trim prefixOverrides=['AND ', 'OR ']> \r \t OR AND hello world </@ql.trim>"), is("AND hello world"));
    }

    @Test
    public void trim_prefixOverrides_wrong_parameter() throws Exception {
        try {
            processTemplate("<@ql.trim prefixOverrides='hello'>hello</@ql.trim>");
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), CoreMatchers.is("prefixOverrides must be sequence."));
        }
    }

    @Test
    public void trim_suffixOverrides_not_match() throws Exception {
        assertThat(processTemplate("<@ql.trim suffixOverrides=[',', '|']></@ql.trim>"), is(""));
        assertThat(processTemplate("<@ql.trim suffixOverrides=['|', ', ']> hello world </@ql.trim>"), is("hello world"));
    }

    @Test
    public void trim_suffixOverrides_match() throws Exception {
        assertThat(processTemplate("<@ql.trim suffixOverrides=[',', '|']> \r \t hello world| , </@ql.trim>"), is("hello world| "));
        assertThat(processTemplate("<@ql.trim suffixOverrides=[',', '|']> \r \t hello world, | </@ql.trim>"), is("hello world, "));
    }

    @Test
    public void trim_suffixOverrides_wrong_parameter() throws Exception {
        try {
            processTemplate("<@ql.trim suffixOverrides=12345>hello</@ql.trim>");
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), CoreMatchers.is("suffixOverrides must be sequence."));
        }
    }

    @Test
    public void trim_suffixOverrides_wrong_parameter_sequence_item() throws Exception {
        try {
            processTemplate("<@ql.trim suffixOverrides=[',', 123]>hello</@ql.trim>");
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), CoreMatchers.is("suffixOverrides's item must be string."));
        }
    }
}