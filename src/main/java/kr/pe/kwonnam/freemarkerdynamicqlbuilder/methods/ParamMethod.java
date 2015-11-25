package kr.pe.kwonnam.freemarkerdynamicqlbuilder.methods;

import freemarker.template.*;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.objectunwrapper.TemplateModelObjectUnwrapper;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter.ParameterConverter;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * ParamMethod take a query parameter object from freemarker templates,
 * and adds the parameter object to {@link #parameters} list.<br>
 * Finally this puts a {@code ?} jdbc query parameter bind character to the template result.
 * <p>
 * Usage : <code>${param(parameter[, parameterConverterName]}</code><br>
 * <ul>
 * <li>{@code parameter} will be bound to {@link java.sql.PreparedStatement} parameter.</li>
 * <li>{@code converterName} is used to find {@link ParameterConverter} and convvert the parameter to another object.</li>
 * </ul>
 * <p>
 * This is NOT thread safe. Each template processing request must create new instance.
 *
 * @see ParameterConverter
 */
public class ParamMethod implements TemplateMethodModelEx {
    public static final SimpleScalar JDBC_POSITIONAL_PARAMETER_STRING = new SimpleScalar("?");

    private Logger log = getLogger(ParamMethod.class);

    /**
     * Query Parameters to be bound
     */
    private List<Object> parameters = new ArrayList<Object>();

    /**
     * whether generating position string(?) with index number or not?
     */
    private boolean withPositionalIndex = false;

    private int currentIndex = 0;

    /**
     * To unwrap freemarker {@link TemplateModel} object
     */
    private TemplateModelObjectUnwrapper templateModelObjectUnwrapper;

    /**
     * To convert parameters to another type
     */
    private Map<String, ParameterConverter> parameterConverters;

    public ParamMethod(TemplateModelObjectUnwrapper templateModelObjectUnwrapper, Map<String, ParameterConverter> parameterConverters, boolean withPositionalIndex) {
        if (templateModelObjectUnwrapper == null) {
            throw new IllegalArgumentException("templateModelObjectUnwrapper must not be null.");
        }

        if (parameterConverters == null) {
            throw new IllegalArgumentException("parameterConverters must not be null.");
        }

        this.templateModelObjectUnwrapper = templateModelObjectUnwrapper;
        this.parameterConverters = parameterConverters;
        this.withPositionalIndex = withPositionalIndex;
    }

    public List<Object> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        checkArgumentsConditions(arguments);

        Object queryParam = arguments.get(0);
        log.debug("Original QueryParam : {}", queryParam);

        queryParam = unwrapQueryParam(queryParam);

        queryParam = processParameterConverter(arguments, queryParam);
        parameters.add(queryParam);

        incrementCurrentIndex();
        return getPositionalParameterString();
    }

    private void incrementCurrentIndex() {
        currentIndex = currentIndex + 1;
    }

    private SimpleScalar getPositionalParameterString() {
        if (withPositionalIndex) {
            return new SimpleScalar("?" + currentIndex);
        }
        return JDBC_POSITIONAL_PARAMETER_STRING;
    }

    private void checkArgumentsConditions(List arguments) {
        int argumentsSize = arguments.size();

        if (argumentsSize == 0) {
            throw new IllegalArgumentException("Query parameter required.");
        }

        if (argumentsSize > 2) {
            throw new IllegalArgumentException("paramMethod's argument must be 1 or 2 but " + argumentsSize + ".");
        }
    }

    private Object processParameterConverter(List arguments, Object queryParam) {
        if (arguments.size() != 2) {
            return queryParam;
        }

        String parameterConverterName = parseParameterConverterName(arguments);
        ParameterConverter parameterConverter = findParameterConverter(parameterConverterName);

        Object converted = parameterConverter.convert(queryParam);
        log.debug("Original QueryParam {} converted to {}", queryParam, converted);
        return converted;
    }

    private String parseParameterConverterName(List arguments) {
        SimpleScalar parameterConverterNameScalar = (SimpleScalar) arguments.get(1);

        if (parameterConverterNameScalar == null) {
            throw new IllegalArgumentException("parameterConverterName must not be null.");
        }

        return parameterConverterNameScalar.getAsString();
    }

    private ParameterConverter findParameterConverter(String parameterConverterName) {
        ParameterConverter parameterConverter = parameterConverters.get(parameterConverterName);
        if (parameterConverter == null) {
            throw new IllegalArgumentException("parameterConverter - " + parameterConverterName + " does not exist.");
        }
        return parameterConverter;
    }

    private Object unwrapQueryParam(Object queryParam) throws TemplateModelException {
        if (!(queryParam instanceof TemplateModel)) {
            return queryParam;
        }

        Object unwrapped = templateModelObjectUnwrapper.unwrap((TemplateModel)queryParam);
        log.debug("Unwrapped QueryParam : {}", unwrapped);
        return unwrapped;
    }
}