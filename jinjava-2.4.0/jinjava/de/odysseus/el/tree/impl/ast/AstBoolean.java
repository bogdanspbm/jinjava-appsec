package jinjava.de.odysseus.el.tree.impl.ast;

import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;

public final class AstBoolean extends AstLiteral {
   private final boolean value;

   public AstBoolean(boolean value) {
      this.value = value;
   }

   public Object eval(Bindings bindings, ELContext context) {
      return this.value;
   }

   public String toString() {
      return String.valueOf(this.value);
   }

   public void appendStructure(StringBuilder b, Bindings bindings) {
      b.append(this.value);
   }
}
