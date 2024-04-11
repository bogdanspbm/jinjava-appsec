package com.hubspot.jinjava.el;

import java.lang.reflect.Method;
import jinjava.de.odysseus.el.util.SimpleContext;
import jinjava.javax.el.ELResolver;

public class JinjavaELContext extends SimpleContext {
   private MacroFunctionMapper functionMapper;

   public JinjavaELContext() {
   }

   public JinjavaELContext(ELResolver resolver) {
      super(resolver);
   }

   public MacroFunctionMapper getFunctionMapper() {
      if (this.functionMapper == null) {
         this.functionMapper = new MacroFunctionMapper();
      }

      return this.functionMapper;
   }

   public void setFunction(String prefix, String localName, Method method) {
      this.getFunctionMapper().setFunction(prefix, localName, method);
   }
}
