package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.text.NumberFormat;
import java.util.Locale;
import org.apache.commons.lang3.math.NumberUtils;

@JinjavaDoc(
   value = "Convert the value into an integer.",
   params = {@JinjavaParam(
   value = "value",
   desc = "The value to convert to an integer"
), @JinjavaParam(
   value = "default",
   type = "number",
   defaultValue = "0",
   desc = "Value to return if the conversion fails"
)},
   snippets = {@JinjavaSnippet(
   desc = "This example converts a text field string value to a integer",
   code = "{% text \"my_text\" value='25', export_to_template_context=True %}\n{% widget_data.my_text.value|int + 28 %}"
)}
)
public class IntFilter implements Filter {
   public String getName() {
      return "int";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      int defaultVal = 0;
      if (args.length > 0) {
         defaultVal = NumberUtils.toInt(args[0], 0);
      }

      if (var == null) {
         return defaultVal;
      } else if (Number.class.isAssignableFrom(var.getClass())) {
         return ((Number)var).intValue();
      } else {
         Locale locale = interpreter.getConfig().getLocale();
         NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
         numberFormat.setParseIntegerOnly(true);

         int result;
         try {
            result = numberFormat.parse(var.toString()).intValue();
         } catch (Exception var9) {
            result = defaultVal;
         }

         return result;
      }
   }
}
