package kr.pe.kwonnam.freemarkerdynamicqlbuilder;

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
     * @param withPositionalIndex if true, positional placeholder will be generated with index number like "?1", "?2", .., if false, just "?"
     * @return result of processing template
     */
    DynamicQuery buildQuery(String queryTemplateName, Map<String, Object> dataModel, boolean withPositionalIndex);

    /**
     * build dynamic query with <code>withPositionalIndex = false</code> option.
     * <br>
     *
     * @param queryTemplateName Freemarker Query template name - must not be null or empty.
     * @param dataModel         data to prcoess
     * @return result of processing template
     */
    DynamicQuery buildQuery(String queryTemplateName, Map<String, Object> dataModel);


    /**
     * build dynamic query with empty dataModel;
     * @param queryTemplateName Freemarker Query template name - must not be null or empty.
     * @param withPositionalIndex if true, positional placeholder will be generated with index number like "?1", "?2", .., if false, just "?"
     * @return result of processing template
     */
    DynamicQuery buildQuery(String queryTemplateName, boolean withPositionalIndex);

    /**
     * build dynamic query with empty dataModel and <code>withPositionalIndex = false</code> option.
     * @param queryTemplateName Freemarker Query template name - must not be null or empty.
     * @return result of processing template
     */
    DynamicQuery buildQuery(String queryTemplateName);
}