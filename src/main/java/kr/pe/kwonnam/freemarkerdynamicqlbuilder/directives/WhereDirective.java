package kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives;

import kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives.trim.Trim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Directive for SQL {@code WHERE} statement.
 * <p/>
 * This is the same as {@link TrimDirective} with <code>prefix="WHERE "</code> and <code>prefixOverrides=['AND ','and ', 'OR ', 'or ']</code> options.
 */
public class WhereDirective extends AbstractBaseTrimDirective {
    public static final String DIRECTIVE_NAME = "where";
    private static final String PREFIX = "WHERE ";
    private static final List<String> PREFIX_OVERRIDES = Collections.unmodifiableList(new ArrayList<String>(Arrays.asList(new String[]{"AND ", "and ", "OR ", "or "})));

    private static final Trim WHERE_TRIM;

    static {
        WHERE_TRIM = new Trim();
        WHERE_TRIM.setPrefix(PREFIX);
        WHERE_TRIM.setPrefixOverrides(PREFIX_OVERRIDES);
    }

    public WhereDirective() {
        super(WHERE_TRIM);
    }
}
