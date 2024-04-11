package jinjava.javax.el;

import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanELResolver extends ELResolver {
   private final boolean readOnly;
   private final ConcurrentHashMap<Class<?>, BeanELResolver.BeanProperties> cache;
   private ExpressionFactory defaultFactory;

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

   private static Method findAccessibleMethod(Method method) {
      Method result = findPublicAccessibleMethod(method);
      if (result == null && method != null && Modifier.isPublic(method.getModifiers())) {
         result = method;

         try {
            method.setAccessible(true);
         } catch (SecurityException var3) {
            result = null;
         }
      }

      return result;
   }

   public BeanELResolver() {
      this(false);
   }

   public BeanELResolver(boolean readOnly) {
      this.readOnly = readOnly;
      this.cache = new ConcurrentHashMap();
   }

   public Class<?> getCommonPropertyType(ELContext context, Object base) {
      return this.isResolvable(base) ? Object.class : null;
   }

   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
      if (this.isResolvable(base)) {
         final PropertyDescriptor[] properties;
         try {
            properties = Introspector.getBeanInfo(base.getClass()).getPropertyDescriptors();
         } catch (IntrospectionException var5) {
            return Collections.emptyList().iterator();
         }

         return new Iterator<FeatureDescriptor>() {
            int next = 0;

            public boolean hasNext() {
               return properties != null && this.next < properties.length;
            }

            public FeatureDescriptor next() {
               PropertyDescriptor property = properties[this.next++];
               FeatureDescriptor feature = new FeatureDescriptor();
               feature.setDisplayName(property.getDisplayName());
               feature.setName(property.getName());
               feature.setShortDescription(property.getShortDescription());
               feature.setExpert(property.isExpert());
               feature.setHidden(property.isHidden());
               feature.setPreferred(property.isPreferred());
               feature.setValue("type", property.getPropertyType());
               feature.setValue("resolvableAtDesignTime", true);
               return feature;
            }

            public void remove() {
               throw new UnsupportedOperationException("cannot remove");
            }
         };
      } else {
         return null;
      }
   }

   public Class<?> getType(ELContext context, Object base, Object property) {
      if (context == null) {
         throw new NullPointerException();
      } else {
         Class<?> result = null;
         if (this.isResolvable(base)) {
            result = this.toBeanProperty(base, property).getPropertyType();
            context.setPropertyResolved(true);
         }

         return result;
      }
   }

   public Object getValue(ELContext context, Object base, Object property) {
      if (context == null) {
         throw new NullPointerException();
      } else {
         Object result = null;
         if (this.isResolvable(base)) {
            Method method = this.toBeanProperty(base, property).getReadMethod();
            if (method == null) {
               throw new PropertyNotFoundException("Cannot read property " + property);
            }

            try {
               result = method.invoke(base);
            } catch (InvocationTargetException var7) {
               throw new ELException(var7.getCause());
            } catch (Exception var8) {
               throw new ELException(var8);
            }

            context.setPropertyResolved(true);
         }

         return result;
      }
   }

   public boolean isReadOnly(ELContext context, Object base, Object property) {
      if (context == null) {
         throw new NullPointerException();
      } else {
         boolean result = this.readOnly;
         if (this.isResolvable(base)) {
            result |= this.toBeanProperty(base, property).isReadOnly();
            context.setPropertyResolved(true);
         }

         return result;
      }
   }

   public void setValue(ELContext context, Object base, Object property, Object value) {
      if (context == null) {
         throw new NullPointerException();
      } else {
         if (this.isResolvable(base)) {
            if (this.readOnly) {
               throw new PropertyNotWritableException("resolver is read-only");
            }

            Method method = this.toBeanProperty(base, property).getWriteMethod();
            if (method == null) {
               throw new PropertyNotWritableException("Cannot write property: " + property);
            }

            try {
               method.invoke(base, value);
            } catch (InvocationTargetException var7) {
               throw new ELException("Cannot write property: " + property, var7.getCause());
            } catch (IllegalArgumentException var8) {
               throw new ELException("Cannot write property: " + property, var8);
            } catch (IllegalAccessException var9) {
               throw new PropertyNotWritableException("Cannot write property: " + property, var9);
            }

            context.setPropertyResolved(true);
         }

      }
   }

   public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params) {
      if (context == null) {
         throw new NullPointerException();
      } else {
         Object result = null;
         if (this.isResolvable(base)) {
            if (params == null) {
               params = new Object[0];
            }

            String name = method.toString();
            Method target = this.findMethod(base, name, paramTypes, params.length);
            if (target == null) {
               throw new MethodNotFoundException("Cannot find method " + name + " with " + params.length + " parameters in " + base.getClass());
            }

            try {
               result = target.invoke(base, this.coerceParams(this.getExpressionFactory(context), target, params));
            } catch (InvocationTargetException var10) {
               throw new ELException(var10.getCause());
            } catch (IllegalAccessException var11) {
               throw new ELException(var11);
            }

            context.setPropertyResolved(true);
         }

         return result;
      }
   }

   private Method findMethod(Object base, String name, Class<?>[] types, int paramCount) {
      if (types != null) {
         try {
            return findAccessibleMethod(base.getClass().getMethod(name, types));
         } catch (NoSuchMethodException var11) {
            return null;
         }
      } else {
         Method varArgsMethod = null;
         Method[] arr$ = base.getClass().getMethods();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Method method = arr$[i$];
            if (method.getName().equals(name)) {
               int formalParamCount = method.getParameterTypes().length;
               if (method.isVarArgs() && paramCount >= formalParamCount - 1) {
                  varArgsMethod = method;
               } else if (paramCount == formalParamCount) {
                  return findAccessibleMethod(method);
               }
            }
         }

         return varArgsMethod == null ? null : findAccessibleMethod(varArgsMethod);
      }
   }

   private ExpressionFactory getExpressionFactory(ELContext context) {
      Object obj = context.getContext(ExpressionFactory.class);
      if (obj instanceof ExpressionFactory) {
         return (ExpressionFactory)obj;
      } else {
         if (this.defaultFactory == null) {
            this.defaultFactory = ExpressionFactory.newInstance();
         }

         return this.defaultFactory;
      }
   }

   private Object[] coerceParams(ExpressionFactory factory, Method method, Object[] params) {
      Class<?>[] types = method.getParameterTypes();
      Object[] args = new Object[types.length];
      int varargIndex;
      if (method.isVarArgs()) {
         varargIndex = types.length - 1;
         if (params.length < varargIndex) {
            throw new ELException("Bad argument count");
         }

         for(int i = 0; i < varargIndex; ++i) {
            this.coerceValue(args, i, factory, params[i], types[i]);
         }

         Class<?> varargType = types[varargIndex].getComponentType();
         int length = params.length - varargIndex;
         Object array = null;
         if (length == 1) {
            Object source = params[varargIndex];
            if (source != null && source.getClass().isArray()) {
               if (types[varargIndex].isInstance(source)) {
                  array = source;
               } else {
                  length = Array.getLength(source);
                  array = Array.newInstance(varargType, length);

                  for(int i = 0; i < length; ++i) {
                     this.coerceValue(array, i, factory, Array.get(source, i), varargType);
                  }
               }
            } else {
               array = Array.newInstance(varargType, 1);
               this.coerceValue(array, 0, factory, source, varargType);
            }
         } else {
            array = Array.newInstance(varargType, length);

            for(int i = 0; i < length; ++i) {
               this.coerceValue(array, i, factory, params[varargIndex + i], varargType);
            }
         }

         args[varargIndex] = array;
      } else {
         if (params.length != args.length) {
            throw new ELException("Bad argument count");
         }

         for(varargIndex = 0; varargIndex < args.length; ++varargIndex) {
            this.coerceValue(args, varargIndex, factory, params[varargIndex], types[varargIndex]);
         }
      }

      return args;
   }

   private void coerceValue(Object array, int index, ExpressionFactory factory, Object value, Class<?> type) {
      if (value != null || type.isPrimitive()) {
         Array.set(array, index, factory.coerceToType(value, type));
      }

   }

   private final boolean isResolvable(Object base) {
      return base != null;
   }

   private final BeanELResolver.BeanProperty toBeanProperty(Object base, Object property) {
      BeanELResolver.BeanProperties beanProperties = (BeanELResolver.BeanProperties)this.cache.get(base.getClass());
      if (beanProperties == null) {
         BeanELResolver.BeanProperties newBeanProperties = new BeanELResolver.BeanProperties(base.getClass());
         beanProperties = (BeanELResolver.BeanProperties)this.cache.putIfAbsent(base.getClass(), newBeanProperties);
         if (beanProperties == null) {
            beanProperties = newBeanProperties;
         }
      }

      BeanELResolver.BeanProperty beanProperty = property == null ? null : beanProperties.getBeanProperty(property.toString());
      if (beanProperty == null) {
         throw new PropertyNotFoundException("Could not find property " + property + " in " + base.getClass());
      } else {
         return beanProperty;
      }
   }

   private final void purgeBeanClasses(ClassLoader loader) {
      Iterator classes = this.cache.keySet().iterator();

      while(classes.hasNext()) {
         if (loader == ((Class)classes.next()).getClassLoader()) {
            classes.remove();
         }
      }

   }

   protected static final class BeanProperty {
      private final PropertyDescriptor descriptor;
      private Method readMethod;
      private Method writedMethod;

      public BeanProperty(PropertyDescriptor descriptor) {
         this.descriptor = descriptor;
      }

      public Class<?> getPropertyType() {
         return this.descriptor.getPropertyType();
      }

      public Method getReadMethod() {
         if (this.readMethod == null) {
            this.readMethod = BeanELResolver.findAccessibleMethod(this.descriptor.getReadMethod());
         }

         return this.readMethod;
      }

      public Method getWriteMethod() {
         if (this.writedMethod == null) {
            this.writedMethod = BeanELResolver.findAccessibleMethod(this.descriptor.getWriteMethod());
         }

         return this.writedMethod;
      }

      public boolean isReadOnly() {
         return this.getWriteMethod() == null;
      }
   }

   protected static final class BeanProperties {
      private final Map<String, BeanELResolver.BeanProperty> map = new HashMap();

      public BeanProperties(Class<?> baseClass) {
         PropertyDescriptor[] descriptors;
         try {
            descriptors = Introspector.getBeanInfo(baseClass).getPropertyDescriptors();
         } catch (IntrospectionException var7) {
            throw new ELException(var7);
         }

         PropertyDescriptor[] arr$ = descriptors;
         int len$ = descriptors.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            PropertyDescriptor descriptor = arr$[i$];
            this.map.put(descriptor.getName(), new BeanELResolver.BeanProperty(descriptor));
         }

      }

      public BeanELResolver.BeanProperty getBeanProperty(String property) {
         return (BeanELResolver.BeanProperty)this.map.get(property);
      }
   }
}
