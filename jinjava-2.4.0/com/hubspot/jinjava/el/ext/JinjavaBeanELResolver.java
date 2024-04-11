package com.hubspot.jinjava.el.ext;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import jinjava.javax.el.BeanELResolver;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.MethodNotFoundException;

public class JinjavaBeanELResolver extends BeanELResolver {
   private static final Set<String> RESTRICTED_PROPERTIES = ImmutableSet.builder().add("class").build();
   private static final Set<String> RESTRICTED_METHODS = ImmutableSet.builder().add("clone").add("hashCode").add("notify").add("notifyAll").add("wait").build();

   public JinjavaBeanELResolver() {
   }

   public JinjavaBeanELResolver(boolean readOnly) {
      super(readOnly);
   }

   public Class<?> getType(ELContext context, Object base, Object property) {
      return super.getType(context, base, this.validatePropertyName(property));
   }

   public Object getValue(ELContext context, Object base, Object property) {
      return super.getValue(context, base, this.validatePropertyName(property));
   }

   public boolean isReadOnly(ELContext context, Object base, Object property) {
      return super.isReadOnly(context, base, this.validatePropertyName(property));
   }

   public void setValue(ELContext context, Object base, Object property, Object value) {
      super.setValue(context, base, this.validatePropertyName(property), value);
   }

   public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params) {
      if (method != null && !RESTRICTED_METHODS.contains(method.toString())) {
         return super.invoke(context, base, method, paramTypes, params);
      } else {
         throw new MethodNotFoundException("Cannot find method '" + method + "' in " + base.getClass());
      }
   }

   private String validatePropertyName(Object property) {
      String propertyName = this.transformPropertyName(property);
      return RESTRICTED_PROPERTIES.contains(propertyName) ? null : propertyName;
   }

   private String transformPropertyName(Object property) {
      if (property == null) {
         return null;
      } else {
         String propertyStr = property.toString();
         return propertyStr.indexOf(95) == -1 ? propertyStr : CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, propertyStr);
      }
   }
}
