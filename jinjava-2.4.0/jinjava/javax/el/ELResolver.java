package jinjava.javax.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

public abstract class ELResolver {
   public static final String RESOLVABLE_AT_DESIGN_TIME = "resolvableAtDesignTime";
   public static final String TYPE = "type";

   public abstract Class<?> getCommonPropertyType(ELContext var1, Object var2);

   public abstract Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext var1, Object var2);

   public abstract Class<?> getType(ELContext var1, Object var2, Object var3);

   public abstract Object getValue(ELContext var1, Object var2, Object var3);

   public abstract boolean isReadOnly(ELContext var1, Object var2, Object var3);

   public abstract void setValue(ELContext var1, Object var2, Object var3, Object var4);

   public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params) {
      return null;
   }
}
