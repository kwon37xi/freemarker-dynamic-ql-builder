package kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter;

/**
 * {@link Enum} type to the enum's ordinal() number converter.<br />
 * Beware, saving enum's ordinal is not good practice. You'd better save enum's name().
 */
public class EnumToOrdinalParameterConverter implements ParameterConverter {
    @Override
    public Object convert(Object from) {
        if (from == null) {
            return null;
        }

        Enum<?> enumValue = (Enum<?>) from;
        return enumValue.ordinal();
    }
}
