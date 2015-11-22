package kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class BooleanToNumberParameterConverterTest {
    private Logger log = LoggerFactory.getLogger(BooleanToNumberParameterConverterTest.class);

    private BooleanToNumberParameterConverter booleanToNumberParameterConverter;

    @Before
    public void setUp() throws Exception {
        booleanToNumberParameterConverter = new BooleanToNumberParameterConverter(1111, 9999);
    }

    @Test
    public void convert_null() throws Exception {
        assertThat(booleanToNumberParameterConverter.convert(null), nullValue());
    }

    @Test
    public void convert_true() throws Exception {
        assertThat(booleanToNumberParameterConverter.convert(true), CoreMatchers.<Object>is(1111));
        assertThat(booleanToNumberParameterConverter.convert(Boolean.TRUE), CoreMatchers.<Object>is(1111));
    }

    @Test
    public void convert_false() throws Exception {
        assertThat(booleanToNumberParameterConverter.convert(false), CoreMatchers.<Object>is(9999));
        assertThat(booleanToNumberParameterConverter.convert(Boolean.FALSE), CoreMatchers.<Object>is(9999));
    }
}