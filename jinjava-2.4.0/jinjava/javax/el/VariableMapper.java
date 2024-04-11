package jinjava.javax.el;

public abstract class VariableMapper {
   public abstract ValueExpression resolveVariable(String var1);

   public abstract ValueExpression setVariable(String var1, ValueExpression var2);
}
