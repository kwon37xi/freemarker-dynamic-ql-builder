package kr.pe.kwonnam.fdqlbuilder;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class QueryImplTest {

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
}