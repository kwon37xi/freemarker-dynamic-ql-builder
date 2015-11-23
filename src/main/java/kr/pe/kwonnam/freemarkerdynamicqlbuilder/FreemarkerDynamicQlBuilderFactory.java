package kr.pe.kwonnam.freemarkerdynamicqlbuilder;

import freemarker.template.Configuration;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateModelException;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives.SetDirective;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives.TrimDirective;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives.WhereDirective;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.objectunwrapper.TemplateModelObjectUnwrapper;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.objectunwrapper.TemplateModelObjectUnwrapperDefaultImpl;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter.ParameterConverter;

import java.util.*;

/**
 * Factory class for {@link FreemarkerDynamicQlBuilder}.
 * <br />
 * Default configuration values
 * <ul>
 *     <li>qlDirectivePrefix : <code>ql</code></li>
 *     <li>paramMethodName : <code>param</code></li>
 *     <li>queryTemplateNamePostfix : <code>.ql.ftl</code></li>
 * </ul>
 */
public class FreemarkerDynamicQlBuilderFactory {
    public static final String DEFAULT_QL_DIRECTIVE_PREFIX = "ql";
    public static final String DEFAULT_PARAM_METHOD_NAME = "param";
    public static final String DEFAULT_QUERY_TEMPLATE_NAME_POSTFIX = ".ql.ftl";

    public static final TemplateModelObjectUnwrapper DEFAULT_TEMPLATE_MODEL_OBJECT_UNWRAPPER =
            new TemplateModelObjectUnwrapperDefaultImpl();

    /**
     * Freemarker Configuration
     */
    private Configuration freemarkerConfiguration;

    /**
     * Freemarker QL Custom Directive prefix
     */
    private String qlDirectivePrefix = DEFAULT_QL_DIRECTIVE_PREFIX;

    /**
     * Freemarker param custom method name
     */
    private String paramMethodName = DEFAULT_PARAM_METHOD_NAME;

    /**
     * queryTemplateNamePostfix
     */
    private String queryTemplateNamePostfix = DEFAULT_QUERY_TEMPLATE_NAME_POSTFIX;

    private Map<String, ParameterConverter> parameterConverters = new HashMap<String, ParameterConverter>();

    private TemplateModelObjectUnwrapper templateModelObjectUnwrapper = DEFAULT_TEMPLATE_MODEL_OBJECT_UNWRAPPER;

    Configuration getFreemarkerConfiguration() {
        return freemarkerConfiguration;
    }

    String getQlDirectivePrefix() {
        return qlDirectivePrefix;
    }

    String getParamMethodName() {
        return paramMethodName;
    }

    Map<String, ParameterConverter> getParameterConverters() {
        return parameterConverters;
    }

    TemplateModelObjectUnwrapper getTemplateModelObjectUnwrapper() {
        return templateModelObjectUnwrapper;
    }

    /**
     * Create new instance.
     * @param freemarkerConfiguration must not be null.
     */
    public FreemarkerDynamicQlBuilderFactory(Configuration freemarkerConfiguration) {
        if (freemarkerConfiguration == null) {
            throw new IllegalArgumentException("freemarkerConfiguration must not be null.");
        }

        this.freemarkerConfiguration = freemarkerConfiguration;
    }

    private void checkNotNullOrNotEmpty(String argName, String obj) {
        if (obj == null || obj.isEmpty()) {
            throw new IllegalArgumentException(argName + " must not be null or empty.");
        }
    }
    /**
     * set qlDirectivePrefix.
     *
     * @param qlDirectivePrefix must not be null or empty
     * @return this
     */
    public FreemarkerDynamicQlBuilderFactory qlDirectivePrefix(String qlDirectivePrefix) {
        checkNotNullOrNotEmpty("qlDirectivePrefix", qlDirectivePrefix);

        this.qlDirectivePrefix = qlDirectivePrefix;
        return this;
    }

    /**
     * set paramMethodName
     *
     * @param paramMethodName must not be null or empty
     * @return this
     */
    public FreemarkerDynamicQlBuilderFactory paramMethodName(String paramMethodName) {
        checkNotNullOrNotEmpty("paramMethodName", paramMethodName);

        this.paramMethodName = paramMethodName;
        return this;
    }

