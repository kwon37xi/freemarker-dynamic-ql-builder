package kr.pe.kwonnam.freemarkerdynamicqlbuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DynamicQueryImplTest {

    @Mock
    private PreparedStatement preparedStatement;

    @Test
    public void constructor_no_nulls() throws Exception {
        Date date = new Date();

        List<Object> queryParameters = new ArrayList<Object>();
        queryParameters.add("hello");
        queryParameters.add(123);
        queryParameters.add(date);

        DynamicQueryImpl dynamicQuery = new DynamicQueryImpl("SELECT 1 FROM DUAL", queryParameters);
        assertThat(dynamicQuery.getQueryString(), is("SELECT 1 FROM DUAL"));
        assertThat(dynamicQuery.getQueryParameters().size(), is(3));
        assertThat(dynamicQuery.getQueryParameters(), sameInstance(queryParameters));

        Object[] queryParameterArray = dynamicQuery.getQueryParameterArray();
        assertThat(queryParameterArray.length, is(3));
        assertThat(queryParameterArray[0], is(queryParameters.get(0)));
        assertThat(queryParameterArray[1], is(queryParameters.get(1)));
        assertThat(queryParameterArray[2], is(queryParameters.get(2)));
    }

    @Test
    public void constructor_query_parameters_null() throws Exception {
        DynamicQueryImpl dynamicQuery = new DynamicQueryImpl("INSERT INTO...", null);

        assertThat(dynamicQuery.getQueryParameters().size(), is(0));
        assertThat(dynamicQuery.getQueryParameterArray().length, is(0));
    }

    @Test
    public void bindParameters_preparedStatement_null() throws Exception {
        DynamicQueryImpl dynamicQuery = new DynamicQueryImpl("INSERT INTO...", Arrays.asList(new Object[]{1, 2, 3, 4, 5, "6", "7"}));
        try {
            dynamicQuery.bindParameters(null);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), is("preparedStatement must not be null."));
        }
    }

    @Test
    public void bindParameters() throws Exception {
        DynamicQueryImpl dynamicQuery = new DynamicQueryImpl("INSERT INTO...", Arrays.asList(new Object[]{1000, 2000, "3000", "4000"}));

        dynamicQuery.bindParameters(preparedStatement);

        verify(preparedStatement, times(4)).setObject(anyInt(), anyObject());

        verify(preparedStatement, times(1)).setObject(1, 1000);
        verify(preparedStatement, times(1)).setObject(2, 2000);
        verify(preparedStatement, times(1)).setObject(3, "3000");
        verify(preparedStatement, times(1)).setObject(4, "4000");

    }
}