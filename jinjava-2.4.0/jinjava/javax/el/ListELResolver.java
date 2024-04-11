package jinjava.javax.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.List;

public class ListELResolver extends ELResolver {
   private final boolean readOnly;

   public ListELResolver() {
      this(false);
   }

   public ListELResolver(boolean readOnly) {
      this.readOnly = readOnly;
   }

   public Class<?> getCommonPropertyType(ELContext context, Object base) {
      return isResolvable(base) ? Integer.class : null;
   }

   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
      return null;
   }

   public Class<?> getType(ELContext context, Object base, Object property) {
      if (context == null) {
         throw new NullPointerException("context is null");
      } else {
         Class<?> result = null;
         if (isResolvable(base)) {
            toIndex((List)base, property);
            result = Object.class;
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
         if (isResolvable(base)) {
            int index = toIndex((List)null, property);
            List<?> list = (List)base;
            result = index >= 0 && index < list.size() ? list.get(index) : null;
            context.setPropertyResolved(true);
         }

         return result;
      }
   }

   public boolean isReadOnly(ELContext context, Object base, Object property) {
      if (context == null) {
         throw new NullPointerException("context is null");
      } else {
         if (isResolvable(base)) {
            toIndex((List)base, property);
            context.setPropertyResolved(true);
         }

         return this.readOnly;
      }
   }

   public void setValue(ELContext context, Object base, Object property, Object value) {
      if (context == null) {
         throw new NullPointerException("context is null");
      } else {
         if (isResolvable(base)) {
            if (this.readOnly) {
               throw new PropertyNotWritableException("resolver is read-only");
            }

            List list = (List)base;
            int index = toIndex(list, property);

            try {
               list.set(index, value);
            } catch (UnsupportedOperationException var8) {
               throw new PropertyNotWritableException(var8);
            } catch (ArrayStoreException var9) {
               throw new IllegalArgumentException(var9);
            }

            context.setPropertyResolved(true);
         }

      }
   }

   private static final boolean isResolvable(Object base) {
      return base instanceof List;
   }

   private static final int toIndex(List<?> base, Object property) {
      int index = false;
      int index;
      if (property instanceof Number) {
         index = ((Number)property).intValue();
      } else if (property instanceof String) {
         try {
            index = Integer.valueOf((String)property);
         } catch (NumberFormatException var4) {
            throw new IllegalArgumentException("Cannot parse list index: " + property);
         }
      } else if (property instanceof Character) {
         index = (Character)property;
      } else {
         if (!(property instanceof Boolean)) {
            throw new IllegalArgumentException("Cannot coerce property to list index: " + property);
         }

         index = (Boolean)property ? 1 : 0;
      }

      if (base == null || index >= 0 && index < base.size()) {
         return index;
      } else {
         throw new PropertyNotFoundException("List index out of bounds: " + index);
      }
   }
}
