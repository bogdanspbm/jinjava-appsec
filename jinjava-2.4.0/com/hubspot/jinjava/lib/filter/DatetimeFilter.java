package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;

@JinjavaDoc(
   value = "",
   aliasOf = "datetimeformat"
)
public class DatetimeFilter extends DateTimeFormatFilter {
   public Object filter(Object object, JinjavaInterpreter interpreter, String... arg) {
      return super.filter(object, interpreter, arg);
   }

   public String getName() {
      return "date";
   }
}
