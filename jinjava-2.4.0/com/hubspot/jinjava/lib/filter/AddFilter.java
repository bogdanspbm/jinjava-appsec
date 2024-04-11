package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.math.BigDecimal;
import java.util.Objects;

@JinjavaDoc(
   value = "adds a number to the existing value",
   params = {@JinjavaParam(
   value = "number",
   type = "number",
   desc = "Number or numeric variable to add to"
), @JinjavaParam(
   value = "addend",
   type = "number",
   desc = "The number added to the base number"
)},
   snippets = {@JinjavaSnippet(
   code = "{% set my_num = 40 %} \n{{ my_num|add(13) }}"
)}
)
public class AddFilter implements Filter {
   public Object filter(Object object, JinjavaInterpreter interpreter, String... arg) {
      if (arg.length != 1) {
         throw new InterpretException("filter add expects 1 arg >>> " + arg.length);
      } else {
         try {
            BigDecimal base = new BigDecimal(Objects.toString(object));
            BigDecimal addend = new BigDecimal(Objects.toString(arg[0]));
            return base.add(addend);
         } catch (Exception var6) {
            throw new InterpretException("filter add error", var6);
         }
      }
   }

   public String getName() {
      return "add";
   }
}
