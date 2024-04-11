package com.hubspot.jinjava.lib.filter;

import com.google.common.collect.Lists;
import com.google.common.primitives.Chars;
import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.util.Collection;

@JinjavaDoc(
   value = "Convert the value into a list. If it was a string the returned list will be a list of characters.",
   params = {@JinjavaParam(
   value = "value",
   desc = "Value to add to a sequence"
)},
   snippets = {@JinjavaSnippet(
   code = "{% set one = 1 %}\n{% set two = 2 %}\n{% set three = 3 %}\n{% set list_num = one|list + two|list + three|list %}\n{{ list_num|list }}"
)}
)
public class ListFilter implements Filter {
   public String getName() {
      return "list";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      Object result;
      if (var instanceof String) {
         result = Chars.asList(((String)var).toCharArray());
      } else if (Collection.class.isAssignableFrom(var.getClass())) {
         result = Lists.newArrayList((Collection)var);
      } else {
         result = Lists.newArrayList(new Object[]{var});
      }

      return result;
   }
}
