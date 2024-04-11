package jinjava.de.odysseus.el.tree.impl.ast;

import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;

public final class AstString extends AstLiteral {
   private final String value;

   public AstString(String value) {
      this.value = value;
   }

   public Object eval(Bindings bindings, ELContext context) {
      return this.value;
   }

   public String toString() {
      return "\"" + this.value + "\"";
   }

   public void appendStructure(StringBuilder b, Bindings bindings) {
      b.append("'");
      int length = this.value.length();

      for(int i = 0; i < length; ++i) {
         char c = this.value.charAt(i);
         if (c == '\\' || c == '\'') {
            b.append('\\');
         }

         b.append(c);
      }

      b.append("'");
   }
}
