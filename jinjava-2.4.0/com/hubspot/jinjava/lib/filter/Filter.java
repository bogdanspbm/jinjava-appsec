package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.Importable;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.ArrayUtils;

public interface Filter extends Importable {
   Object filter(Object var1, JinjavaInterpreter var2, String... var3);

   default Object filter(Object var, JinjavaInterpreter interpreter, Object[] args, Map<String, Object> kwargs) {
      Object[] allArgs = ArrayUtils.addAll(args, kwargs.values().toArray());
      String[] stringArgs = (String[])Arrays.stream(allArgs).map((arg) -> {
         return Objects.toString(arg, (String)null);
      }).toArray((x$0) -> {
         return new String[x$0];
      });
      return this.filter(var, interpreter, stringArgs);
   }
}
