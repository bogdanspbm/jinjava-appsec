package jinjava.javax.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Map;

public class MapELResolver extends ELResolver {
   private final boolean readOnly;

   public MapELResolver() {
      this(false);
   }

   public MapELResolver(boolean readOnly) {
      this.readOnly = readOnly;
   }

   public Class<?> getCommonPropertyType(ELContext context, Object base) {
      return this.isResolvable(base) ? Object.class : null;
   }

   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
      if (this.isResolvable(base)) {
         Map<?, ?> map = (Map)base;
         final Iterator<?> keys = map.keySet().iterator();
         return new Iterator<FeatureDescriptor>() {
            public boolean hasNext() {
               return keys.hasNext();
            }

            public FeatureDescriptor next() {
               Object key = keys.next();
               FeatureDescriptor feature = new FeatureDescriptor();
               feature.setDisplayName(key == null ? "null" : key.toString());
               feature.setName(feature.getDisplayName());
               feature.setShortDescription("");
               feature.setExpert(true);
               feature.setHidden(false);
               feature.setPreferred(true);
               feature.setValue("type", key == null ? null : key.getClass());
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
         throw new NullPointerException("context is null");
      } else {
         Class<?> result = null;
         if (this.isResolvable(base)) {
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
         if (this.isResolvable(base)) {
            result = ((Map)base).get(property);
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

            ((Map)base).put(property, value);
            context.setPropertyResolved(true);
         }

      }
   }

   private final boolean isResolvable(Object base) {
      return base instanceof Map;
   }
}
