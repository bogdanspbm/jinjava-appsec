package jinjava.de.odysseus.el.tree.impl.ast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import jinjava.de.odysseus.el.misc.LocalMessages;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELException;
import jinjava.javax.el.MethodInfo;
import jinjava.javax.el.MethodNotFoundException;
import jinjava.javax.el.PropertyNotFoundException;
import jinjava.javax.el.ValueReference;

public abstract class AstProperty extends AstNode {
   protected final AstNode prefix;
   protected final boolean lvalue;
   protected final boolean strict;
   protected final boolean ignoreReturnType;

   public AstProperty(AstNode prefix, boolean lvalue, boolean strict) {
      this(prefix, lvalue, strict, false);
   }

   public AstProperty(AstNode prefix, boolean lvalue, boolean strict, boolean ignoreReturnType) {
      this.prefix = prefix;
      this.lvalue = lvalue;
      this.strict = strict;
      this.ignoreReturnType = ignoreReturnType;
   }

   protected abstract Object getProperty(Bindings var1, ELContext var2) throws ELException;

   protected AstNode getPrefix() {
      return this.prefix;
   }

   public ValueReference getValueReference(Bindings bindings, ELContext context) {
      Object base = this.prefix.eval(bindings, context);
      if (base == null) {
         throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", this.prefix));
      } else {
         Object property = this.getProperty(bindings, context);
         if (property == null && this.strict) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", "null", base));
         } else {
            return new ValueReference(base, property);
         }
      }
   }

   public Object eval(Bindings bindings, ELContext context) {
      Object base = this.prefix.eval(bindings, context);
      if (base == null) {
         return null;
      } else {
         Object property = this.getProperty(bindings, context);
         if (property == null && this.strict) {
            return null;
         } else {
            context.setPropertyResolved(false);
            Object result = context.getELResolver().getValue(context, base, property);
            if (!context.isPropertyResolved()) {
               throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", property, base));
            } else {
               return result;
            }
         }
      }
   }

   public final boolean isLiteralText() {
      return false;
   }

   public final boolean isLeftValue() {
      return this.lvalue;
   }

   public boolean isMethodInvocation() {
      return false;
   }

   public Class<?> getType(Bindings bindings, ELContext context) {
      if (!this.lvalue) {
         return null;
      } else {
         Object base = this.prefix.eval(bindings, context);
         if (base == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", this.prefix));
         } else {
            Object property = this.getProperty(bindings, context);
            if (property == null && this.strict) {
               throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", "null", base));
            } else {
               context.setPropertyResolved(false);
               Class<?> result = context.getELResolver().getType(context, base, property);
               if (!context.isPropertyResolved()) {
                  throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", property, base));
               } else {
                  return result;
               }
            }
         }
      }
   }

   public boolean isReadOnly(Bindings bindings, ELContext context) throws ELException {
      if (!this.lvalue) {
         return true;
      } else {
         Object base = this.prefix.eval(bindings, context);
         if (base == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", this.prefix));
         } else {
            Object property = this.getProperty(bindings, context);
            if (property == null && this.strict) {
               throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", "null", base));
            } else {
               context.setPropertyResolved(false);
               boolean result = context.getELResolver().isReadOnly(context, base, property);
               if (!context.isPropertyResolved()) {
                  throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", property, base));
               } else {
                  return result;
               }
            }
         }
      }
   }

   public void setValue(Bindings bindings, ELContext context, Object value) throws ELException {
      if (!this.lvalue) {
         throw new ELException(LocalMessages.get("error.value.set.rvalue", this.getStructuralId(bindings)));
      } else {
         Object base = this.prefix.eval(bindings, context);
         if (base == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", this.prefix));
         } else {
            Object property = this.getProperty(bindings, context);
            if (property == null && this.strict) {
               throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", "null", base));
            } else {
               context.setPropertyResolved(false);
               Class<?> type = context.getELResolver().getType(context, base, property);
               if (context.isPropertyResolved()) {
                  if (type != null && (value != null || type.isPrimitive())) {
                     value = bindings.convert(value, type);
                  }

                  context.setPropertyResolved(false);
               }

               context.getELResolver().setValue(context, base, property, value);
               if (!context.isPropertyResolved()) {
                  throw new PropertyNotFoundException(LocalMessages.get("error.property.property.notfound", property, base));
               }
            }
         }
      }
   }

   protected Method findMethod(String name, Class<?> clazz, Class<?> returnType, Class<?>[] paramTypes) {
      Method method = null;

      try {
         method = clazz.getMethod(name, paramTypes);
      } catch (NoSuchMethodException var7) {
         throw new MethodNotFoundException(LocalMessages.get("error.property.method.notfound", name, clazz));
      }

      method = this.findAccessibleMethod(method);
      if (method == null) {
         throw new MethodNotFoundException(LocalMessages.get("error.property.method.notfound", name, clazz));
      } else if (!this.ignoreReturnType && returnType != null && !returnType.isAssignableFrom(method.getReturnType())) {
         throw new MethodNotFoundException(LocalMessages.get("error.property.method.returntype", method.getReturnType(), name, clazz, returnType));
      } else {
         return method;
      }
   }

   public MethodInfo getMethodInfo(Bindings bindings, ELContext context, Class<?> returnType, Class<?>[] paramTypes) {
      Object base = this.prefix.eval(bindings, context);
      if (base == null) {
         throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", this.prefix));
      } else {
         Object property = this.getProperty(bindings, context);
         if (property == null && this.strict) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.method.notfound", "null", base));
         } else {
            String name = (String)bindings.convert(property, String.class);
            Method method = this.findMethod(name, base.getClass(), returnType, paramTypes);
            return new MethodInfo(method.getName(), method.getReturnType(), paramTypes);
         }
      }
   }

   public Object invoke(Bindings bindings, ELContext context, Class<?> returnType, Class<?>[] paramTypes, Object[] paramValues) {
      Object base = this.prefix.eval(bindings, context);
      if (base == null) {
         throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", this.prefix));
      } else {
         Object property = this.getProperty(bindings, context);
         if (property == null && this.strict) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.method.notfound", "null", base));
         } else {
            String name = (String)bindings.convert(property, String.class);
            Method method = this.findMethod(name, base.getClass(), returnType, paramTypes);

            try {
               return method.invoke(base, paramValues);
            } catch (IllegalAccessException var11) {
               throw new ELException(LocalMessages.get("error.property.method.access", name, base.getClass()));
            } catch (IllegalArgumentException var12) {
               throw new ELException(LocalMessages.get("error.property.method.invocation", name, base.getClass()), var12);
            } catch (InvocationTargetException var13) {
               throw new ELException(LocalMessages.get("error.property.method.invocation", name, base.getClass()), var13.getCause());
            }
         }
      }
   }

   public AstNode getChild(int i) {
      return i == 0 ? this.prefix : null;
   }
}
