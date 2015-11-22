package kr.pe.kwonnam.freemarkerdynamicqlbuilder.paramconverter;

/**
 * {@link Enum} type to the enum's name() string converter.
 */
public class EnumToNameParameterConverter implements ParameterConverter {
    @Override
    public Object convert(Object from) {
        if (from == null) {
            return null;
        }

        Enum<?> enumValue = (Enum<?>) from;
        return enumValue.name();
    }
}