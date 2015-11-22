package kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.EmployeeType;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class EnumToOrdirnalParameterConverterTest {
    private Logger log = LoggerFactory.getLogger(EnumToOrdirnalParameterConverterTest.class);

    private EnumToOrdirnalParameterConverter enumToOrdirnalParameterConverter;

    @Before
    public void setUp() throws Exception {
        enumToOrdirnalParameterConverter = new EnumToOrdirnalParameterConverter();
    }

    @Test
    public void convert_null() throws Exception {
        assertThat(enumToOrdirnalParameterConverter.convert(null), nullValue());
    }

    @Test
    public void convert() throws Exception {
        assertThat(enumToOrdirnalParameterConverter.convert(EmployeeType.FULLTIME), CoreMatchers.<Object>is(0));
        assertThat(enumToOrdirnalParameterConverter.convert(EmployeeType.PARTTIME), CoreMatchers.<Object>is(1));

    }
}