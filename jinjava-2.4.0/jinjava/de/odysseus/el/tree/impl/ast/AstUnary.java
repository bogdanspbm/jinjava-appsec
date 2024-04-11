package jinjava.de.odysseus.el.tree.impl.ast;

import jinjava.de.odysseus.el.misc.BooleanOperations;
import jinjava.de.odysseus.el.misc.NumberOperations;
import jinjava.de.odysseus.el.misc.TypeConverter;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELException;

public class AstUnary extends AstRightValue {
   public static final AstUnary.Operator EMPTY = new AstUnary.SimpleOperator() {
      public Object apply(TypeConverter converter, Object o) {
         return BooleanOperations.empty(converter, o);
      }

      public String toString() {
         return "empty";
      }
   };
   public static final AstUnary.Operator NEG = new AstUnary.SimpleOperator() {
      public Object apply(TypeConverter converter, Object o) {
         return NumberOperations.neg(converter, o);
      }

      public String toString() {
         return "-";
      }
   };
   public static final AstUnary.Operator NOT = new AstUnary.SimpleOperator() {
      public Object apply(TypeConverter converter, Object o) {
         return !(Boolean)converter.convert(o, Boolean.class);
      }

      public String toString() {
         return "!";
      }
   };
   private final AstUnary.Operator operator;
   private final AstNode child;

   public AstUnary(AstNode child, AstUnary.Operator operator) {
      this.child = child;
      this.operator = operator;
   }

   public AstUnary.Operator getOperator() {
      return this.operator;
   }

   public Object eval(Bindings bindings, ELContext context) throws ELException {
      return this.operator.eval(bindings, context, this.child);
   }

   public String toString() {
      return "'" + this.operator.toString() + "'";
   }

   public void appendStructure(StringBuilder b, Bindings bindings) {
      b.append(this.operator);
      b.append(' ');
      this.child.appendStructure(b, bindings);
   }

   public int getCardinality() {
      return 1;
   }

   public AstNode getChild(int i) {
      return i == 0 ? this.child : null;
   }

   public abstract static class SimpleOperator implements AstUnary.Operator {
      public Object eval(Bindings bindings, ELContext context, AstNode node) {
         return this.apply(bindings, node.eval(bindings, context));
      }

      protected abstract Object apply(TypeConverter var1, Object var2);
   }

   public interface Operator {
      Object eval(Bindings var1, ELContext var2, AstNode var3);
   }
}
