package kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class DateToSqlDateParameterConverterTest {
    private Logger log = LoggerFactory.getLogger(DateToSqlDateParameterConverterTest.class);

    private DateToSqlDateParameterConverter dateToSqlDateParameterConverter;

    @Before
    public void setUp() throws Exception {
        dateToSqlDateParameterConverter = new DateToSqlDateParameterConverter();
    }

    @Test
    public void convert_null() throws Exception {
        assertThat(dateToSqlDateParameterConverter.convert(null), nullValue());
    }

    @Test
    public void convert() throws Exception {
        Date date = new Date();
        Object sqlDate = dateToSqlDateParameterConverter.convert(date);
        log.debug("Date to java.sql.Date : {} -> {}", date, sqlDate);

        assertThat(sqlDate, instanceOf(java.sql.Date.class));
        assertThat(sqlDate, CoreMatchers.<Object>is(new java.sql.Date(date.getTime())));
    }
}