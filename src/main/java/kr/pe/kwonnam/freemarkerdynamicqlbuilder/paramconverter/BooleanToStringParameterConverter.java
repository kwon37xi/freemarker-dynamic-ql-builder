package kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter;

/**
 * {@link Boolean} type to {@link String} type parameter converter.
 */
public class BooleanToStringParameterConverter implements ParameterConverter {

    private String trueString;
    private String falseString;

    public BooleanToStringParameterConverter(String trueString, String falseString) {
        this.trueString = trueString;
        this.falseString = falseString;
    }

    @Override
    public Object convert(Object from) {
        if (from == null) {
            return null;
        }

        return ((Boolean) from).booleanValue() ? trueString : falseString;
    }
}
