package jinjava.de.odysseus.el.tree.impl.ast;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.de.odysseus.el.tree.ExpressionNode;
import jinjava.javax.el.ELContext;

public abstract class AstNode implements ExpressionNode {
   public final Object getValue(Bindings bindings, ELContext context, Class<?> type) {
      Object value = this.eval(bindings, context);
      if (type != null) {
         value = bindings.convert(value, type);
      }

      return value;
   }

   public abstract void appendStructure(StringBuilder var1, Bindings var2);

   public abstract Object eval(Bindings var1, ELContext var2);

   public final String getStructuralId(Bindings bindings) {
      StringBuilder builder = new StringBuilder();
      this.appendStructure(builder, bindings);
      return builder.toString();
   }

   private static Method findPublicAccessibleMethod(Method method) {
      if (method != null && Modifier.isPublic(method.getModifiers())) {
         if (!method.isAccessible() && !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            Class[] arr$ = method.getDeclaringClass().getInterfaces();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Class<?> cls = arr$[i$];
               Method mth = null;

               try {
                  mth = findPublicAccessibleMethod(cls.getMethod(method.getName(), method.getParameterTypes()));
                  if (mth != null) {
                     return mth;
                  }
               } catch (NoSuchMethodException var8) {
               }
            }

            Class<?> cls = method.getDeclaringClass().getSuperclass();
            if (cls != null) {
               Method mth = null;

               try {
                  mth = findPublicAccessibleMethod(cls.getMethod(method.getName(), method.getParameterTypes()));
                  if (mth != null) {
                     return mth;
                  }
               } catch (NoSuchMethodException var7) {
               }
            }

            return null;
         } else {
            return method;
         }
      } else {
         return null;
      }
   }

   protected Method findAccessibleMethod(Method method) {
      Method result = findPublicAccessibleMethod(method);
      if (result == null && method != null && Modifier.isPublic(method.getModifiers())) {
         result = method;

         try {
            method.setAccessible(true);
         } catch (SecurityException var4) {
            result = null;
         }
      }

      return result;
   }
}
