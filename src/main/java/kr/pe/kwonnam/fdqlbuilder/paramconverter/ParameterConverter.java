package kr.pe.kwonnam.fdqlbuilder.paramconverter;

import kr.pe.kwonnam.fdqlbuilder.methods.ParamMethod;

/**
 * Parameter converter.
 * <p/>
 * This is called through {@link ParamMethod}.
 * <p/>
 * Sometime java object and database column type are mismatch.
 * You may need to convert from java object to column type.
 * Implement this interface and pass
 * <p/>
 * {@link ParamMethod} allows null value. So you need to take care of null input parameter.
 */
public interface ParameterConverter<F, T> {

    /**
     * Convert {@code F from} object to {@code T} object.
     * @param from source object.
     * @return target object.
     */
    T convert(F from);
}