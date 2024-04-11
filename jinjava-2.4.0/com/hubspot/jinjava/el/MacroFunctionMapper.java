package com.hubspot.jinjava.el;

import com.hubspot.jinjava.el.ext.AbstractCallableMethod;
import com.hubspot.jinjava.interpret.Context;
import com.hubspot.jinjava.interpret.DisabledException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.fn.MacroFunction;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import jinjava.javax.el.FunctionMapper;

public class MacroFunctionMapper extends FunctionMapper {
   private Map<String, Method> map = Collections.emptyMap();

   private static String buildFunctionName(String prefix, String name) {
      return prefix + ":" + name;
   }

   public Method resolveFunction(String prefix, String localName) {
      Context context = JinjavaInterpreter.getCurrent().getContext();
      MacroFunction macroFunction = context.getGlobalMacro(localName);
      if (macroFunction != null) {
         return AbstractCallableMethod.EVAL_METHOD;
      } else {
         String functionName = buildFunctionName(prefix, localName);
         if (context.isFunctionDisabled(functionName)) {
            throw new DisabledException(functionName);
         } else {
            if (this.map.containsKey(functionName)) {
               context.addResolvedFunction(functionName);
            }

            return (Method)this.map.get(functionName);
         }
      }
   }

   public void setFunction(String prefix, String localName, Method method) {
      if (this.map.isEmpty()) {
         this.map = new HashMap();
      }

      this.map.put(buildFunctionName(prefix, localName), method);
   }
}
