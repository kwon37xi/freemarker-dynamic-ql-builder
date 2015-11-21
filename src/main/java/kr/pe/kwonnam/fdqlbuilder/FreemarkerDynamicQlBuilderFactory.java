package kr.pe.kwonnam.fdqlbuilder;

import freemarker.template.Configuration;
import kr.pe.kwonnam.fdqlbuilder.objectunwrapper.TemplateModelObjectUnwrapper;
import kr.pe.kwonnam.fdqlbuilder.objectunwrapper.TemplateModelObjectUnwrapperDefaultImpl;
import kr.pe.kwonnam.fdqlbuilder.paramconverter.ParameterConverter;

import java.util.*;

/**
 * Factory class for {@link FreemarkerDynamicQlBuilder}.
 */
public class FreemarkerDynamicQlBuilderFactory {
    public static final String DEFAULT_QL_DIRECTIVE_PREFIX = "ql";
    public static final String DEFAULT_PARAM_METHOD_NAME = "param";
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

    private void checkNotNullOrNotEmpty(String argName, String object) {
        if (object == null || object.equals("")) {
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
        FreemarkerDynamicQlBuilderImpl freemarkerDynamicQlBuilder = new FreemarkerDynamicQlBuilderImpl();
        freemarkerDynamicQlBuilder.setFreemarkerConfiguration(freemarkerConfiguration);
        freemarkerDynamicQlBuilder.setQlDirectivePrefix(qlDirectivePrefix);
        freemarkerDynamicQlBuilder.setParamMethodName(paramMethodName);
        freemarkerDynamicQlBuilder.setParameterConverters(Collections.unmodifiableMap(parameterConverters));
        freemarkerDynamicQlBuilder.setTemplateModelObjectUnwrapper(templateModelObjectUnwrapper);
        return freemarkerDynamicQlBuilder;
    }
}