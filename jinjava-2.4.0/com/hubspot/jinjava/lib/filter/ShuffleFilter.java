package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@JinjavaDoc(
   value = "Randomly shuffle a given list, returning a new list with all of the items of the original list in a random order",
   snippets = {@JinjavaSnippet(
   desc = "The example below is a standard blog loop that's order is randomized on page load",
   code = "{% for content in contents|shuffle %}\n    <div class=\"post-item\">Markup of each post</div>\n{% endfor %}"
)}
)
public class ShuffleFilter implements Filter {
   public String getName() {
      return "shuffle";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      if (var instanceof Collection) {
         List<?> list = new ArrayList((Collection)var);
         Collections.shuffle(list, interpreter.getRandom());
         return list;
      } else {
         return var;
      }
   }
}
