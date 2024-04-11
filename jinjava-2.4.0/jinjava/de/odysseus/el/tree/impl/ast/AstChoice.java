package jinjava.de.odysseus.el.tree.impl.ast;

import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELException;

public class AstChoice extends AstRightValue {
   private final AstNode question;
   private final AstNode yes;
   private final AstNode no;

   public AstChoice(AstNode question, AstNode yes, AstNode no) {
      this.question = question;
      this.yes = yes;
      this.no = no;
   }

   public Object eval(Bindings bindings, ELContext context) throws ELException {
      Boolean value = (Boolean)bindings.convert(this.question.eval(bindings, context), Boolean.class);
      return value ? this.yes.eval(bindings, context) : this.no.eval(bindings, context);
   }

   public String toString() {
      return "?";
   }

   public void appendStructure(StringBuilder b, Bindings bindings) {
      this.question.appendStructure(b, bindings);
      b.append(" ? ");
      this.yes.appendStructure(b, bindings);
      b.append(" : ");
      this.no.appendStructure(b, bindings);
   }

   public int getCardinality() {
      return 3;
   }

   public AstNode getChild(int i) {
      return i == 0 ? this.question : (i == 1 ? this.yes : (i == 2 ? this.no : null));
   }
}
