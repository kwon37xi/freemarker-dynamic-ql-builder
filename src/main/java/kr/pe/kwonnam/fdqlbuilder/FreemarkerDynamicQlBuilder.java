package kr.pe.kwonnam.fdqlbuilder;

import java.util.Map;

/**
 * Build Dynamic QL with Freemarker templates.
 *
 * Implementations of this interface must be thread safe.
 */
public interface FreemarkerDynamicQlBuilder {

    /**
     * build dynamic query.
     *
     * @param queryTemplateName Freemarker Query template name - must not be null or empty.
     * @param dataModel data to prcoess
     * @return result of processing template
     */
    Query buildQuery(String queryTemplateName, Map<String, Object> dataModel);

    /**
     * build dynamic query with empty dataModel;
     * @param queryTemplateName Freemarker Query template name - must not be null or empty.
     * @return result of processing template
     */
    Query buildQuery(String queryTemplateName);
}