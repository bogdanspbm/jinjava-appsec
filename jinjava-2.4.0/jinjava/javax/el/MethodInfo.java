package jinjava.javax.el;

public class MethodInfo {
   private final String name;
   private final Class<?> returnType;
   private final Class<?>[] paramTypes;

   public MethodInfo(String name, Class<?> returnType, Class<?>[] paramTypes) {
      this.name = name;
      this.returnType = returnType;
      this.paramTypes = paramTypes;
   }

   public String getName() {
      return this.name;
   }

   public Class<?>[] getParamTypes() {
      return this.paramTypes;
   }

   public Class<?> getReturnType() {
      return this.returnType;
   }
}
