package jinjava.de.odysseus.el.tree.impl.ast;

import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;

public final class AstNull extends AstLiteral {
   public Object eval(Bindings bindings, ELContext context) {
      return null;
   }

   public String toString() {
      return "null";
   }

   public void appendStructure(StringBuilder b, Bindings bindings) {
      b.append("null");
   }
}
