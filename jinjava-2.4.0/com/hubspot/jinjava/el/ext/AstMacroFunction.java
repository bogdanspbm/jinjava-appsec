package com.hubspot.jinjava.el.ext;

import com.google.common.collect.ImmutableMap;
import com.hubspot.jinjava.interpret.CallStack;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.interpret.MacroTagCycleException;
import com.hubspot.jinjava.interpret.TemplateError;
import com.hubspot.jinjava.interpret.errorcategory.BasicTemplateErrorCategory;
import com.hubspot.jinjava.lib.fn.MacroFunction;
import java.lang.reflect.InvocationTargetException;
import jinjava.de.odysseus.el.misc.LocalMessages;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.de.odysseus.el.tree.impl.ast.AstFunction;
import jinjava.de.odysseus.el.tree.impl.ast.AstParameters;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELException;

public class AstMacroFunction extends AstFunction {
   public AstMacroFunction(String name, int index, AstParameters params, boolean varargs) {
      super(name, index, params, varargs);
   }

   public Object eval(Bindings bindings, ELContext context) {
      JinjavaInterpreter interpreter = (JinjavaInterpreter)context.getELResolver().getValue(context, (Object)null, "____int3rpr3t3r____");
      MacroFunction macroFunction = interpreter.getContext().getGlobalMacro(this.getName());
      if (macroFunction != null) {
         CallStack macroStack = interpreter.getContext().getMacroStack();
         if (!macroFunction.isCaller()) {
            try {
               if (interpreter.getConfig().isEnableRecursiveMacroCalls()) {
                  macroStack.pushWithoutCycleCheck(this.getName());
               } else {
                  macroStack.push(this.getName(), -1, -1);
               }
            } catch (MacroTagCycleException var15) {
               interpreter.addError(new TemplateError(TemplateError.ErrorType.WARNING, TemplateError.ErrorReason.EXCEPTION, TemplateError.ErrorItem.TAG, "Cycle detected for macro '" + this.getName() + "'", (String)null, var15.getLineNumber(), var15.getStartPosition(), var15, BasicTemplateErrorCategory.CYCLE_DETECTED, ImmutableMap.of("name", this.getName())));
               return "";
            }
         }

         Object var6;
         try {
            var6 = super.invoke(bindings, context, macroFunction, AbstractCallableMethod.EVAL_METHOD);
         } catch (IllegalAccessException var12) {
            throw new ELException(LocalMessages.get("error.function.access", this.getName()), var12);
         } catch (InvocationTargetException var13) {
            throw new ELException(LocalMessages.get("error.function.invocation", this.getName()), var13.getCause());
         } finally {
            macroStack.pop();
         }

         return var6;
      } else {
         return super.eval(bindings, context);
      }
   }
}
