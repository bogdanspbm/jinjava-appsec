package jinjava.de.odysseus.el.tree.impl.ast;

import jinjava.de.odysseus.el.misc.BooleanOperations;
import jinjava.de.odysseus.el.misc.NumberOperations;
import jinjava.de.odysseus.el.misc.TypeConverter;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;

public class AstBinary extends AstRightValue {
   public static final AstBinary.Operator ADD = new AstBinary.SimpleOperator() {
      public Object apply(TypeConverter converter, Object o1, Object o2) {
         return NumberOperations.add(converter, o1, o2);
      }

      public String toString() {
         return "+";
      }
   };
   public static final AstBinary.Operator AND = new AstBinary.Operator() {
      public Object eval(Bindings bindings, ELContext context, AstNode left, AstNode right) {
         Boolean l = (Boolean)bindings.convert(left.eval(bindings, context), Boolean.class);
         return Boolean.TRUE.equals(l) ? (Boolean)bindings.convert(right.eval(bindings, context), Boolean.class) : Boolean.FALSE;
      }

      public String toString() {
         return "&&";
      }
   };
   public static final AstBinary.Operator DIV = new AstBinary.SimpleOperator() {
      public Object apply(TypeConverter converter, Object o1, Object o2) {
         return NumberOperations.div(converter, o1, o2);
      }

      public String toString() {
         return "/";
      }
   };
   public static final AstBinary.Operator EQ = new AstBinary.SimpleOperator() {
      public Object apply(TypeConverter converter, Object o1, Object o2) {
         return BooleanOperations.eq(converter, o1, o2);
      }

      public String toString() {
         return "==";
      }
   };
   public static final AstBinary.Operator GE = new AstBinary.SimpleOperator() {
      public Object apply(TypeConverter converter, Object o1, Object o2) {
         return BooleanOperations.ge(converter, o1, o2);
      }

      public String toString() {
         return ">=";
      }
   };
   public static final AstBinary.Operator GT = new AstBinary.SimpleOperator() {
      public Object apply(TypeConverter converter, Object o1, Object o2) {
         return BooleanOperations.gt(converter, o1, o2);
      }

      public String toString() {
         return ">";
      }
   };
   public static final AstBinary.Operator LE = new AstBinary.SimpleOperator() {
      public Object apply(TypeConverter converter, Object o1, Object o2) {
         return BooleanOperations.le(converter, o1, o2);
      }

      public String toString() {
         return "<=";
      }
   };
   public static final AstBinary.Operator LT = new AstBinary.SimpleOperator() {
      public Object apply(TypeConverter converter, Object o1, Object o2) {
         return BooleanOperations.lt(converter, o1, o2);
      }

      public String toString() {
         return "<";
      }
   };
   public static final AstBinary.Operator MOD = new AstBinary.SimpleOperator() {
      public Object apply(TypeConverter converter, Object o1, Object o2) {
         return NumberOperations.mod(converter, o1, o2);
      }

      public String toString() {
         return "%";
      }
   };
   public static final AstBinary.Operator MUL = new AstBinary.SimpleOperator() {
      public Object apply(TypeConverter converter, Object o1, Object o2) {
         return NumberOperations.mul(converter, o1, o2);
      }

      public String toString() {
         return "*";
      }
   };
   public static final AstBinary.Operator NE = new AstBinary.SimpleOperator() {
      public Object apply(TypeConverter converter, Object o1, Object o2) {
         return BooleanOperations.ne(converter, o1, o2);
      }

      public String toString() {
         return "!=";
      }
   };
   public static final AstBinary.Operator OR = new AstBinary.Operator() {
      public Object eval(Bindings bindings, ELContext context, AstNode left, AstNode right) {
         Boolean l = (Boolean)bindings.convert(left.eval(bindings, context), Boolean.class);
         return Boolean.TRUE.equals(l) ? Boolean.TRUE : (Boolean)bindings.convert(right.eval(bindings, context), Boolean.class);
      }

      public String toString() {
         return "||";
      }
   };
   public static final AstBinary.Operator SUB = new AstBinary.SimpleOperator() {
      public Object apply(TypeConverter converter, Object o1, Object o2) {
         return NumberOperations.sub(converter, o1, o2);
      }

      public String toString() {
         return "-";
      }
   };
   private final AstBinary.Operator operator;
   private final AstNode left;
   private final AstNode right;

   public AstBinary(AstNode left, AstNode right, AstBinary.Operator operator) {
      this.left = left;
      this.right = right;
      this.operator = operator;
   }

   public AstBinary.Operator getOperator() {
      return this.operator;
   }

   public Object eval(Bindings bindings, ELContext context) {
      return this.operator.eval(bindings, context, this.left, this.right);
   }

   public String toString() {
      return "'" + this.operator.toString() + "'";
   }

   public void appendStructure(StringBuilder b, Bindings bindings) {
      this.left.appendStructure(b, bindings);
      b.append(' ');
      b.append(this.operator);
      b.append(' ');
      this.right.appendStructure(b, bindings);
   }

   public int getCardinality() {
      return 2;
   }

   public AstNode getChild(int i) {
      return i == 0 ? this.left : (i == 1 ? this.right : null);
   }

   public abstract static class SimpleOperator implements AstBinary.Operator {
      public Object eval(Bindings bindings, ELContext context, AstNode left, AstNode right) {
         return this.apply(bindings, left.eval(bindings, context), right.eval(bindings, context));
      }

      protected abstract Object apply(TypeConverter var1, Object var2, Object var3);
   }

   public interface Operator {
      Object eval(Bindings var1, ELContext var2, AstNode var3, AstNode var4);
   }
}
