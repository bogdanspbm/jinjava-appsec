package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.objects.date.PyishDate;
import com.hubspot.jinjava.util.Logging;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Pretty print a variable. Useful for debugging.",
   params = {@JinjavaParam(
   value = "value",
   type = "object",
   desc = "Object to Pretty Print"
)},
   snippets = {@JinjavaSnippet(
   code = "{% set this_var =\"Variable that I want to debug\" %}\n{{ this_var|pprint }}"
)}
)
public class PrettyPrintFilter implements Filter {
   public String getName() {
      return "pprint";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      if (var == null) {
         return "null";
      } else {
         String varStr = null;
         if (!(var instanceof String) && !(var instanceof Number) && !(var instanceof PyishDate) && !(var instanceof Iterable) && !(var instanceof Map)) {
            varStr = this.objPropsToString(var);
         } else {
            varStr = Objects.toString(var);
         }

         return StringEscapeUtils.escapeHtml4("{% raw %}(" + var.getClass().getSimpleName() + ": " + varStr + "){% endraw %}");
      }
   }

   private String objPropsToString(Object var) {
      LinkedList props = new LinkedList();

      try {
         BeanInfo beanInfo = Introspector.getBeanInfo(var.getClass());
         PropertyDescriptor[] var4 = beanInfo.getPropertyDescriptors();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            PropertyDescriptor pd = var4[var6];

            try {
               if (!pd.getPropertyType().equals(Class.class)) {
                  Method readMethod = pd.getReadMethod();
                  if (readMethod != null && !readMethod.getDeclaringClass().equals(Object.class)) {
                     props.add(pd.getName() + "=" + readMethod.invoke(var));
                  }
               }
            } catch (Exception var9) {
               Logging.ENGINE_LOG.error("Error reading bean value", var9);
            }
         }
      } catch (IntrospectionException var10) {
         Logging.ENGINE_LOG.error("Error inspecting bean", var10);
      }

      return '{' + StringUtils.join(props, ", ") + '}';
   }
}
