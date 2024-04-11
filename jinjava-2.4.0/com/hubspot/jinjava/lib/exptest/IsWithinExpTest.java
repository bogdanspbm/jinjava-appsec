package com.hubspot.jinjava.lib.exptest;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.util.ForLoop;
import com.hubspot.jinjava.util.ObjectIterator;
import java.util.Objects;

public class IsWithinExpTest implements ExpTest {
   public boolean evaluate(Object var, JinjavaInterpreter interpreter, Object... args) {
      if (args != null && args.length != 0) {
         ForLoop loop = ObjectIterator.getLoop(args[0]);

         do {
            if (!loop.hasNext()) {
               return false;
            }
         } while(!Objects.equals(loop.next(), var));

         return true;
      } else {
         return false;
      }
   }

   public String getName() {
      return "within";
   }
}
