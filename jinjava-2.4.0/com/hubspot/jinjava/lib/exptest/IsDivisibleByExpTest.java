package com.hubspot.jinjava.lib.exptest;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;

@JinjavaDoc(
   value = "Check if a variable is divisible by a number",
   params = {@JinjavaParam(
   value = "num",
   type = "number",
   desc = "The number to check whether a number is divisble by"
)},
   snippets = {@JinjavaSnippet(
   code = "{% if variable is divisbleby 5 %}\n   <!--code to render if variable can be divided by 5-->\n{% else %}\n   <!--code to render if variable cannot be divided by 5-->\n{% endif %}"
)}
)
public class IsDivisibleByExpTest implements ExpTest {
   public String getName() {
      return "divisibleby";
   }

   public boolean evaluate(Object var, JinjavaInterpreter interpreter, Object... args) {
      if (var == null) {
         return false;
      } else if (!Number.class.isAssignableFrom(var.getClass())) {
         return false;
      } else if (args.length != 0 && args[0] != null && Number.class.isAssignableFrom(args[0].getClass())) {
         return ((Number)var).intValue() % ((Number)args[0]).intValue() == 0;
      } else {
         throw new InterpretException(this.getName() + " test requires a numeric argument");
      }
   }
}
