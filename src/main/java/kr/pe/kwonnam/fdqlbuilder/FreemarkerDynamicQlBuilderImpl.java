package kr.pe.kwonnam.fdqlbuilder;

import freemarker.template.Configuration;

import java.util.Map;

/**
 * Default {@link FreemarkerDynamicQlBuilder} instance.
 * <br/>
 * Do not instantiate directlry. Use {@link FreemarkerDynamicQlBuilderFactory}.
 * <p/>
 * The isntance of this class is thread safe. But you MUST not change freemarkerConfigration object status(DO NOT call setter methods after the object configured).
 */
public class FreemarkerDynamicQlBuilderImpl implements FreemarkerDynamicQlBuilder {

    /** Freemarker configuration */
    private Configuration freemarkerConfiguration;

    /** Freemarker QL Custom Directive prefix */
    private String qlDirectivePrefix;

    /** Freemarker param custom method name */
    private String paramMethodName;

    @Override
    public Query buildQuery(String name, Map<String, Object> dataModel) {
        return null;
    }
}
