package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.fn.Functions;

@JinjavaDoc(
   value = "Formats a date object",
   params = {@JinjavaParam(
   value = "value",
   defaultValue = "current time",
   desc = "The date variable or UNIX timestamp to format"
), @JinjavaParam(
   value = "format",
   defaultValue = "%H:%M / %d-%m-%Y",
   desc = "The format of the date determined by the directives added to this parameter"
)},
   snippets = {@JinjavaSnippet(
   code = "{% content.updated|datetimeformat('%B %e, %Y') %}"
), @JinjavaSnippet(
   code = "{% content.updated|datetimeformat('%a %A %w %d %e %b %B %m %y %Y %H %I %k %l %p %M %S %f %z %Z %j %U %W %c %x %X %%') %}"
)}
)
public class DateTimeFormatFilter implements Filter {
   public String getName() {
      return "datetimeformat";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      return args.length > 0 ? Functions.dateTimeFormat(var, args[0]) : Functions.dateTimeFormat(var);
   }
}
