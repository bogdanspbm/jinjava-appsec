package jinjava.de.odysseus.el.util;

import java.beans.FeatureDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELResolver;
import jinjava.javax.el.PropertyNotFoundException;
import jinjava.javax.el.PropertyNotWritableException;

public class RootPropertyResolver extends ELResolver {
   private final Map<String, Object> map;
   private final boolean readOnly;

   public RootPropertyResolver() {
      this(false);
   }

   public RootPropertyResolver(boolean readOnly) {
      this.map = Collections.synchronizedMap(new HashMap());
      this.readOnly = readOnly;
   }

   private boolean isResolvable(Object base) {
      return base == null;
   }

   private boolean resolve(ELContext context, Object base, Object property) {
      context.setPropertyResolved(this.isResolvable(base) && property instanceof String);
      return context.isPropertyResolved();
   }

   public Class<?> getCommonPropertyType(ELContext context, Object base) {
      return this.isResolvable(context) ? String.class : null;
   }

   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
      return null;
   }

   public Class<?> getType(ELContext context, Object base, Object property) {
      return this.resolve(context, base, property) ? Object.class : null;
   }

   public Object getValue(ELContext context, Object base, Object property) {
      if (this.resolve(context, base, property)) {
         if (!this.isProperty((String)property)) {
            throw new PropertyNotFoundException("Cannot find property " + property);
         } else {
            return this.getProperty((String)property);
         }
      } else {
         return null;
      }
   }

   public boolean isReadOnly(ELContext context, Object base, Object property) {
      return this.resolve(context, base, property) ? this.readOnly : false;
   }

   public void setValue(ELContext context, Object base, Object property, Object value) throws PropertyNotWritableException {
      if (this.resolve(context, base, property)) {
         if (this.readOnly) {
            throw new PropertyNotWritableException("Resolver is read only!");
         }

         this.setProperty((String)property, value);
      }

   }

   public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params) {
      if (this.resolve(context, base, method)) {
         throw new NullPointerException("Cannot invoke method " + method + " on null");
      } else {
         return null;
      }
   }

   public Object getProperty(String property) {
      return this.map.get(property);
   }

   public void setProperty(String property, Object value) {
      this.map.put(property, value);
   }

   public boolean isProperty(String property) {
      return this.map.containsKey(property);
   }

   public Iterable<String> properties() {
      return this.map.keySet();
   }
}
