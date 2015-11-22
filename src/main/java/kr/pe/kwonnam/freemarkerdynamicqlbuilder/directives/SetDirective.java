package kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives.trim.Trim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Directive for SQL {@code SET} statement.
 * <p/>
 * This is the same as {@link TrimDirective} with <code>prefix="SET "</code> and <code>suffixOverrides=[',']</code> options.
 */
public class SetDirective extends AbstractBaseTrimDirective {
    private static final String PREFIX = "SET ";
    private static final List<String> SUFFIX_OVERRIDES = Collections.unmodifiableList(new ArrayList<String>(Arrays.asList(new String[]{","})));

    private static final Trim SET_TRIM;

    static {
        SET_TRIM = new Trim();
        SET_TRIM.setPrefix(PREFIX);
        SET_TRIM.setSuffixOverrides(SUFFIX_OVERRIDES);
    }

    public SetDirective() {
        super(SET_TRIM);
    }
}