package com.hubspot.jinjava.el.ext;

import com.google.common.base.Throwables;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AbstractCallableMethod {
   public static final Method EVAL_METHOD;
   private final String name;
   private final LinkedHashMap<String, Object> argNamesWithDefaults;

   public AbstractCallableMethod(String name, LinkedHashMap<String, Object> argNamesWithDefaults) {
      this.name = name;
      this.argNamesWithDefaults = argNamesWithDefaults;
   }

   public Object evaluate(Object... args) {
      Map<String, Object> argMap = new LinkedHashMap(this.argNamesWithDefaults);
      Map<String, Object> kwargMap = new LinkedHashMap();
      List<Object> varArgs = new ArrayList();
      int argPos = 0;
      Iterator var6 = argMap.entrySet().iterator();

      while(var6.hasNext()) {
         Entry<String, Object> argEntry = (Entry)var6.next();
         if (argPos >= args.length) {
            break;
         }

         Object arg = args[argPos++];
         if (arg instanceof NamedParameter) {
            --argPos;
            break;
         }

         argEntry.setValue(arg);
      }

      for(int i = argPos; i < args.length; ++i) {
         Object arg = args[i];
         if (arg instanceof NamedParameter) {
            NamedParameter param = (NamedParameter)arg;
            if (argMap.containsKey(param.getName())) {
               argMap.put(param.getName(), param.getValue());
            } else {
               kwargMap.put(param.getName(), param.getValue());
            }
         } else {
            varArgs.add(arg);
         }
      }

      return this.doEvaluate(argMap, kwargMap, varArgs);
   }

   public abstract Object doEvaluate(Map<String, Object> var1, Map<String, Object> var2, List<Object> var3);

   public String getName() {
      return this.name;
   }

   public List<String> getArguments() {
      return new ArrayList(this.argNamesWithDefaults.keySet());
   }

   public Map<String, Object> getDefaults() {
      return this.argNamesWithDefaults;
   }

   static {
      try {
         EVAL_METHOD = AbstractCallableMethod.class.getMethod("evaluate", Object[].class);
      } catch (Exception var1) {
         throw Throwables.propagate(var1);
      }
   }
}
