package jinjava.javax.el;

import java.beans.FeatureDescriptor;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceBundleELResolver extends ELResolver {
   public Class<?> getCommonPropertyType(ELContext context, Object base) {
      return this.isResolvable(base) ? String.class : null;
   }

   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
      if (this.isResolvable(base)) {
         final Enumeration<String> keys = ((ResourceBundle)base).getKeys();
         return new Iterator<FeatureDescriptor>() {
            public boolean hasNext() {
               return keys.hasMoreElements();
            }

            public FeatureDescriptor next() {
               FeatureDescriptor feature = new FeatureDescriptor();
               feature.setDisplayName((String)keys.nextElement());
               feature.setName(feature.getDisplayName());
               feature.setShortDescription("");
               feature.setExpert(true);
               feature.setHidden(false);
               feature.setPreferred(true);
               feature.setValue("type", String.class);
               feature.setValue("resolvableAtDesignTime", true);
               return feature;
            }

            public void remove() {
               throw new UnsupportedOperationException("Cannot remove");
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
         if (this.isResolvable(base)) {
            context.setPropertyResolved(true);
         }

         return null;
      }
   }

   public Object getValue(ELContext context, Object base, Object property) {
      if (context == null) {
         throw new NullPointerException("context is null");
      } else {
         Object result = null;
         if (this.isResolvable(base)) {
            if (property != null) {
               try {
                  result = ((ResourceBundle)base).getObject(property.toString());
               } catch (MissingResourceException var6) {
                  result = "???" + property + "???";
               }
            }

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

         return true;
      }
   }

   public void setValue(ELContext context, Object base, Object property, Object value) {
      if (context == null) {
         throw new NullPointerException("context is null");
      } else if (this.isResolvable(base)) {
         throw new PropertyNotWritableException("resolver is read-only");
      }
   }

   private final boolean isResolvable(Object base) {
      return base instanceof ResourceBundle;
   }
}
