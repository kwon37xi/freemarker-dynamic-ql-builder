package kr.pe.kwonnam.fdqlbuilder.methods;

import freemarker.template.*;
import kr.pe.kwonnam.fdqlbuilder.objectunwrapper.TemplateModelObjectUnwrapper;
import kr.pe.kwonnam.fdqlbuilder.paramconverter.ParameterConverter;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * ParamMethod take a query parameter object from freemarker templates,
 * and adds the parameter object to {@link #parameters} list.<br/>
 * Finally this puts a {@code ?} jdbc query parameter bind character to the template result.
 * <p/>
 * Usage : <code>${param(parameter[, parameterConverterName]}</code><br/>
 * <ul>
 * <li>{@code parameter} will be bound to {@link java.sql.PreparedStatement} parameter.</li>
 * <li>{@code converterName} is used to find {@link ParameterConverter} and convvert the parameter to another object.</li>
 * </ul>
 * <p/>
 * This is NOT thread safe. Each template processing request must create new {@link ParamMethod} object.
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
     * To unwrap freemarker {@link TemplateModel} object
     */
    private TemplateModelObjectUnwrapper templateModelObjectUnwrapper;

    /**
     * To convert parameters to another type
     */
    private Map<String, ParameterConverter> parameterConverters;

    public ParamMethod(TemplateModelObjectUnwrapper templateModelObjectUnwrapper, Map<String, ParameterConverter> parameterConverters) {
        if (templateModelObjectUnwrapper == null) {
            throw new IllegalArgumentException("templateModelObjectUnwrapper must not be null.");
        }

        if (parameterConverters == null) {
            throw new IllegalArgumentException("parameterConverters must not be null.");
        }

        this.templateModelObjectUnwrapper = templateModelObjectUnwrapper;
        this.parameterConverters = parameterConverters;
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