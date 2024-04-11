package com.hubspot.jinjava.lib.exptest;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Return true if the given string is all lowercased",
   snippets = {@JinjavaSnippet(
   code = "{% if variable is lower %}\n   <!--code to render if variable value is lowercased-->\n{% endif %}"
)}
)
public class IsLowerExpTest implements ExpTest {
   public String getName() {
      return "lower";
   }

   public boolean evaluate(Object var, JinjavaInterpreter interpreter, Object... args) {
      return var != null && var instanceof String ? StringUtils.isAllLowerCase((String)var) : false;
   }
}
