package kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.EmployeeType;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class EnumToOrdinalParameterConverterTest {
    private Logger log = LoggerFactory.getLogger(EnumToOrdinalParameterConverterTest.class);

    private EnumToOrdinalParameterConverter enumToOrdinalParameterConverter;

    @Before
    public void setUp() throws Exception {
        enumToOrdinalParameterConverter = new EnumToOrdinalParameterConverter();
    }

    @Test
    public void convert_null() throws Exception {
        assertThat(enumToOrdinalParameterConverter.convert(null), nullValue());
    }

    @Test
    public void convert() throws Exception {
        assertThat(enumToOrdinalParameterConverter.convert(EmployeeType.FULLTIME), CoreMatchers.<Object>is(0));
        assertThat(enumToOrdinalParameterConverter.convert(EmployeeType.PARTTIME), CoreMatchers.<Object>is(1));

    }
}