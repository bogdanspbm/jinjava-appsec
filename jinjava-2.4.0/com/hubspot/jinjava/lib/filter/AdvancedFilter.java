package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.Importable;
import java.util.HashMap;
import java.util.Map;

public interface AdvancedFilter extends Importable, Filter {
   Object filter(Object var1, JinjavaInterpreter var2, Object[] var3, Map<String, Object> var4);

   default Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      return this.filter(var, interpreter, (Object[])args, new HashMap());
   }
}
