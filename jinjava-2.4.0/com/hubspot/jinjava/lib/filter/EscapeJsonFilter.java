package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.util.Objects;
import org.apache.commons.lang3.StringEscapeUtils;

@JinjavaDoc(
   value = "Escapes strings so that they can be used as JSON values",
   params = {@JinjavaParam(
   value = "s",
   desc = "String to escape"
)},
   snippets = {@JinjavaSnippet(
   code = "{{String that contains JavaScript|escapejson}}"
)}
)
public class EscapeJsonFilter implements Filter {
   public String getName() {
      return "escapejson";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      return StringEscapeUtils.escapeJson(Objects.toString(var));
   }
}
