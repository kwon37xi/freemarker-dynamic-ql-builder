package kr.pe.kwonnam.fdqlbuilder;

import java.util.Map;

/**
 * Build Dynamic QL with Freemarker templates.
 *
 * This implementation must be thread safe.
 */
public interface FreemarkerDynamicQlBuilder {

    /**
     * build dynamic query.
     *
     * @param name Freemarker template name
     * @param dataModel data to prcoess
     * @return result of processing template
     */
    Query buildQuery(String name, Map<String, Object> dataModel);
}