package jinjava.de.odysseus.el.tree.impl.ast;

import java.util.List;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;

public class AstParameters extends AstRightValue {
   private final List<AstNode> nodes;

   public AstParameters(List<AstNode> nodes) {
      this.nodes = nodes;
   }

   public Object[] eval(Bindings bindings, ELContext context) {
      Object[] result = new Object[this.nodes.size()];

      for(int i = 0; i < this.nodes.size(); ++i) {
         result[i] = ((AstNode)this.nodes.get(i)).eval(bindings, context);
      }

      return result;
   }

   public String toString() {
      return "(...)";
   }

   public void appendStructure(StringBuilder builder, Bindings bindings) {
      builder.append("(");

      for(int i = 0; i < this.nodes.size(); ++i) {
         if (i > 0) {
            builder.append(", ");
         }

         ((AstNode)this.nodes.get(i)).appendStructure(builder, bindings);
      }

      builder.append(")");
   }

   public int getCardinality() {
      return this.nodes.size();
   }

   public AstNode getChild(int i) {
      return (AstNode)this.nodes.get(i);
   }
}
