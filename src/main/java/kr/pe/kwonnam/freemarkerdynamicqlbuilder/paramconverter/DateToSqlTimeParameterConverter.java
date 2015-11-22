package kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter;

import java.util.Date;

/**
 * {@link java.util.Date} type to {@link java.sql.Time} parameter converter.
 * It will truncate date part.
 */
public class DateToSqlTimeParameterConverter implements ParameterConverter {

    @Override
    public Object convert(Object from) {
        if (from == null) {
            return null;
        }
        Date date = (Date) from;
        return new java.sql.Time(date.getTime());
    }
}
