package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Capitalize a value. The first character will be uppercase, all others lowercase.",
   params = {@JinjavaParam(
   value = "string",
   desc = "String to capitalize the first letter of"
)},
   snippets = {@JinjavaSnippet(
   code = "{% set sentence = \"the first letter of a sentence should always be capitalized.\" %}\n{{ sentence|capitalize }}"
)}
)
public class CapitalizeFilter implements Filter {
   public String getName() {
      return "capitalize";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      if (var instanceof String) {
         String value = (String)var;
         return StringUtils.capitalize(value);
      } else {
         return var;
      }
   }
}
