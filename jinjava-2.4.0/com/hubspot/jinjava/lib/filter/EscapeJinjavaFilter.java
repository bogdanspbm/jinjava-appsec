package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Converts the characters { and } in string s to Jinjava-safe sequences. Use this filter if you need to display text that might contain such characters in Jinjava. Marks return value as markup string.",
   params = {@JinjavaParam(
   value = "s",
   desc = "String to escape"
)},
   snippets = {@JinjavaSnippet(
   code = "{% set escape_string = \"{{This markup is printed as text}}\" %}\n{{ escape_string|escapeJinjava }}"
)}
)
public class EscapeJinjavaFilter implements Filter {
   private static final String SLBRACE = "{";
   private static final String BLBRACE = "&lbrace;";
   private static final String SRBRACE = "}";
   private static final String BRBRACE = "&rbrace;";
   private static final String[] TO_REPLACE = new String[]{"{", "}"};
   private static final String[] REPLACE_WITH = new String[]{"&lbrace;", "&rbrace;"};

   public static String escapeJinjavaEntities(String input) {
      return StringUtils.replaceEach(input, TO_REPLACE, REPLACE_WITH);
   }

   public Object filter(Object object, JinjavaInterpreter interpreter, String... arg) {
      return escapeJinjavaEntities(Objects.toString(object, ""));
   }

   public String getName() {
      return "escape_jinjava";
   }
}
