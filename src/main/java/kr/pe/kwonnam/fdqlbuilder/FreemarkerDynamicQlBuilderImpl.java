package kr.pe.kwonnam.fdqlbuilder;

import freemarker.template.Configuration;
import kr.pe.kwonnam.fdqlbuilder.objectunwrapper.TemplateModelObjectUnwrapper;
import kr.pe.kwonnam.fdqlbuilder.paramconverter.ParameterConverter;

import java.util.List;
import java.util.Map;

/**
 * Default {@link FreemarkerDynamicQlBuilder} instance.
 * <br/>
 * Do not instantiate directlry. Use {@link FreemarkerDynamicQlBuilderFactory}.
 * <p/>
 * The isntance of this class is thread safe. But you MUST not change freemarkerConfigration object status(DO NOT call setter methods after the object configured).
 */
public class FreemarkerDynamicQlBuilderImpl implements FreemarkerDynamicQlBuilder {

    /**
     * Freemarker configuration
     */
    private Configuration freemarkerConfiguration;

    /**
     * Freemarker QL Custom Directive prefix
     */
    private String qlDirectivePrefix;

    /**
     * Freemarker param custom method name
     */
    private String paramMethodName;

    /**
     * Parameter Converters
     */
    private Map<String, ParameterConverter> parameterConverters;

    /**
     * TemplateModelObject Unwrapper
     */
    private TemplateModelObjectUnwrapper templateModelObjectUnwrapper;

    Configuration getFreemarkerConfiguration() {
        return freemarkerConfiguration;
    }

    void setFreemarkerConfiguration(Configuration freemarkerConfiguration) {
        this.freemarkerConfiguration = freemarkerConfiguration;
    }

    String getQlDirectivePrefix() {
        return qlDirectivePrefix;
    }

    void setQlDirectivePrefix(String qlDirectivePrefix) {
        this.qlDirectivePrefix = qlDirectivePrefix;
    }

    String getParamMethodName() {
        return paramMethodName;
    }

    void setParamMethodName(String paramMethodName) {
        this.paramMethodName = paramMethodName;
    }

    Map<String, ParameterConverter> getParameterConverters() {
        return parameterConverters;
    }

    void setParameterConverters(Map<String, ParameterConverter> parameterConverters) {
        this.parameterConverters = parameterConverters;
    }

    TemplateModelObjectUnwrapper getTemplateModelObjectUnwrapper() {
        return templateModelObjectUnwrapper;
    }

    void setTemplateModelObjectUnwrapper(TemplateModelObjectUnwrapper templateModelObjectUnwrapper) {
        this.templateModelObjectUnwrapper = templateModelObjectUnwrapper;
    }

    @Override
    public Query buildQuery(String name, Map<String, Object> dataModel) {
        return null;
    }
}
