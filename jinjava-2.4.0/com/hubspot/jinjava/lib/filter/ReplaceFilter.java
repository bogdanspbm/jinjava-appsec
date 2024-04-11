package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

@JinjavaDoc(
   value = "Return a copy of the value with all occurrences of a substring replaced with a new one. The first argument is the substring that should be replaced, the second is the replacement string. If the optional third argument count is given, only the first count occurrences are replaced",
   params = {@JinjavaParam(
   value = "s",
   desc = "Base string to find and replace within"
), @JinjavaParam(
   value = "old",
   desc = "The old substring that you want to match and replace"
), @JinjavaParam(
   value = "new",
   desc = "The new string that you replace the matched substring"
), @JinjavaParam(
   value = "count",
   type = "number",
   desc = "Replace only the first N occurrences"
)},
   snippets = {@JinjavaSnippet(
   code = "{{ \"Hello World\"|replace(\"Hello\", \"Goodbye\") }}",
   output = "Goodbye World"
), @JinjavaSnippet(
   code = "{{ \"aaaaargh\"|replace(\"a\", \"d'oh, \", 2) }}",
   output = "d'oh, d'oh, aaargh"
)}
)
public class ReplaceFilter implements Filter {
   public String getName() {
      return "replace";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      if (var == null) {
         return null;
      } else if (args.length < 2) {
         throw new InterpretException("filter " + this.getName() + " requires two string args");
      } else {
         String s = (String)var;
         String toReplace = args[0];
         String replaceWith = args[1];
         Integer count = null;
         if (args.length > 2) {
            count = NumberUtils.createInteger(args[2]);
         }

         return count == null ? StringUtils.replace(s, toReplace, replaceWith) : StringUtils.replace(s, toReplace, replaceWith, count);
      }
   }
}
