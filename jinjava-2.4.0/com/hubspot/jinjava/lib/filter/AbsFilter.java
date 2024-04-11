package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.math.BigDecimal;
import java.math.BigInteger;

@JinjavaDoc(
   value = "Return the absolute value of the argument.",
   params = {@JinjavaParam(
   value = "number",
   type = "number",
   desc = "The number that you want to get the absolute value of"
)},
   snippets = {@JinjavaSnippet(
   code = "{% set my_number = -53 %}\n{{ my_number|abs }}"
)}
)
public class AbsFilter implements Filter {
   public Object filter(Object object, JinjavaInterpreter interpreter, String... arg) {
      if (object instanceof Integer) {
         return Math.abs((Integer)object);
      } else if (object instanceof Float) {
         return Math.abs((Float)object);
      } else if (object instanceof Long) {
         return Math.abs((Long)object);
      } else if (object instanceof Short) {
         return Math.abs((Short)object);
      } else if (object instanceof Double) {
         return Math.abs((Double)object);
      } else if (object instanceof BigDecimal) {
         return ((BigDecimal)object).abs();
      } else if (object instanceof BigInteger) {
         return ((BigInteger)object).abs();
      } else if (object instanceof Byte) {
         return Math.abs((Byte)object);
      } else if (object instanceof String) {
         try {
            return (new BigDecimal((String)object)).abs();
         } catch (Exception var5) {
            throw new InterpretException(object + " can't be handled by abs filter", var5);
         }
      } else {
         return object;
      }
   }

   public String getName() {
      return "abs";
   }
}
