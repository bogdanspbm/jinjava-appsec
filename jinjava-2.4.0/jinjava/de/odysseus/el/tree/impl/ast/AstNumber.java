package jinjava.de.odysseus.el.tree.impl.ast;

import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;

public final class AstNumber extends AstLiteral {
   private final Number value;

   public AstNumber(Number value) {
      this.value = value;
   }

   public Object eval(Bindings bindings, ELContext context) {
      return this.value;
   }

   public String toString() {
      return this.value.toString();
   }

   public void appendStructure(StringBuilder b, Bindings bindings) {
      b.append(this.value);
   }
}
