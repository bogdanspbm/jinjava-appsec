package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.util.Logging;
import java.lang.reflect.Array;
import java.util.Collection;

@JinjavaDoc(
   value = "Reverse the object or return an iterator the iterates over it the other way round.",
   params = {@JinjavaParam(
   value = "value",
   type = "object",
   desc = "The sequence or dict to reverse the iteration order"
)},
   snippets = {@JinjavaSnippet(
   code = "{% set nums = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10] %}\n{% for num in nums|reverse %}\n    {{ num }}\n{% endfor %}"
)}
)
public class ReverseFilter implements Filter {
   public Object filter(Object object, JinjavaInterpreter interpreter, String... arg) {
      if (object == null) {
         return null;
      } else {
         int length;
         int i;
         if (object instanceof Collection) {
            Object[] origin = ((Collection)object).toArray();
            length = origin.length;
            Object[] res = new Object[length];
            --length;

            for(i = 0; i <= length; ++i) {
               res[i] = origin[length - i];
            }

            return res;
         } else if (object.getClass().isArray()) {
            int length = Array.getLength(object);
            Object[] res = new Object[length];
            --length;

            for(int i = 0; i <= length; ++i) {
               res[i] = Array.get(object, length - i);
            }

            return res;
         } else if (!(object instanceof String)) {
            Logging.ENGINE_LOG.warn("filter contain can't be applied to >>> " + object.getClass().getName());
            return object;
         } else {
            String origin = (String)object;
            length = origin.length();
            char[] res = new char[length];
            --length;

            for(i = 0; i <= length; ++i) {
               res[i] = origin.charAt(length - i);
            }

            return String.valueOf(res);
         }
      }
   }

   public String getName() {
      return "reverse";
   }
}
