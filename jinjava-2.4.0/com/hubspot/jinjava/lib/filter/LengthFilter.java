package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@JinjavaDoc(
   value = "Return the number of items of a sequence or mapping",
   params = {@JinjavaParam(
   value = "object",
   desc = "The sequence to count"
)},
   snippets = {@JinjavaSnippet(
   code = "{% set services = ['Web design', 'SEO', 'Inbound Marketing', 'PPC'] %}\n{{ services|length }}"
)}
)
public class LengthFilter implements Filter {
   public Object filter(Object object, JinjavaInterpreter interpreter, String... arg) {
      if (null == object) {
         return 0;
      } else if (object instanceof Collection) {
         return ((Collection)object).size();
      } else if (object.getClass().isArray()) {
         return Array.getLength(object);
      } else if (object instanceof Map) {
         return ((Map)object).size();
      } else {
         Iterator it;
         int size;
         if (object instanceof Iterable) {
            it = ((Iterable)object).iterator();

            for(size = 0; it.hasNext(); ++size) {
               it.next();
            }

            return size;
         } else if (!(object instanceof Iterator)) {
            return object instanceof String ? ((String)object).length() : 0;
         } else {
            it = (Iterator)object;

            for(size = 0; it.hasNext(); ++size) {
               it.next();
            }

            return size;
         }
      }
   }

   public String getName() {
      return "length";
   }
}
