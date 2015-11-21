package kr.pe.kwonnam.freemarkerdynamicqlbuilder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * The result of processing query template.
 */
public interface Query {
    /**
     * final query string
     */
    String getQueryString();

    /**
     * Query binding parameters as {@link List}
     */
    List<Object> getQueryParameters();

    /**
     * Query binding parameters as Object array
     */
    Object[] getQueryParameterArray();

    /**
     * bind query parameters to {@link PreparedStatement}.
     * @param preparedStatement must not be null.
     * @throws SQLException
     */
    void bindParameters(PreparedStatement preparedStatement) throws SQLException;
}
