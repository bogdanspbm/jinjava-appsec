package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;

@JinjavaDoc(
   value = "Evaluates to true if the value is divisible by the given number",
   params = {@JinjavaParam(
   value = "value",
   type = "number",
   desc = "The value to be divided"
), @JinjavaParam(
   value = "divisor",
   type = "number",
   desc = "The divisor to check if the value is divisible by"
)},
   snippets = {@JinjavaSnippet(
   desc = "This example is an alternative to using the is divisibleby expression test",
   code = "{% set num = 10 %}\n{% if num|divisible(2) %}\n    The number is divisble by 2\n{% endif %}"
)}
)
public class DivisibleFilter implements Filter {
   public Object filter(Object object, JinjavaInterpreter interpreter, String... arg) {
      if (object == null) {
         return false;
      } else {
         if (object instanceof Number) {
            if (arg.length != 1) {
               throw new InterpretException("filter divisible expects 1 arg >>> " + arg.length);
            }

            long factor = Long.parseLong(arg[0]);
            long value = ((Number)object).longValue();
            if (value % factor == 0L) {
               return true;
            }
         }

         return false;
      }
   }

   public String getName() {
      return "divisible";
   }
}
