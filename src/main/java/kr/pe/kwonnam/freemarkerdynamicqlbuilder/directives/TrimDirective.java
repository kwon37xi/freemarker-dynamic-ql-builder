package kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives;

import freemarker.core.Environment;
import freemarker.template.*;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives.trim.Trim;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * String trim custom directive.
 * <br/>
 * Parameters
 * <ul>
 * <li><code>prefix</code> : prefix will be added to the trimmed string's head if the trimmed string is not empty.</li>
 * <li><code>prefixOverrides</code> : After trimming, if the trimmed string starts with one of the prefixRemoves,
 * only the first matching item from the head of the trimmed string will be removed.</li>
 * <li><code>suffix</code> : suffix will be add to the trimmed string's tail if the trimmed string is not empty.</li>
 * <li><code>suffixOverrides</code> : After trimming, if the trimmed string ends with one of the suffixOverrides,
 * only the first matching item from the tail of the trimmed string will be removed.</li>
 * </ul>
 */
public class TrimDirective implements TemplateDirectiveModel {
    public static final String DIRECTIVE_NAME = "trim";
    public static final String PREFIX_PARAM_NAME = "prefix";
    public static final String SUFFIX_PARAM_NAME = "suffix";
    public static final String PREFIX_OVERRIDES_PARAM_NAME = "prefixOverrides";
    public static final String SUFFIX_OVERRIDES_PARAM_NAME = "suffixOverrides";

    private Logger log = getLogger(TrimDirective.class);

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (body == null) {
            return;
        }

        StringWriter bodyWriter = new StringWriter();
        body.render(bodyWriter);

        String prefix = parseStringParameter(params, PREFIX_PARAM_NAME);
        String suffix = parseStringParameter(params, SUFFIX_PARAM_NAME);
        List<String> prefixOverrides = parseSequenceParameter(params, PREFIX_OVERRIDES_PARAM_NAME);
        List<String> suffixOverrides = parseSequenceParameter(params, SUFFIX_OVERRIDES_PARAM_NAME);

        Trim trim = new Trim();
        trim.setPrefix(prefix);
        trim.setSuffix(suffix);
        trim.setPrefixOverrides(prefixOverrides);
        trim.setSuffixOverrides(suffixOverrides);

        String trimmed = trim.trim(bodyWriter.toString());

        Writer out = env.getOut();
        out.write(trimmed);
    }

    private String parseStringParameter(Map params, String paramName) {
        Object paramModel = params.get(paramName);
        if (paramModel == null) {
            return null;
        }

        if (!(paramModel instanceof SimpleScalar)) {
            throw new IllegalArgumentException(paramName + " must be string.");
        }
        return ((SimpleScalar) paramModel).getAsString();
    }

    private List<String> parseSequenceParameter(Map params, String paramName) throws TemplateModelException {
        Object paramModel = params.get(paramName);
        if (paramModel == null) {
            return null;
        }

        if (!(paramModel instanceof SimpleSequence)) {
            throw new IllegalArgumentException(paramName + " must be sequence.");
        }

        List<String> strings = transformSimpleSequenceToStringList((SimpleSequence) paramModel, paramName);

        log.debug("sequence parameter {} -> {}", paramName, strings);

        return strings;
    }

    private List<String> transformSimpleSequenceToStringList(SimpleSequence sequence, String paramName) throws TemplateModelException {
        List<String> strings = new ArrayList<String>();
        int size = sequence.size();

        for (int i = 0; i < size; i++) {
            TemplateModel item = sequence.get(i);

            if (!(item instanceof SimpleScalar)) {
                throw new IllegalArgumentException(paramName + "'s item must be string.");
            }

            strings.add(((SimpleScalar) item).getAsString());
        }
        return strings;
    }
}