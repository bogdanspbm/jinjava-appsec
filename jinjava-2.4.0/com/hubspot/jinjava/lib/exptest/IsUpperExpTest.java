package com.hubspot.jinjava.lib.exptest;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Return true if string is all uppercased",
   snippets = {@JinjavaSnippet(
   code = "{% if variable is upper %}\n    <!-- code to render if variable value is uppercased -->\n{% endif %}"
)}
)
public class IsUpperExpTest implements ExpTest {
   public String getName() {
      return "upper";
   }

   public boolean evaluate(Object var, JinjavaInterpreter interpreter, Object... args) {
      return var != null && var instanceof String ? StringUtils.isAllUpperCase((String)var) : false;
   }
}
