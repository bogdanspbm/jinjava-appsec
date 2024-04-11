package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Create an HTML/XML attribute string based on the items in a dict.",
   params = {@JinjavaParam(
   value = "d",
   type = "dict",
   desc = "Dict to filter"
), @JinjavaParam(
   value = "autospace",
   type = "boolean",
   defaultValue = "True",
   desc = "Automatically prepend a space in front of the item"
)},
   snippets = {@JinjavaSnippet(
   code = "{% set html_attributes = {'class': 'bold', 'id': 'sidebar'} %}\n<div {{ html_attributes|xmlattr }}></div>"
)}
)
public class XmlAttrFilter implements Filter {
   public String getName() {
      return "xmlattr";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      if (var != null && Map.class.isAssignableFrom(var.getClass())) {
         Map<String, Object> dict = (Map)var;
         List<String> attrs = new ArrayList();
         Iterator var6 = dict.entrySet().iterator();

         while(var6.hasNext()) {
            Entry<String, Object> entry = (Entry)var6.next();
            attrs.add((String)entry.getKey() + "=\"" + StringEscapeUtils.escapeXml10(Objects.toString(entry.getValue(), "")) + "\"");
         }

         String space = " ";
         if (args.length > 0 && !BooleanUtils.toBoolean(args[0])) {
            space = "";
         }

         return space + StringUtils.join(attrs, "\n");
      } else {
         return var;
      }
   }
}
