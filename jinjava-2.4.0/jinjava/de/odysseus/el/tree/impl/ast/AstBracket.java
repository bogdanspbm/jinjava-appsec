package jinjava.de.odysseus.el.tree.impl.ast;

import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELException;

public class AstBracket extends AstProperty {
   protected final AstNode property;

   public AstBracket(AstNode base, AstNode property, boolean lvalue, boolean strict) {
      this(base, property, lvalue, strict, false);
   }

   public AstBracket(AstNode base, AstNode property, boolean lvalue, boolean strict, boolean ignoreReturnType) {
      super(base, lvalue, strict, ignoreReturnType);
      this.property = property;
   }

   protected Object getProperty(Bindings bindings, ELContext context) throws ELException {
      return this.property.eval(bindings, context);
   }

   public String toString() {
      return "[...]";
   }

   public void appendStructure(StringBuilder b, Bindings bindings) {
      this.getChild(0).appendStructure(b, bindings);
      b.append("[");
      this.getChild(1).appendStructure(b, bindings);
      b.append("]");
   }

   public int getCardinality() {
      return 2;
   }

   public AstNode getChild(int i) {
      return i == 1 ? this.property : super.getChild(i);
   }
}
