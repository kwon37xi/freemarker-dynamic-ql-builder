package kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import kr.pe.kwonnam.freemarkerdynamicqlbuilder.directives.trim.Trim;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class AbstractBaseTrimDirective implements TemplateDirectiveModel {
    private final Trim trim;

    public AbstractBaseTrimDirective(Trim trim) {
        this.trim = trim;
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (body == null) {
            return;
        }

        StringWriter bodyWriter = new StringWriter();
        body.render(bodyWriter);

        String trimmed = trim.trim(bodyWriter.toString());

        Writer out = env.getOut();
        out.write(trimmed);
    }
}
