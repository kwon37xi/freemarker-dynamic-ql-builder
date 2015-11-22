package kr.pe.kwonnam.freemarkerdynamicqlbuilder;

import freemarker.template.Configuration;
import freemarker.template.Template;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.methods.ParamMethod;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.objectunwrapper.TemplateModelObjectUnwrapper;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter.ParameterConverter;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Default {@link FreemarkerDynamicQlBuilder} instance.
 * <br/>
 * Do not instantiate directlry. Use {@link FreemarkerDynamicQlBuilderFactory}.
 * <p/>
 * The isntance of this class is thread safe. But you MUST not change freemarkerConfigration object status(DO NOT call setter methods after the object configured).
 */
public class FreemarkerDynamicQlBuilderImpl implements FreemarkerDynamicQlBuilder {

    private Logger log = getLogger(FreemarkerDynamicQlBuilderImpl.class);

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
     * queryTemplateNamePostfix will be added to template name.
     */
    private String queryTemplateNamePostfix;

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

    String getQueryTemplateNamePostfix() {
        return queryTemplateNamePostfix;
    }

    void setQueryTemplateNamePostfix(String queryTemplateNamePostfix) {
        this.queryTemplateNamePostfix = queryTemplateNamePostfix;
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
    public DynamicQuery buildQuery(String queryTemplateName) {
        return buildQuery(queryTemplateName, Collections.<String, Object>emptyMap());
    }

    @Override
    public DynamicQuery buildQuery(String queryTemplateName, Map<String, Object> dataModel) {
        verifyQueryTemplateName(queryTemplateName);
        verifyDataModel(dataModel);

        final String finalQueryTemplateName = queryTemplateName + queryTemplateNamePostfix;
        Template template = createTemplate(finalQueryTemplateName);
        DynamicQueryImpl query = processTemplate(dataModel, finalQueryTemplateName, template);

        log.debug("Query for templateName : {}, dataModel : {} -> {}", finalQueryTemplateName, dataModel, query);
        return query;
    }

    private Template createTemplate(String finalQueryTemplateName) {
        try {
            return freemarkerConfiguration.getTemplate(finalQueryTemplateName);
        } catch (IOException ex) {
            throw new IllegalStateException("Can not create freemarker template - " + finalQueryTemplateName + ".", ex);
        }
    }

    private DynamicQueryImpl processTemplate(Map<String, Object> dataModel, String finalQueryTemplateName, Template template) {
        final Map<String, Object> finalDataModel = new HashMap<String, Object>(dataModel);
        final ParamMethod paramMethod = new ParamMethod(templateModelObjectUnwrapper, parameterConverters);
        finalDataModel.put(paramMethodName, paramMethod);

        final StringWriter out = new StringWriter();
        try {
            template.process(finalDataModel, out);
        } catch (Exception ex) {
            throw new IllegalStateException("Can not process freemarker template - " + finalQueryTemplateName + ".", ex);
        }

        return new DynamicQueryImpl(out.toString(), paramMethod.getParameters());
    }

    private void verifyQueryTemplateName(String queryTemplateName) {
        if (queryTemplateName == null || queryTemplateName.isEmpty()) {
            throw new IllegalArgumentException("queryTemplateName must not be null or empty.");
        }
    }

    private void verifyDataModel(Map<String, Object> dataModel) {
        if (dataModel == null) {
            throw new IllegalArgumentException("dataModel must not be null.");
        }

        if (dataModel.keySet().contains(paramMethodName)) {
            throw new IllegalArgumentException("dataModel must not contain paramMethodName(" + paramMethodName + ") as a key.");
        }

        if (dataModel.keySet().contains(qlDirectivePrefix)) {
            throw new IllegalArgumentException("dataModel must not contain qlDirectivePrefix(" + qlDirectivePrefix + ") as a key.");
        }
    }
}