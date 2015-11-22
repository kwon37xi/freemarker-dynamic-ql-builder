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

public class BooleanToStringParameterConverterTest {
    private Logger log = LoggerFactory.getLogger(BooleanToStringParameterConverterTest.class);

    private BooleanToStringParameterConverter booleanToStringParameterConverter;

    @Before
    public void setUp() throws Exception {
        booleanToStringParameterConverter = new BooleanToStringParameterConverter("YES", "NO");
    }

    @Test
    public void convert_null() throws Exception {
        assertThat(booleanToStringParameterConverter.convert(null), nullValue());
    }

    @Test
    public void convert_true() throws Exception {
        assertThat(booleanToStringParameterConverter.convert(true), CoreMatchers.<Object>is("YES"));
        assertThat(booleanToStringParameterConverter.convert(Boolean.TRUE), CoreMatchers.<Object>is("YES"));
    }


    @Test
    public void convert_false() throws Exception {
        assertThat(booleanToStringParameterConverter.convert(false), CoreMatchers.<Object>is("NO"));
        assertThat(booleanToStringParameterConverter.convert(Boolean.FALSE), CoreMatchers.<Object>is("NO"));
    }
}