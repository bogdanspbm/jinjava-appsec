package jinjava.de.odysseus.el.tree.impl.ast;

public abstract class AstLiteral extends AstRightValue {
   public final int getCardinality() {
      return 0;
   }

   public final AstNode getChild(int i) {
      return null;
   }
}
