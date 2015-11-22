package kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter;

/**
 * {@link Boolean} type to {@link Number} type parameter converter.
 */
public class BooleanToNumberParameterConverter implements ParameterConverter {
    private Number trueValue;
    private Number falseValue;

    /**
     * constructor.
     *
     * @param trueValue  number for true
     * @param falseValue number for false
     */
    public BooleanToNumberParameterConverter(Number trueValue, Number falseValue) {
        this.trueValue = trueValue;
        this.falseValue = falseValue;
    }

    @Override
    public Object convert(Object from) {
        if (from == null) {
            return null;
        }

        return ((Boolean) from).booleanValue() ? trueValue : falseValue;
    }
}