    /**
     * queryTemplateNamePostfix will be added to queryTemplateName when create template instances from freemarkerConfiguration.<br />
     * For example, when queryTemplateNamePostfix is ".ql.ftl" and you call <code>buildQuery("/users/select", dataModel)</code>,
     * then builder will create a template instance with name "/user/select.ql.ftl".
     *
     * @param queryTemplateNamePostfix must not be null. empty is ok.
     * @return this
     */
    public FreemarkerDynamicQlBuilderFactory queryTemplateNamePostfix(String queryTemplateNamePostfix) {
        if (queryTemplateNamePostfix == null) {
            throw new IllegalArgumentException("queryTemplateNamePostfix must not be null.");
        }

        this.queryTemplateNamePostfix = queryTemplateNamePostfix;
        return this;
    }

    public FreemarkerDynamicQlBuilderFactory templateModelObjectUnwrapper(TemplateModelObjectUnwrapper templateModelObjectUnwrapper) {
        if (templateModelObjectUnwrapper == null) {
            throw new IllegalArgumentException("templateModelObjectUnwrapper must not be null.");
        }

        this.templateModelObjectUnwrapper = templateModelObjectUnwrapper;
        return this;
    }

    /**
     * add new {@link ParameterConverter}.
     *
     * @param parameterConverterName converter parameterConverterName must not be null or empty. duplicated key is not allowed.
     * @param parameterConverter must not be null
     * @return this
     */
    public FreemarkerDynamicQlBuilderFactory addParameterConverter(String parameterConverterName, ParameterConverter parameterConverter) {
        checkNotNullOrNotEmpty("parameterConverterName", parameterConverterName);
        if (parameterConverter == null) {
            throw new IllegalArgumentException("parameterConverter must not be null.");
        }

        if (parameterConverters.containsKey(parameterConverterName)) {
            throw new IllegalArgumentException("parameterConverterName - " + parameterConverterName + " is duplicated.");
        }

        parameterConverters.put(parameterConverterName, parameterConverter);
        return this;
    }

    /**
     * bulk add {@link ParameterConverter}.
     *
     * @param otherParameterConverters converter name must not be null or empty. duplicated key is not allowed.
     * @return this
     */
    public FreemarkerDynamicQlBuilderFactory addAllParameterConverters(Map<String, ParameterConverter> otherParameterConverters) {
        if (otherParameterConverters == null) {
            return this;
        }

        for (Map.Entry<String, ParameterConverter> parameterConverterEntry : otherParameterConverters.entrySet()) {
            addParameterConverter(parameterConverterEntry.getKey(), parameterConverterEntry.getValue());
        }
        return this;
    }

    public FreemarkerDynamicQlBuilderFactory clearParameterConverters() {
        parameterConverters.clear();
        return this;
    }

    public FreemarkerDynamicQlBuilder getFreemarkerDynamicQlBuilder() {
        doConfigure();
        return doBuildFreemarkerDynamicQlBuilder();
    }

    private void doConfigure() {
        Map<String, TemplateDirectiveModel> qlDirectives = new HashMap<String, TemplateDirectiveModel>();
        qlDirectives.put(SetDirective.DIRECTIVE_NAME, new SetDirective());
        qlDirectives.put(TrimDirective.DIRECTIVE_NAME, new TrimDirective());
        qlDirectives.put(WhereDirective.DIRECTIVE_NAME, new WhereDirective());

        try {
            freemarkerConfiguration.setSharedVariable(qlDirectivePrefix, qlDirectives);
        } catch (TemplateModelException ex) {
            throw new IllegalStateException("Failed to configurate freemarkerConfiguration.", ex);
        }
    }

    private FreemarkerDynamicQlBuilderImpl doBuildFreemarkerDynamicQlBuilder() {
        FreemarkerDynamicQlBuilderImpl freemarkerDynamicQlBuilder = new FreemarkerDynamicQlBuilderImpl();
        freemarkerDynamicQlBuilder.setFreemarkerConfiguration(freemarkerConfiguration);
        freemarkerDynamicQlBuilder.setQlDirectivePrefix(qlDirectivePrefix);
        freemarkerDynamicQlBuilder.setParamMethodName(paramMethodName);
        freemarkerDynamicQlBuilder.setQueryTemplateNamePostfix(queryTemplateNamePostfix);
        freemarkerDynamicQlBuilder.setParameterConverters(Collections.unmodifiableMap(parameterConverters));
        freemarkerDynamicQlBuilder.setTemplateModelObjectUnwrapper(templateModelObjectUnwrapper);
        return freemarkerDynamicQlBuilder;
    }
}