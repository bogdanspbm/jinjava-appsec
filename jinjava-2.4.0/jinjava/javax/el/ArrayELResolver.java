package jinjava.javax.el;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Array;
import java.util.Iterator;

public class ArrayELResolver extends ELResolver {
   private final boolean readOnly;

   public ArrayELResolver() {
      this(false);
   }

   public ArrayELResolver(boolean readOnly) {
      this.readOnly = readOnly;
   }

   public Class<?> getCommonPropertyType(ELContext context, Object base) {
      return this.isResolvable(base) ? Integer.class : null;
   }

   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
      return null;
   }

   public Class<?> getType(ELContext context, Object base, Object property) {
      if (context == null) {
         throw new NullPointerException("context is null");
      } else {
         Class<?> result = null;
         if (this.isResolvable(base)) {
            this.toIndex(base, property);
            result = base.getClass().getComponentType();
            context.setPropertyResolved(true);
         }

         return result;
      }
   }

   public Object getValue(ELContext context, Object base, Object property) {
      if (context == null) {
         throw new NullPointerException("context is null");
      } else {
         Object result = null;
         if (this.isResolvable(base)) {
            int index = this.toIndex((Object)null, property);
            result = index >= 0 && index < Array.getLength(base) ? Array.get(base, index) : null;
            context.setPropertyResolved(true);
         }

         return result;
      }
   }

   public boolean isReadOnly(ELContext context, Object base, Object property) {
      if (context == null) {
         throw new NullPointerException("context is null");
      } else {
         if (this.isResolvable(base)) {
            this.toIndex(base, property);
            context.setPropertyResolved(true);
         }

         return this.readOnly;
      }
   }

   public void setValue(ELContext context, Object base, Object property, Object value) {
      if (context == null) {
         throw new NullPointerException("context is null");
      } else {
         if (this.isResolvable(base)) {
            if (this.readOnly) {
               throw new PropertyNotWritableException("resolver is read-only");
            }

            Array.set(base, this.toIndex(base, property), value);
            context.setPropertyResolved(true);
         }

      }
   }

   private final boolean isResolvable(Object base) {
      return base != null && base.getClass().isArray();
   }

   private final int toIndex(Object base, Object property) {
      int index = false;
      int index;
      if (property instanceof Number) {
         index = ((Number)property).intValue();
      } else if (property instanceof String) {
         try {
            index = Integer.valueOf((String)property);
         } catch (NumberFormatException var5) {
            throw new IllegalArgumentException("Cannot parse array index: " + property);
         }
      } else if (property instanceof Character) {
         index = (Character)property;
      } else {
         if (!(property instanceof Boolean)) {
            throw new IllegalArgumentException("Cannot coerce property to array index: " + property);
         }

         index = (Boolean)property ? 1 : 0;
      }

      if (base == null || index >= 0 && index < Array.getLength(base)) {
         return index;
      } else {
         throw new PropertyNotFoundException("Array index out of bounds: " + index);
      }
   }
}
