package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.math.BigDecimal;
import java.math.BigInteger;

@JinjavaDoc(
   value = "Multiplies the current object with the given multiplier",
   params = {@JinjavaParam(
   value = "value",
   type = "number",
   desc = "Base number to be multiplied"
), @JinjavaParam(
   value = "multiplier",
   type = "number",
   desc = "The multiplier"
)},
   snippets = {@JinjavaSnippet(
   code = "{% set n = 20 %}\n{{ n|multiply(3) }}"
)}
)
public class MultiplyFilter implements Filter {
   public Object filter(Object object, JinjavaInterpreter interpreter, String... arg) {
      if (arg.length != 1) {
         throw new InterpretException("filter multiply expects 1 arg >>> " + arg.length);
      } else {
         String toMul = arg[0];
         Number num = new BigDecimal(toMul);
         if (object instanceof Integer) {
            return num.intValue() * (Integer)object;
         } else if (object instanceof Float) {
            return 0.0D + (double)(num.floatValue() * (Float)object);
         } else if (object instanceof Long) {
            return num.longValue() * (Long)object;
         } else if (object instanceof Short) {
            return 0 + num.shortValue() * (Short)object;
         } else if (object instanceof Double) {
            return num.doubleValue() * (Double)object;
         } else if (object instanceof BigDecimal) {
            return ((BigDecimal)object).multiply(BigDecimal.valueOf(num.doubleValue()));
         } else if (object instanceof BigInteger) {
            return ((BigInteger)object).multiply(BigInteger.valueOf(num.longValue()));
         } else if (object instanceof Byte) {
            return num.byteValue() * (Byte)object;
         } else if (object instanceof String) {
            try {
               return num.doubleValue() * Double.parseDouble((String)object);
            } catch (Exception var7) {
               throw new InterpretException(object + " can't be dealed with multiply filter", var7);
            }
         } else {
            return object;
         }
      }
   }

   public String getName() {
      return "multiply";
   }
}
