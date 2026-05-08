package com.hammer.internal.notification.application.service;

import com.hammer.internal.notification.application.dto.PreviewTemplateResponse;
import com.hammer.internal.notification.application.port.in.PreviewTemplateUseCase;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
class PreviewTemplateService implements PreviewTemplateUseCase {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{(\\w+)}}");

    @Override
    public PreviewTemplateResponse preview(String titleTemplate, String bodyTemplate, Map<String, String> variables) {
        String renderedTitle = render(titleTemplate, variables);
        String renderedBody = render(bodyTemplate, variables);
        return new PreviewTemplateResponse(renderedTitle, renderedBody);
    }

    private String render(String template, Map<String, String> variables) {
        Matcher matcher = VARIABLE_PATTERN.matcher(template);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String key = matcher.group(1);
            String replacement = variables.getOrDefault(key, "{{" + key + "}}");
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
