package jinjava.de.odysseus.el.tree.impl.ast;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import jinjava.de.odysseus.el.misc.LocalMessages;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.de.odysseus.el.tree.FunctionNode;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELException;

public class AstFunction extends AstRightValue implements FunctionNode {
   private final int index;
   private final String name;
   private final AstParameters params;
   private final boolean varargs;

   public AstFunction(String name, int index, AstParameters params) {
      this(name, index, params, false);
   }

   public AstFunction(String name, int index, AstParameters params, boolean varargs) {
      this.name = name;
      this.index = index;
      this.params = params;
      this.varargs = varargs;
   }

   protected Object invoke(Bindings bindings, ELContext context, Object base, Method method) throws InvocationTargetException, IllegalAccessException {
      Class<?>[] types = method.getParameterTypes();
      Object[] params = null;
      if (types.length > 0) {
         params = new Object[types.length];
         int varargIndex;
         Object param;
         if (this.varargs && method.isVarArgs()) {
            for(varargIndex = 0; varargIndex < params.length - 1; ++varargIndex) {
               param = this.getParam(varargIndex).eval(bindings, context);
               if (param != null || types[varargIndex].isPrimitive()) {
                  params[varargIndex] = bindings.convert(param, types[varargIndex]);
               }
            }

            varargIndex = types.length - 1;
            Class<?> varargType = types[varargIndex].getComponentType();
            int length = this.getParamCount() - varargIndex;
            Object array = null;
            if (length == 1) {
               Object param = this.getParam(varargIndex).eval(bindings, context);
               if (param != null && param.getClass().isArray()) {
                  if (types[varargIndex].isInstance(param)) {
                     array = param;
                  } else {
                     length = Array.getLength(param);
                     array = Array.newInstance(varargType, length);

                     for(int i = 0; i < length; ++i) {
                        Object elem = Array.get(param, i);
                        if (elem != null || varargType.isPrimitive()) {
                           Array.set(array, i, bindings.convert(elem, varargType));
                        }
                     }
                  }
               } else {
                  array = Array.newInstance(varargType, 1);
                  if (param != null || varargType.isPrimitive()) {
                     Array.set(array, 0, bindings.convert(param, varargType));
                  }
               }
            } else {
               array = Array.newInstance(varargType, length);

               for(int i = 0; i < length; ++i) {
                  Object param = this.getParam(varargIndex + i).eval(bindings, context);
                  if (param != null || varargType.isPrimitive()) {
                     Array.set(array, i, bindings.convert(param, varargType));
                  }
               }
            }

            params[varargIndex] = array;
         } else {
            for(varargIndex = 0; varargIndex < params.length; ++varargIndex) {
               param = this.getParam(varargIndex).eval(bindings, context);
               if (param != null || types[varargIndex].isPrimitive()) {
                  params[varargIndex] = bindings.convert(param, types[varargIndex]);
               }
            }
         }
      }

      return method.invoke(base, params);
   }

   public Object eval(Bindings bindings, ELContext context) {
      Method method = bindings.getFunction(this.index);

      try {
         return this.invoke(bindings, context, (Object)null, method);
      } catch (IllegalAccessException var5) {
         throw new ELException(LocalMessages.get("error.function.access", this.name), var5);
      } catch (InvocationTargetException var6) {
         throw new ELException(LocalMessages.get("error.function.invocation", this.name), var6.getCause());
      }
   }

   public String toString() {
      return this.name;
   }

   public void appendStructure(StringBuilder b, Bindings bindings) {
      b.append(bindings != null && bindings.isFunctionBound(this.index) ? "<fn>" : this.name);
      this.params.appendStructure(b, bindings);
   }

   public int getIndex() {
      return this.index;
   }

   public String getName() {
      return this.name;
   }

   public boolean isVarArgs() {
      return this.varargs;
   }

   public int getParamCount() {
      return this.params.getCardinality();
   }

   protected AstNode getParam(int i) {
      return this.params.getChild(i);
   }

   public int getCardinality() {
      return 1;
   }

   public AstNode getChild(int i) {
      return i == 0 ? this.params : null;
   }
}
