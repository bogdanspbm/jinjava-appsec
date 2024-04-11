package com.hubspot.jinjava.el.ext;

import jinjava.javax.el.ELContext;
import jinjava.javax.el.ListELResolver;

public class JinjavaListELResolver extends ListELResolver {
   public JinjavaListELResolver(boolean readOnly) {
      super(readOnly);
   }

   public Class<?> getType(ELContext context, Object base, Object property) {
      try {
         return super.getType(context, base, property);
      } catch (IllegalArgumentException var5) {
         return null;
      }
   }

   public boolean isReadOnly(ELContext context, Object base, Object property) {
      try {
         return super.isReadOnly(context, base, property);
      } catch (IllegalArgumentException var5) {
         return false;
      }
   }

   public Object getValue(ELContext context, Object base, Object property) {
      try {
         return super.getValue(context, base, property);
      } catch (IllegalArgumentException var5) {
         return null;
      }
   }

   public void setValue(ELContext context, Object base, Object property, Object value) {
      try {
         super.setValue(context, base, property, value);
      } catch (IllegalArgumentException var6) {
      }

   }
}
