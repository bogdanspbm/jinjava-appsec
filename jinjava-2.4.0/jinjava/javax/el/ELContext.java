package jinjava.javax.el;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class ELContext {
   private Map<Class<?>, Object> context;
   private Locale locale;
   private boolean resolved;

   public Object getContext(Class<?> key) {
      if (key == null) {
         throw new NullPointerException("key is null");
      } else {
         return this.context == null ? null : this.context.get(key);
      }
   }

   public abstract ELResolver getELResolver();

   public abstract FunctionMapper getFunctionMapper();

   public Locale getLocale() {
      return this.locale;
   }

   public abstract VariableMapper getVariableMapper();

   public boolean isPropertyResolved() {
      return this.resolved;
   }

   public void putContext(Class<?> key, Object contextObject) {
      if (key == null) {
         throw new NullPointerException("key is null");
      } else {
         if (this.context == null) {
            this.context = new HashMap();
         }

         this.context.put(key, contextObject);
      }
   }

   public void setLocale(Locale locale) {
      this.locale = locale;
   }

   public void setPropertyResolved(boolean resolved) {
      this.resolved = resolved;
   }
}
