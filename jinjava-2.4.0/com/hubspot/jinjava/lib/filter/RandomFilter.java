package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@JinjavaDoc(
   value = "Return a random item from the sequence.",
   params = {@JinjavaParam(
   value = "seq",
   type = "sequence",
   desc = "Sequence to return a random item from"
)},
   snippets = {@JinjavaSnippet(
   desc = "The example below is a standard blog loop that returns a single random post.",
   code = "{% for content in contents|random %}\n    <div class=\"post-item\">Post item markup</div>{% endfor %}"
)}
)
public class RandomFilter implements Filter {
   public Object filter(Object object, JinjavaInterpreter interpreter, String... arg) {
      if (object == null) {
         return null;
      } else {
         Iterator it;
         int size;
         int var7;
         if (object instanceof Collection) {
            Collection<?> clt = (Collection)object;
            it = clt.iterator();
            size = clt.size();
            if (size == 0) {
               return null;
            } else {
               var7 = interpreter.getRandom().nextInt(size);

               while(var7-- > 0) {
                  it.next();
               }

               return it.next();
            }
         } else if (object.getClass().isArray()) {
            int size = Array.getLength(object);
            if (size == 0) {
               return null;
            } else {
               int index = interpreter.getRandom().nextInt(size);
               return Array.get(object, index);
            }
         } else if (!(object instanceof Map)) {
            if (object instanceof Number) {
               return interpreter.getRandom().nextInt(((Number)object).intValue());
            } else if (object instanceof String) {
               try {
                  return interpreter.getRandom().nextInt((new BigDecimal((String)object)).intValue());
               } catch (Exception var8) {
                  return 0;
               }
            } else {
               return object;
            }
         } else {
            Map<?, ?> map = (Map)object;
            it = map.values().iterator();
            size = map.size();
            if (size == 0) {
               return null;
            } else {
               var7 = interpreter.getRandom().nextInt(size);

               while(var7-- > 0) {
                  it.next();
               }

               return it.next();
            }
         }
      }
   }

   public String getName() {
      return "random";
   }
}
