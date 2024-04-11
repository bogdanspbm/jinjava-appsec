package jinjava.javax.el;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CompositeELResolver extends ELResolver {
   private final List<ELResolver> resolvers = new ArrayList();

   public void add(ELResolver elResolver) {
      if (elResolver == null) {
         throw new NullPointerException("resolver must not be null");
      } else {
         this.resolvers.add(elResolver);
      }
   }

   public Class<?> getCommonPropertyType(ELContext context, Object base) {
      Class<?> result = null;
      int i = 0;

      for(int l = this.resolvers.size(); i < l; ++i) {
         Class<?> type = ((ELResolver)this.resolvers.get(i)).getCommonPropertyType(context, base);
         if (type != null) {
            if (result != null && !type.isAssignableFrom(result)) {
               if (!result.isAssignableFrom(type)) {
                  result = Object.class;
               }
            } else {
               result = type;
            }
         }
      }

      return result;
   }

   public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext context, final Object base) {
      return new Iterator<FeatureDescriptor>() {
         Iterator<FeatureDescriptor> empty = Collections.emptyList().iterator();
         Iterator<ELResolver> resolvers;
         Iterator<FeatureDescriptor> features;

         {
            this.resolvers = CompositeELResolver.this.resolvers.iterator();
            this.features = this.empty;
         }

         Iterator<FeatureDescriptor> features() {
            while(!this.features.hasNext() && this.resolvers.hasNext()) {
               this.features = ((ELResolver)this.resolvers.next()).getFeatureDescriptors(context, base);
               if (this.features == null) {
                  this.features = this.empty;
               }
            }

            return this.features;
         }

         public boolean hasNext() {
            return this.features().hasNext();
         }

         public FeatureDescriptor next() {
            return (FeatureDescriptor)this.features().next();
         }

         public void remove() {
            this.features().remove();
         }
      };
   }

   public Class<?> getType(ELContext context, Object base, Object property) {
      context.setPropertyResolved(false);
      int i = 0;

      for(int l = this.resolvers.size(); i < l; ++i) {
         Class<?> type = ((ELResolver)this.resolvers.get(i)).getType(context, base, property);
         if (context.isPropertyResolved()) {
            return type;
         }
      }

      return null;
   }

   public Object getValue(ELContext context, Object base, Object property) {
      context.setPropertyResolved(false);
      int i = 0;

      for(int l = this.resolvers.size(); i < l; ++i) {
         Object value = ((ELResolver)this.resolvers.get(i)).getValue(context, base, property);
         if (context.isPropertyResolved()) {
            return value;
         }
      }

      return null;
   }

   public boolean isReadOnly(ELContext context, Object base, Object property) {
      context.setPropertyResolved(false);
      int i = 0;

      for(int l = this.resolvers.size(); i < l; ++i) {
         boolean readOnly = ((ELResolver)this.resolvers.get(i)).isReadOnly(context, base, property);
         if (context.isPropertyResolved()) {
            return readOnly;
         }
      }

      return false;
   }

   public void setValue(ELContext context, Object base, Object property, Object value) {
      context.setPropertyResolved(false);
      int i = 0;

      for(int l = this.resolvers.size(); i < l; ++i) {
         ((ELResolver)this.resolvers.get(i)).setValue(context, base, property, value);
         if (context.isPropertyResolved()) {
            return;
         }
      }

   }

   public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params) {
      context.setPropertyResolved(false);
      int i = 0;

      for(int l = this.resolvers.size(); i < l; ++i) {
         Object result = ((ELResolver)this.resolvers.get(i)).invoke(context, base, method, paramTypes, params);
         if (context.isPropertyResolved()) {
            return result;
         }
      }

      return null;
   }
}
