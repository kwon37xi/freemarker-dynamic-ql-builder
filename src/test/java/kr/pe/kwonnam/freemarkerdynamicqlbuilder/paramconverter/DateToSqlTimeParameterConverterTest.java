package kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class DateToSqlTimeParameterConverterTest {
    private Logger log = LoggerFactory.getLogger(DateToSqlTimeParameterConverterTest.class);

    private DateToSqlTimeParameterConverter dateToSqlTimeParameterConverter;

    @Before
    public void setUp() throws Exception {
        dateToSqlTimeParameterConverter = new DateToSqlTimeParameterConverter();
    }

    @Test
    public void convert_null() throws Exception {
        assertThat(dateToSqlTimeParameterConverter.convert(null), nullValue());
    }

    @Test
    public void convert() throws Exception {
        Date date = new Date();

        Object sqlTime = dateToSqlTimeParameterConverter.convert(date);
        log.debug("Date to java.sql.Time : {} -> {}", date, sqlTime);

        assertThat(sqlTime, CoreMatchers.<Object>is(new Time(date.getTime())));
    }
}