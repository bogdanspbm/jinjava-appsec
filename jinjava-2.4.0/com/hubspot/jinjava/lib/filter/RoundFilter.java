package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import org.apache.commons.lang3.math.NumberUtils;

@JinjavaDoc(
   value = "Round the number to a given precision.",
   params = {@JinjavaParam(
   value = "value",
   type = "number",
   desc = "The number to round"
), @JinjavaParam(
   value = "precision",
   type = "number",
   defaultValue = "0",
   desc = "Specifies the precision of rounding"
), @JinjavaParam(
   value = "method",
   type = "enum common|ceil|floor",
   defaultValue = "common",
   desc = "Method of rounding: 'common' rounds either up or down, 'ceil' always rounds up, and 'floor' always rounds down."
)},
   snippets = {@JinjavaSnippet(
   code = "{{ 42.55|round }}",
   output = "43.0",
   desc = "Note that even if rounded to 0 precision, a float is returned."
), @JinjavaSnippet(
   code = "{{ 42.55|round(1, 'floor') }}",
   output = "42.5"
), @JinjavaSnippet(
   code = "{{ 42.55|round|int }}",
   output = "43",
   desc = "If you need a real integer, pipe it through int"
)}
)
public class RoundFilter implements Filter {
   public String getName() {
      return "round";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      BigDecimal result = BigDecimal.ZERO;

      try {
         result = new BigDecimal(Objects.toString(var));
      } catch (NumberFormatException var10) {
      }

      int precision = 0;
      if (args.length > 0) {
         precision = NumberUtils.toInt(args[0]);
      }

      String method = "common";
      if (args.length > 1) {
         method = args[1];
      }

      byte var8 = -1;
      switch(method.hashCode()) {
      case -1354814997:
         if (method.equals("common")) {
            var8 = 2;
         }
         break;
      case 3049733:
         if (method.equals("ceil")) {
            var8 = 0;
         }
         break;
      case 97526796:
         if (method.equals("floor")) {
            var8 = 1;
         }
      }

      RoundingMode roundingMode;
      switch(var8) {
      case 0:
         roundingMode = RoundingMode.CEILING;
         break;
      case 1:
         roundingMode = RoundingMode.FLOOR;
         break;
      case 2:
      default:
         roundingMode = RoundingMode.HALF_UP;
      }

      return result.setScale(precision, roundingMode);
   }
}
