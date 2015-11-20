package kr.pe.kwonnam.fdqlbuilder;

import java.util.List;

/**
 * The result of processing query template.
 */
public interface Query {
    /** final query string */
    String getQueryString();

    /** Query binding parameters as {@link List} */
    List<Object> getQueryParameters();

    /** Query binding parameters as Object array */
    Object[] getQueryParameterArray();
}
