package jinjava.de.odysseus.el.tree.impl.ast;

import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.MethodInfo;
import jinjava.javax.el.ValueReference;

public final class AstEval extends AstNode {
   private final AstNode child;
   private final boolean deferred;

   public AstEval(AstNode child, boolean deferred) {
      this.child = child;
      this.deferred = deferred;
   }

   public boolean isDeferred() {
      return this.deferred;
   }

   public boolean isLeftValue() {
      return this.getChild(0).isLeftValue();
   }

   public boolean isMethodInvocation() {
      return this.getChild(0).isMethodInvocation();
   }

   public ValueReference getValueReference(Bindings bindings, ELContext context) {
      return this.child.getValueReference(bindings, context);
   }

   public Object eval(Bindings bindings, ELContext context) {
      return this.child.eval(bindings, context);
   }

   public String toString() {
      return (this.deferred ? "#" : "$") + "{...}";
   }

   public void appendStructure(StringBuilder b, Bindings bindings) {
      b.append(this.deferred ? "#{" : "${");
      this.child.appendStructure(b, bindings);
      b.append("}");
   }

   public MethodInfo getMethodInfo(Bindings bindings, ELContext context, Class<?> returnType, Class<?>[] paramTypes) {
      return this.child.getMethodInfo(bindings, context, returnType, paramTypes);
   }

   public Object invoke(Bindings bindings, ELContext context, Class<?> returnType, Class<?>[] paramTypes, Object[] paramValues) {
      return this.child.invoke(bindings, context, returnType, paramTypes, paramValues);
   }

   public Class<?> getType(Bindings bindings, ELContext context) {
      return this.child.getType(bindings, context);
   }

   public boolean isLiteralText() {
      return this.child.isLiteralText();
   }

   public boolean isReadOnly(Bindings bindings, ELContext context) {
      return this.child.isReadOnly(bindings, context);
   }

   public void setValue(Bindings bindings, ELContext context, Object value) {
      this.child.setValue(bindings, context, value);
   }

   public int getCardinality() {
      return 1;
   }

   public AstNode getChild(int i) {
      return i == 0 ? this.child : null;
   }
}
