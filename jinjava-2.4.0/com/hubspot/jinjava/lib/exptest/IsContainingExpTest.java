package com.hubspot.jinjava.lib.exptest;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.util.ForLoop;
import com.hubspot.jinjava.util.ObjectIterator;
import java.util.Objects;

public class IsContainingExpTest implements ExpTest {
   public boolean evaluate(Object var, JinjavaInterpreter interpreter, Object... args) {
      if (null != var && args.length != 0) {
         ForLoop loop = ObjectIterator.getLoop(var);

         do {
            if (!loop.hasNext()) {
               return false;
            }
         } while(!Objects.equals(loop.next(), args[0]));

         return true;
      } else {
         return false;
      }
   }

   public String getName() {
      return "containing";
   }
}
