package kr.pe.kwonnam.fdqlbuilder.methods;

import freemarker.template.*;
import kr.pe.kwonnam.fdqlbuilder.paramconverter.ParameterConverter;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * ParamMethod gets query parameter object from freemarker templates, and adds the parameter object to {@link #parameters} list.
 * Finally this puts a {@code ?} jdbc query parameter bind character to template.
 * <p/>
 * <p/>
 * This is NOT thread safe. Each template processing request must create new {@link ParamMethod} object.
 */
public class ParamMethod implements TemplateMethodModelEx {

    private Logger log = getLogger(ParamMethod.class);

    private List<Object> parameters = new ArrayList<Object>();

    private ObjectWrapperAndUnwrapper objectWrapperAndUnwrapper;

    private Map<String, ParameterConverter<?, ?>> parameterConverters;

    public ParamMethod(ObjectWrapperAndUnwrapper objectWrapperAndUnwrapper, Map<String, ParameterConverter<?, ?>> parameterConverters) {
        this.objectWrapperAndUnwrapper = objectWrapperAndUnwrapper;
        this.parameterConverters = parameterConverters;
    }

    public List<Object> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {

        Object parameter = unwrapDataModel(arguments.get(0)) ;

        parameters.add(parameter);
        return new SimpleScalar("?");
    }

    /**
     * Freemarker wraps java object as {@link TemplateModel} instance.
     * To set the object as {@link java.sql.PreparedStatement} parameter, the wrapped object must be unwrapped.
     * <p/>
     * Freemarker has {@link ObjectWrapperAndUnwrapper} interface but this is not official yes before freemarker 2.4.
     * Beware freemarker version.
     *
     * @param parameter Method parameter
     * @return if parameter is an instance of {@link TemplateModel} then unwrapped object returned or input parameter returned.
     */
    protected Object unwrapDataModel(Object parameter) throws TemplateModelException {
        log.debug("Parameter class : {}, value : {}", parameter.getClass(), parameter);
        boolean isTEmplateModel = parameter instanceof TemplateModel;

        if (!isTEmplateModel) {
            return parameter;
        }

        TemplateModel templateModel = (TemplateModel) parameter;
        Object unwrapped = objectWrapperAndUnwrapper.unwrap(templateModel);
        log.debug("Unwrapped parameter : {}, value : {}", unwrapped.getClass(), unwrapped);

        return unwrapped;
    }
}
