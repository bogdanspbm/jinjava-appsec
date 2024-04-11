package com.hubspot.jinjava.lib.exptest;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.util.ForLoop;
import com.hubspot.jinjava.util.ObjectIterator;
import java.util.Objects;

public class IsContainingAllExpTest implements ExpTest {
   public boolean evaluate(Object var, JinjavaInterpreter interpreter, Object... args) {
      if (null != var && args.length != 0 && args[0] != null) {
         ForLoop loop = ObjectIterator.getLoop(args[0]);

         boolean matches;
         do {
            if (!loop.hasNext()) {
               return true;
            }

            Object matchValue = loop.next();
            ForLoop varLoop = ObjectIterator.getLoop(var);
            matches = false;

            while(varLoop.hasNext()) {
               if (Objects.equals(matchValue, varLoop.next())) {
                  matches = true;
                  break;
               }
            }
         } while(matches);

         return false;
      } else {
         return false;
      }
   }

   public String getName() {
      return "containingall";
   }
}
