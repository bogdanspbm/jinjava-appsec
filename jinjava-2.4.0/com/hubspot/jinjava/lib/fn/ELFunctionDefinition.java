package com.hubspot.jinjava.lib.fn;

import com.google.common.base.Throwables;
import com.hubspot.jinjava.lib.Importable;
import java.lang.reflect.Method;

public class ELFunctionDefinition implements Importable {
   private String namespace;
   private String localName;
   private Method method;

   public ELFunctionDefinition(String namespace, String localName, Class<?> methodClass, String methodName, Class<?>... parameterTypes) {
      this.namespace = namespace;
      this.localName = localName;
      this.method = resolveMethod(methodClass, methodName, parameterTypes);
   }

   private static Method resolveMethod(Class<?> methodClass, String methodName, Class<?>... parameterTypes) {
      try {
         Method m = methodClass.getDeclaredMethod(methodName, parameterTypes);
         m.setAccessible(true);
         return m;
      } catch (Exception var4) {
         throw Throwables.propagate(var4);
      }
   }

   public ELFunctionDefinition(String namespace, String localName, Method method) {
      this.namespace = namespace;
      this.localName = localName;
      this.method = method;
   }

   public String getNamespace() {
      return this.namespace;
   }

   public String getLocalName() {
      return this.localName;
   }

   public String getName() {
      return this.namespace + ":" + this.localName;
   }

   public Method getMethod() {
      return this.method;
   }
}
