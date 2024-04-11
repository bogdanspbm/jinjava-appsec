package com.hubspot.jinjava.lib.fn;

import com.google.common.collect.ImmutableMap;
import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.el.ext.AstDict;
import com.hubspot.jinjava.el.ext.AstList;
import com.hubspot.jinjava.el.ext.AstTuple;
import com.hubspot.jinjava.objects.date.PyishDate;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@JinjavaDoc("Get a string that describes the type of the object, similar to Python's type()")
public class TypeFunction {
   private static Map<Class<?>, String> CLASS_TYPE_TO_NAME = ImmutableMap.builder().put(AstDict.class, "dict").put(AstList.class, "list").put(AstTuple.class, "tuple").put(Boolean.class, "bool").put(PyishDate.class, "datetime").put(ZonedDateTime.class, "datetime").build();
   private static Map<Class<?>, String> ASSIGNABLE_TYPE_TO_NAME = ImmutableMap.builder().put(Boolean.class, "bool").put(Double.class, "float").put(Float.class, "float").put(Integer.class, "int").put(List.class, "list").put(Long.class, "long").put(Map.class, "dict").put(String.class, "str").build();

   public static String type(Object var) {
      if (var == null) {
         return "null";
      } else {
         Iterator var1 = CLASS_TYPE_TO_NAME.entrySet().iterator();

         Entry entry;
         do {
            if (!var1.hasNext()) {
               var1 = ASSIGNABLE_TYPE_TO_NAME.entrySet().iterator();

               do {
                  if (!var1.hasNext()) {
                     return "unknown";
                  }

                  entry = (Entry)var1.next();
               } while(!((Class)entry.getKey()).isAssignableFrom(var.getClass()));

               return (String)entry.getValue();
            }

            entry = (Entry)var1.next();
         } while(var.getClass() != entry.getKey());

         return (String)entry.getValue();
      }
   }
}
