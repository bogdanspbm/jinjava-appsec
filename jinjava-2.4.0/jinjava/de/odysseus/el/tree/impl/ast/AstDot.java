package jinjava.de.odysseus.el.tree.impl.ast;

import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELException;

public class AstDot extends AstProperty {
   protected final String property;

   public AstDot(AstNode base, String property, boolean lvalue) {
      this(base, property, lvalue, false);
   }

   public AstDot(AstNode base, String property, boolean lvalue, boolean ignoreReturnType) {
      super(base, lvalue, true, ignoreReturnType);
      this.property = property;
   }

   protected String getProperty(Bindings bindings, ELContext context) throws ELException {
      return this.property;
   }

   public String toString() {
      return ". " + this.property;
   }

   public void appendStructure(StringBuilder b, Bindings bindings) {
      this.getChild(0).appendStructure(b, bindings);
      b.append(".");
      b.append(this.property);
   }

   public int getCardinality() {
      return 1;
   }
}
