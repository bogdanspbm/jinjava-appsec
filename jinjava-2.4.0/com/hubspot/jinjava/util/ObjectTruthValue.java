package com.hubspot.jinjava.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public final class ObjectTruthValue {
   private ObjectTruthValue() {
   }

   public static boolean evaluate(Object object) {
      if (object == null) {
         return false;
      } else if (object instanceof Boolean) {
         Boolean b = (Boolean)object;
         return b;
      } else if (object instanceof Number) {
         return ((Number)object).intValue() != 0;
      } else if (!(object instanceof String)) {
         if (object.getClass().isArray()) {
            return Array.getLength(object) != 0;
         } else if (object instanceof Collection) {
            return ((Collection)object).size() != 0;
         } else if (object instanceof Map) {
            return ((Map)object).size() != 0;
         } else {
            return true;
         }
      } else {
         return !"".equals(object) && !"false".equalsIgnoreCase((String)object);
      }
   }
}
