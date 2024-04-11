package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.util.Objects;

@JinjavaDoc(
   value = "Returns string value of object",
   snippets = {@JinjavaSnippet(
   code = "{% set number_to_string = 45 %}\n{{ number_to_string|string }}"
)}
)
public class StringFilter implements Filter {
   public String getName() {
      return "string";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      return Objects.toString(var);
   }
}
