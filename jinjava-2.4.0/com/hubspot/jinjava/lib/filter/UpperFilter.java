package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;

@JinjavaDoc(
   value = "Convert a value to uppercase",
   snippets = {@JinjavaSnippet(
   code = "{{ \"text to make uppercase\"|uppercase }}"
)}
)
public class UpperFilter implements Filter {
   public Object filter(Object object, JinjavaInterpreter interpreter, String... arg) {
      if (object instanceof String) {
         String value = (String)object;
         return value.toUpperCase();
      } else {
         return object;
      }
   }

   public String getName() {
      return "upper";
   }
}
