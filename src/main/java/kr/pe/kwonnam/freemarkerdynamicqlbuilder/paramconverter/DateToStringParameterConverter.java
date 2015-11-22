package kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * {@link Date} type to {@link String} type parameter converter.
 */
public class DateToStringParameterConverter implements ParameterConverter {
    private String dateFormat;

    public DateToStringParameterConverter(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public Object convert(Object from) {
        if (from == null) {
            return null;
        }

        Date date = (Date) from;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        return sdf.format(date);
    }
}
