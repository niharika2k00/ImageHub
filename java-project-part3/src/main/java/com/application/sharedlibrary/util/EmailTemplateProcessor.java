package com.application.sharedlibrary.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailTemplateProcessor {

  public String processContent(String mailBody, Map<String, String> replacements) {
    // Parse Markdown to HTML
    Parser p = Parser.builder().build();
    Node document = p.parse(mailBody);
    HtmlRenderer renderer = HtmlRenderer.builder().build();
    String mailBodyHtml = renderer.render(document);

    // Replace all placeholders
    for (Map.Entry<String, String> entry : replacements.entrySet()) {
      mailBodyHtml = mailBodyHtml.replace(entry.getKey(), entry.getValue());
    }

    return mailBodyHtml;
  }
}
