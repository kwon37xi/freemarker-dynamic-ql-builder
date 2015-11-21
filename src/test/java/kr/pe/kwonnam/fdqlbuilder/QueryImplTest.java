package kr.pe.kwonnam.fdqlbuilder;

import org.hamcrest.CoreMatchers;
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
public class QueryImplTest {

    @Mock
    private PreparedStatement preparedStatement;

    @Test
    public void constructor_no_nulls() throws Exception {
        Date date = new Date();

        List<Object> queryParameters = new ArrayList<Object>();
        queryParameters.add("hello");
        queryParameters.add(123);
        queryParameters.add(date);

        QueryImpl query = new QueryImpl("SELECT 1 FROM DUAL", queryParameters);
        assertThat(query.getQueryString(), is("SELECT 1 FROM DUAL"));
        assertThat(query.getQueryParameters().size(), is(3));
        assertThat(query.getQueryParameters(), sameInstance(queryParameters));

        Object[] queryParameterArray = query.getQueryParameterArray();
        assertThat(queryParameterArray.length, is(3));
        assertThat(queryParameterArray[0], is(queryParameters.get(0)));
        assertThat(queryParameterArray[1], is(queryParameters.get(1)));
        assertThat(queryParameterArray[2], is(queryParameters.get(2)));
    }

    @Test
    public void constructor_query_parameters_null() throws Exception {
        QueryImpl query = new QueryImpl("INSERT INTO...", null);

        assertThat(query.getQueryParameters().size(), is(0));
        assertThat(query.getQueryParameterArray().length, is(0));
    }

    @Test
    public void bindParameters_preparedStatement_null() throws Exception {
        QueryImpl query = new QueryImpl("INSERT INTO...", Arrays.asList(new Object[]{1, 2, 3, 4, 5, "6", "7"}));
        try {
            query.bindParameters(null);
            fail("Must throw an exception - IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat("Must throw an exception",
                    ex.getMessage(), is("preparedStatement must not be null."));
        }
    }

    @Test
    public void bindParameters() throws Exception {
        QueryImpl query = new QueryImpl("INSERT INTO...", Arrays.asList(new Object[]{1000, 2000, "3000", "4000"}));

        query.bindParameters(preparedStatement);

        verify(preparedStatement, times(4)).setObject(anyInt(), anyObject());

        verify(preparedStatement, times(1)).setObject(1, 1000);
        verify(preparedStatement, times(1)).setObject(2, 2000);
        verify(preparedStatement, times(1)).setObject(3, "3000");
        verify(preparedStatement, times(1)).setObject(4, "4000");

    }
}