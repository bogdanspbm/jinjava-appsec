package com.hubspot.jinjava.lib.fn;

import com.hubspot.jinjava.el.ext.AbstractCallableMethod;
import com.hubspot.jinjava.interpret.Context;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.tree.Node;
import com.hubspot.jinjava.util.LengthLimitingStringBuilder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MacroFunction extends AbstractCallableMethod {
   private final List<Node> content;
   private final boolean catchKwargs;
   private final boolean catchVarargs;
   private final boolean caller;
   private final Context localContextScope;

   public MacroFunction(List<Node> content, String name, LinkedHashMap<String, Object> argNamesWithDefaults, boolean catchKwargs, boolean catchVarargs, boolean caller, Context localContextScope) {
      super(name, argNamesWithDefaults);
      this.content = content;
      this.catchKwargs = catchKwargs;
      this.catchVarargs = catchVarargs;
      this.caller = caller;
      this.localContextScope = localContextScope;
   }

   public Object doEvaluate(Map<String, Object> argMap, Map<String, Object> kwargMap, List<Object> varArgs) {
      JinjavaInterpreter interpreter = JinjavaInterpreter.getCurrent();
      JinjavaInterpreter.InterpreterScopeClosable c = interpreter.enterScope();
      Throwable var6 = null;

      try {
         Iterator var7 = this.localContextScope.getScope().entrySet().iterator();

         Entry argEntry;
         while(var7.hasNext()) {
            argEntry = (Entry)var7.next();
            if (argEntry.getValue() instanceof MacroFunction) {
               interpreter.getContext().addGlobalMacro((MacroFunction)argEntry.getValue());
            } else {
               interpreter.getContext().put(argEntry.getKey(), argEntry.getValue());
            }
         }

         var7 = argMap.entrySet().iterator();

         while(var7.hasNext()) {
            argEntry = (Entry)var7.next();
            interpreter.getContext().put(argEntry.getKey(), argEntry.getValue());
         }

         interpreter.getContext().put("kwargs", argMap);
         interpreter.getContext().put("varargs", varArgs);
         LengthLimitingStringBuilder result = new LengthLimitingStringBuilder(interpreter.getConfig().getMaxOutputSize());
         Iterator var20 = this.content.iterator();

         while(var20.hasNext()) {
            Node node = (Node)var20.next();
            result.append((Object)node.render(interpreter));
         }

         String var21 = result.toString();
         return var21;
      } catch (Throwable var17) {
         var6 = var17;
         throw var17;
      } finally {
         if (c != null) {
            if (var6 != null) {
               try {
                  c.close();
               } catch (Throwable var16) {
                  var6.addSuppressed(var16);
               }
            } else {
               c.close();
            }
         }

      }
   }

   public boolean isCatchKwargs() {
      return this.catchKwargs;
   }

   public boolean isCatchVarargs() {
      return this.catchVarargs;
   }

   public boolean isCaller() {
      return this.caller;
   }
}
