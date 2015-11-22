package kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class DateToStringParameterConverterTest {
    public static final String DATE_FORMAT = "yyyyMMdd HH:mm:ss";
    private Logger log = LoggerFactory.getLogger(DateToStringParameterConverterTest.class);

    private DateToStringParameterConverter dateToStringParameterConverter;

    @Before
    public void setUp() throws Exception {
        dateToStringParameterConverter = new DateToStringParameterConverter(DATE_FORMAT);
    }

    @Test
    public void convert_null() throws Exception {
        assertThat(dateToStringParameterConverter.convert(null), nullValue());
    }

    @Test
    public void convert() throws Exception {
        final Date date = new Date();

        Object converted = dateToStringParameterConverter.convert(date);
        log.debug("Converted {} -> {}", date, converted);
        assertThat(converted, CoreMatchers.<Object>is(new SimpleDateFormat(DATE_FORMAT).format(date)));
    }
}