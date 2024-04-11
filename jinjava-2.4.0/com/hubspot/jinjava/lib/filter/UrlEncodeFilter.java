package com.hubspot.jinjava.lib.filter;

import com.google.common.base.Throwables;
import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Escape strings for use in URLs (uses UTF-8 encoding). It accepts both dictionaries and regular strings as well as pairwise iterables.",
   snippets = {@JinjavaSnippet(
   code = "{{ \"Escape & URL encode this string\"|urlencode }}"
)}
)
public class UrlEncodeFilter implements Filter {
   public String getName() {
      return "urlencode";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      if (var == null && args.length == 0) {
         return "";
      } else if (var == null) {
         return this.urlEncode(args[0]);
      } else if (!Map.class.isAssignableFrom(var.getClass())) {
         return this.urlEncode(var.toString());
      } else {
         Map<Object, Object> dict = (Map)var;
         List<String> paramPairs = new ArrayList();
         Iterator var6 = dict.entrySet().iterator();

         while(var6.hasNext()) {
            Entry<Object, Object> param = (Entry)var6.next();
            StringBuilder paramPair = new StringBuilder();
            paramPair.append(this.urlEncode(Objects.toString(param.getKey())));
            paramPair.append("=");
            paramPair.append(this.urlEncode(Objects.toString(param.getValue())));
            paramPairs.add(paramPair.toString());
         }

         return StringUtils.join(paramPairs, "&");
      }
   }

   private String urlEncode(String s) {
      try {
         return URLEncoder.encode(s, "UTF-8");
      } catch (UnsupportedEncodingException var3) {
         throw Throwables.propagate(var3);
      }
   }
}
