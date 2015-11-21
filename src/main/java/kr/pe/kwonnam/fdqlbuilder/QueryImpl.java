package kr.pe.kwonnam.fdqlbuilder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getQueryString() {
        return queryString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object> getQueryParameters() {
        return queryParameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getQueryParameterArray() {
        return queryParameters.toArray(new Object[queryParameters.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindParameters(PreparedStatement preparedStatement) throws SQLException {
        if (preparedStatement == null) {
            throw new IllegalArgumentException("preparedStatement must not be null.");
        }

        for (int parameterIndex = 1; parameterIndex <= queryParameters.size(); parameterIndex++) {
            preparedStatement.setObject(parameterIndex, queryParameters.get(parameterIndex - 1));
        }
    }

    @Override
    public String toString() {
        return "QueryImpl{" +
                "queryString='" + queryString + '\'' +
                ", queryParameters=" + queryParameters +
                '}';
    }
}
