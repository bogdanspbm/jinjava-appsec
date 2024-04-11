package jinjava.de.odysseus.el.tree.impl.ast;

import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;

public final class AstNested extends AstRightValue {
   private final AstNode child;

   public AstNested(AstNode child) {
      this.child = child;
   }

   public Object eval(Bindings bindings, ELContext context) {
      return this.child.eval(bindings, context);
   }

   public String toString() {
      return "(...)";
   }

   public void appendStructure(StringBuilder b, Bindings bindings) {
      b.append("(");
      this.child.appendStructure(b, bindings);
      b.append(")");
   }

   public int getCardinality() {
      return 1;
   }

   public AstNode getChild(int i) {
      return i == 0 ? this.child : null;
   }
}
