package kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.EmployeeType;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class EnumToNameParameterConverterTest {
    private Logger log = LoggerFactory.getLogger(EnumToNameParameterConverterTest.class);

    private EnumToNameParameterConverter enumToNameParameterConverter;

    @Before
    public void setUp() throws Exception {
        enumToNameParameterConverter = new EnumToNameParameterConverter();
    }

    @Test
    public void convert_null() throws Exception {
        assertThat(enumToNameParameterConverter.convert(null), nullValue());
    }

    @Test
    public void convert() throws Exception {
        assertThat(enumToNameParameterConverter.convert(EmployeeType.FULLTIME), CoreMatchers.<Object>is("FULLTIME"));
        assertThat(enumToNameParameterConverter.convert(EmployeeType.PARTTIME), CoreMatchers.<Object>is("PARTTIME"));
    }
}