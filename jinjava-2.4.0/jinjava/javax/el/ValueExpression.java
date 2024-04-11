package jinjava.javax.el;

public abstract class ValueExpression extends Expression {
   private static final long serialVersionUID = 1L;

   public abstract Class<?> getExpectedType();

   public abstract Class<?> getType(ELContext var1);

   public abstract Object getValue(ELContext var1);

   public abstract boolean isReadOnly(ELContext var1);

   public abstract void setValue(ELContext var1, Object var2);

   public ValueReference getValueReference(ELContext context) {
      return null;
   }
}
