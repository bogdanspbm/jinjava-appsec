package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.math.BigDecimal;
import java.math.BigInteger;

@JinjavaDoc(
   value = "Divides the current value by a divisor",
   params = {@JinjavaParam(
   value = "value",
   type = "number",
   desc = "The numerator to be divided"
), @JinjavaParam(
   value = "divisor",
   type = "number",
   desc = "The divisor to divide the value"
)},
   snippets = {@JinjavaSnippet(
   code = "{% set numerator = 106 %}\n{% numerator|divide(2) %}"
)}
)
public class DivideFilter implements Filter {
   public Object filter(Object object, JinjavaInterpreter interpreter, String... arg) {
      if (arg.length != 1) {
         throw new InterpretException("filter multiply expects 1 arg >>> " + arg.length);
      } else {
         String toMul = arg[0];
         if (toMul != null) {
            Number num = new BigDecimal(toMul);
            if (object instanceof Integer) {
               return (Integer)object / num.intValue();
            } else if (object instanceof Float) {
               return (Float)object / num.floatValue();
            } else if (object instanceof Long) {
               return (Long)object / num.longValue();
            } else if (object instanceof Short) {
               return (Short)object / num.shortValue();
            } else if (object instanceof Double) {
               return (Double)object / num.doubleValue();
            } else if (object instanceof BigDecimal) {
               return ((BigDecimal)object).divide(BigDecimal.valueOf(num.doubleValue()));
            } else if (object instanceof BigInteger) {
               return ((BigInteger)object).divide(BigInteger.valueOf(num.longValue()));
            } else if (object instanceof Byte) {
               return (Byte)object / num.byteValue();
            } else if (object instanceof String) {
               try {
                  return Double.valueOf((String)object) / num.doubleValue();
               } catch (Exception var7) {
                  throw new InterpretException(object + " can't be dealed with multiply filter", var7);
               }
            } else {
               return object;
            }
         } else {
            return object;
         }
      }
   }

   public String getName() {
      return "divide";
   }
}
