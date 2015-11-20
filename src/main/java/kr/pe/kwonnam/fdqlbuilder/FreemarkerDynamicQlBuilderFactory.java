package kr.pe.kwonnam.fdqlbuilder;

import freemarker.template.Configuration;
import kr.pe.kwonnam.fdqlbuilder.paramconverter.ParameterConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Factory class for {@link FreemarkerDynamicQlBuilder}.
 */
public class FreemarkerDynamicQlBuilderFactory {
    public static final String DEFAULT_QL_DIRECTIVE_PREFIX = "ql";
    public static final String DEFAULT_PARAM_METHOD_NAME = "param";

    /** Freemarker Configuration */
    private Configuration freemarkerConfiguration;

    /** Freemarker QL Custom Directive prefix */
    private String qlDirectivePrefix = DEFAULT_QL_DIRECTIVE_PREFIX;

    /** Freemarker param custom method name */
    private String paramMethodName = DEFAULT_PARAM_METHOD_NAME;

    /** Parameter Converters */
    private List<ParameterConverter> parameterConverters = new ArrayList<ParameterConverter>();

    public FreemarkerDynamicQlBuilderFactory(Configuration freemarkerConfiguration) {
        this.freemarkerConfiguration = freemarkerConfiguration;

    }

    public FreemarkerDynamicQlBuilderFactory qlDirectivePrefix(String qlDirectivePrefix) {
        this.qlDirectivePrefix = qlDirectivePrefix;
        return this;
    }

    public FreemarkerDynamicQlBuilderFactory paramMethodName(String paramMethodName) {
        this.paramMethodName = paramMethodName;
        return this;
    }

    public FreemarkerDynamicQlBuilderFactory addParameterConverter(ParameterConverter parameterConverter) {
        parameterConverters.add(parameterConverter);
        return this;
    }

    public FreemarkerDynamicQlBuilderFactory addAllParameterConverters(Collection<ParameterConverter> otherParameterConverters) {
        this.parameterConverters.addAll(otherParameterConverters);
        return this;
    }

    public FreemarkerDynamicQlBuilderFactory clearParameterConverters() {
        parameterConverters.clear();
        return this;
    }
}