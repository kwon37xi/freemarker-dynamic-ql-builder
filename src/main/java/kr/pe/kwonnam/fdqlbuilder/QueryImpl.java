package kr.pe.kwonnam.fdqlbuilder;

import java.util.ArrayList;
import java.util.List;

public class QueryImpl implements Query {
    private String queryString;
    private List<Object> queryParameters;

    public QueryImpl(String queryString, List<Object> queryParameters) {
        this.queryString = queryString;

        if (queryParameters == null) {
            this.queryParameters = new ArrayList<Object>();
        } else {
            this.queryParameters = queryParameters;
        }
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    @Override
    public List<Object> getQueryParameters() {
        return queryParameters;
    }

    @Override
    public Object[] getQueryParameterArray() {
        return queryParameters.toArray(new Object[queryParameters.size()]);
    }
}
