package kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter;

import java.util.Date;

/**
 * {@link java.util.Date} type to {@link java.sql.Date} parameter converter.
 * It will truncate time part.
 */
public class DateToSqlDateParameterConverter implements ParameterConverter {

    @Override
    public Object convert(Object from) {
        if (from == null) {
            return null;
        }

        Date date = (Date) from;
        return new java.sql.Date(date.getTime());
    }
}
