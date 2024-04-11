package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Strip leading and trailing whitespace.",
   snippets = {@JinjavaSnippet(
   code = "{{ \" remove whitespace \"|trim }}"
)}
)
public class TrimFilter implements Filter {
   public String getName() {
      return "trim";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      return StringUtils.trim(Objects.toString(var));
   }
}
