package com.hubspot.jinjava.el.ext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import jinjava.de.odysseus.el.misc.NumberOperations;
import jinjava.de.odysseus.el.misc.TypeConverter;
import jinjava.de.odysseus.el.tree.impl.ast.AstBinary;

public class AdditionOperator extends AstBinary.SimpleOperator {
   public static final AdditionOperator OP = new AdditionOperator();

   protected Object apply(TypeConverter converter, Object o1, Object o2) {
      if (o1 instanceof Collection) {
         List<Object> result = new ArrayList((Collection)o1);
         if (o2 instanceof Collection) {
            result.addAll((Collection)o2);
         } else {
            result.add(o2);
         }

         return result;
      } else if (o1 instanceof Map && o2 instanceof Map) {
         Map<Object, Object> result = new HashMap((Map)o1);
         result.putAll((Map)o2);
         return result;
      } else {
         return !(o1 instanceof String) && !(o2 instanceof String) ? NumberOperations.add(converter, o1, o2) : Objects.toString(o1).concat(Objects.toString(o2));
      }
   }
}
