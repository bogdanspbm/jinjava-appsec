package jinjava.de.odysseus.el.util;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELResolver;
import jinjava.javax.el.FunctionMapper;
import jinjava.javax.el.ValueExpression;
import jinjava.javax.el.VariableMapper;

public class SimpleContext extends ELContext {
   private SimpleContext.Functions functions;
   private SimpleContext.Variables variables;
   private ELResolver resolver;

   public SimpleContext() {
      this((ELResolver)null);
   }

   public SimpleContext(ELResolver resolver) {
      this.resolver = resolver;
   }

   public void setFunction(String prefix, String localName, Method method) {
      if (this.functions == null) {
         this.functions = new SimpleContext.Functions();
      }

      this.functions.setFunction(prefix, localName, method);
   }

   public ValueExpression setVariable(String name, ValueExpression expression) {
      if (this.variables == null) {
         this.variables = new SimpleContext.Variables();
      }

      return this.variables.setVariable(name, expression);
   }

   public FunctionMapper getFunctionMapper() {
      if (this.functions == null) {
         this.functions = new SimpleContext.Functions();
      }

      return this.functions;
   }

   public VariableMapper getVariableMapper() {
      if (this.variables == null) {
         this.variables = new SimpleContext.Variables();
      }

      return this.variables;
   }

   public ELResolver getELResolver() {
      if (this.resolver == null) {
         this.resolver = new SimpleResolver();
      }

      return this.resolver;
   }

   public void setELResolver(ELResolver resolver) {
      this.resolver = resolver;
   }

   static class Variables extends VariableMapper {
      Map<String, ValueExpression> map = Collections.emptyMap();

      public ValueExpression resolveVariable(String variable) {
         return (ValueExpression)this.map.get(variable);
      }

      public ValueExpression setVariable(String variable, ValueExpression expression) {
         if (this.map.isEmpty()) {
            this.map = new HashMap();
         }

         return (ValueExpression)this.map.put(variable, expression);
      }
   }

   static class Functions extends FunctionMapper {
      Map<String, Method> map = Collections.emptyMap();

      public Method resolveFunction(String prefix, String localName) {
         return (Method)this.map.get(prefix + ":" + localName);
      }

      public void setFunction(String prefix, String localName, Method method) {
         if (this.map.isEmpty()) {
            this.map = new HashMap();
         }

         this.map.put(prefix + ":" + localName, method);
      }
   }
}
