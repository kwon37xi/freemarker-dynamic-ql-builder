package kr.pe.kwonnam.fdqlbuilder.paramconverter;

import kr.pe.kwonnam.fdqlbuilder.methods.ParamMethod;

/**
 * Sometimes a java object and a database column type are not the same.
 * You may need to convert from the java object to another object that matches the column type.
 * <p/>
 * If you register this interface implementation to the configuration,
 * {@link ParamMethod} will call this when required and the converted object will be set as query parameter.
 * <p/>
 * {@link ParamMethod} allows null value. So you need to take care of null input parameter.
 */
public interface ParameterConverter {

    /**
     * Convert from object to another object.
     * @param from source object.
     * @return target object.
     */
    Object convert(Object from);
}