package jinjava.de.odysseus.el.util;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import jinjava.javax.el.ArrayELResolver;
import jinjava.javax.el.BeanELResolver;
import jinjava.javax.el.CompositeELResolver;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELResolver;
import jinjava.javax.el.ListELResolver;
import jinjava.javax.el.MapELResolver;
import jinjava.javax.el.ResourceBundleELResolver;

public class SimpleResolver extends ELResolver {
   private static final ELResolver DEFAULT_RESOLVER_READ_ONLY = new CompositeELResolver() {
      {
         this.add(new ArrayELResolver(true));
         this.add(new ListELResolver(true));
         this.add(new MapELResolver(true));
         this.add(new ResourceBundleELResolver());
         this.add(new BeanELResolver(true));
      }
   };
   private static final ELResolver DEFAULT_RESOLVER_READ_WRITE = new CompositeELResolver() {
      {
         this.add(new ArrayELResolver(false));
         this.add(new ListELResolver(false));
         this.add(new MapELResolver(false));
         this.add(new ResourceBundleELResolver());
         this.add(new BeanELResolver(false));
      }
   };
   private final RootPropertyResolver root;
   private final CompositeELResolver delegate;

   public SimpleResolver(ELResolver resolver, boolean readOnly) {
      this.delegate = new CompositeELResolver();
      this.delegate.add(this.root = new RootPropertyResolver(readOnly));
      this.delegate.add(resolver);
   }

   public SimpleResolver(ELResolver resolver) {
      this(resolver, false);
   }

   public SimpleResolver(boolean readOnly) {
      this(readOnly ? DEFAULT_RESOLVER_READ_ONLY : DEFAULT_RESOLVER_READ_WRITE, readOnly);
   }

   public SimpleResolver() {
      this(DEFAULT_RESOLVER_READ_WRITE, false);
   }

   public RootPropertyResolver getRootPropertyResolver() {
      return this.root;
   }

   public Class<?> getCommonPropertyType(ELContext context, Object base) {
      return this.delegate.getCommonPropertyType(context, base);
   }

   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
      return this.delegate.getFeatureDescriptors(context, base);
   }

   public Class<?> getType(ELContext context, Object base, Object property) {
      return this.delegate.getType(context, base, property);
   }

   public Object getValue(ELContext context, Object base, Object property) {
      return this.delegate.getValue(context, base, property);
   }

   public boolean isReadOnly(ELContext context, Object base, Object property) {
      return this.delegate.isReadOnly(context, base, property);
   }

   public void setValue(ELContext context, Object base, Object property, Object value) {
      this.delegate.setValue(context, base, property, value);
   }

   public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params) {
      return this.delegate.invoke(context, base, method, paramTypes, params);
   }
}
