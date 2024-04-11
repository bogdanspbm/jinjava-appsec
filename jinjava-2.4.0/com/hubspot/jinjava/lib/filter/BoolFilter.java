package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import org.apache.commons.lang3.BooleanUtils;

@JinjavaDoc(
   value = "Convert value into a boolean.",
   params = {@JinjavaParam(
   value = "value",
   desc = "The value to convert to a boolean"
)},
   snippets = {@JinjavaSnippet(
   desc = "This example converts a text string value to a boolean",
   code = "{% if \"true\"|bool == true %}hello world{% endif %}"
)}
)
public class BoolFilter implements Filter {
   public String getName() {
      return "bool";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      if (var == null) {
         return false;
      } else {
         String str = var.toString();
         return str.equals("1") ? Boolean.TRUE : BooleanUtils.toBoolean(str);
      }
   }
}
