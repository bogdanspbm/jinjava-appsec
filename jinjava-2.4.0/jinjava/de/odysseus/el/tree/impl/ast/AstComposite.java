package jinjava.de.odysseus.el.tree.impl.ast;

import java.util.List;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;

public class AstComposite extends AstRightValue {
   private final List<AstNode> nodes;

   public AstComposite(List<AstNode> nodes) {
      this.nodes = nodes;
   }

   public Object eval(Bindings bindings, ELContext context) {
      StringBuilder b = new StringBuilder(16);

      for(int i = 0; i < this.getCardinality(); ++i) {
         b.append((String)bindings.convert(((AstNode)this.nodes.get(i)).eval(bindings, context), String.class));
      }

      return b.toString();
   }

   public String toString() {
      return "composite";
   }

   public void appendStructure(StringBuilder b, Bindings bindings) {
      for(int i = 0; i < this.getCardinality(); ++i) {
         ((AstNode)this.nodes.get(i)).appendStructure(b, bindings);
      }

   }

   public int getCardinality() {
      return this.nodes.size();
   }

   public AstNode getChild(int i) {
      return (AstNode)this.nodes.get(i);
   }
}
