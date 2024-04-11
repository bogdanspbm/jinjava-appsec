package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;

@JinjavaDoc(
   value = "Convert a value to lowercase",
   params = {@JinjavaParam(
   value = "s",
   desc = "String to make lowercase"
)},
   snippets = {@JinjavaSnippet(
   code = "{{ \"Text to MAKE Lowercase\"|lowercase }}"
)}
)
public class LowerFilter implements Filter {
   public Object filter(Object object, JinjavaInterpreter interpreter, String... arg) {
      if (object instanceof String) {
         String value = (String)object;
         return value.toLowerCase();
      } else {
         return object;
      }
   }

   public String getName() {
      return "lower";
   }
}
