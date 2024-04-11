package jinjava.javax.el;

public abstract class MethodExpression extends Expression {
   private static final long serialVersionUID = 1L;

   public abstract MethodInfo getMethodInfo(ELContext var1);

   public abstract Object invoke(ELContext var1, Object[] var2);

   public boolean isParmetersProvided() {
      return false;
   }
}
