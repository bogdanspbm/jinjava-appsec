package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Converts the characters &, <, >, ‘, and ” in string s to HTML-safe sequences. Use this filter if you need to display text that might contain such characters in HTML. Marks return value as markup string.",
   params = {@JinjavaParam(
   value = "s",
   desc = "String to escape"
)},
   snippets = {@JinjavaSnippet(
   code = "{% set escape_string = \"<div>This markup is printed as text</div>\" %}\n{{ escape_string|escape }}"
)}
)
public class EscapeFilter implements Filter {
   private static final String SAMP = "&";
   private static final String BAMP = "&amp;";
   private static final String SGT = ">";
   private static final String BGT = "&gt;";
   private static final String SLT = "<";
   private static final String BLT = "&lt;";
   private static final String BSQ = "&#39;";
   private static final String BDQ = "&quot;";
   private static final String[] TO_REPLACE = new String[]{"&", ">", "<", "'", "\""};
   private static final String[] REPLACE_WITH = new String[]{"&amp;", "&gt;", "&lt;", "&#39;", "&quot;"};

   public static String escapeHtmlEntities(String input) {
      return StringUtils.replaceEach(input, TO_REPLACE, REPLACE_WITH);
   }

   public Object filter(Object object, JinjavaInterpreter interpreter, String... arg) {
      return escapeHtmlEntities(Objects.toString(object, ""));
   }

   public String getName() {
      return "escape";
   }
}
