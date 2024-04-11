package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Removes a string from the value from another string",
   params = {@JinjavaParam(
   value = "value",
   desc = "The original string"
), @JinjavaParam(
   value = "to_remove",
   desc = "String to remove from the original string"
)},
   snippets = {@JinjavaSnippet(
   code = "{% set my_string = \"Hello world.\" %}\n{{ my_string|cut(' world') }}"
)}
)
public class CutFilter implements Filter {
   public Object filter(Object object, JinjavaInterpreter interpreter, String... arg) {
      if (arg.length != 1) {
         throw new InterpretException("filter cut expects 1 arg >>> " + arg.length);
      } else {
         String cutee = arg[0];
         String origin = Objects.toString(object, "");
         return StringUtils.replace(origin, cutee, "");
      }
   }

   public String getName() {
      return "cut";
   }
}
