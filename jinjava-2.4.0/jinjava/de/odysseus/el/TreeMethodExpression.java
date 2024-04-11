package jinjava.de.odysseus.el;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import jinjava.de.odysseus.el.misc.LocalMessages;
import jinjava.de.odysseus.el.misc.TypeConverter;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.de.odysseus.el.tree.ExpressionNode;
import jinjava.de.odysseus.el.tree.NodePrinter;
import jinjava.de.odysseus.el.tree.Tree;
import jinjava.de.odysseus.el.tree.TreeBuilder;
import jinjava.de.odysseus.el.tree.TreeStore;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELException;
import jinjava.javax.el.FunctionMapper;
import jinjava.javax.el.MethodExpression;
import jinjava.javax.el.MethodInfo;
import jinjava.javax.el.VariableMapper;

public final class TreeMethodExpression extends MethodExpression {
   private static final long serialVersionUID = 1L;
   private final TreeBuilder builder;
   private final Bindings bindings;
   private final String expr;
   private final Class<?> type;
   private final Class<?>[] types;
   private final boolean deferred;
   private transient ExpressionNode node;
   private String structure;

   public TreeMethodExpression(TreeStore store, FunctionMapper functions, VariableMapper variables, TypeConverter converter, String expr, Class<?> returnType, Class<?>[] paramTypes) {
      Tree tree = store.get(expr);
      this.builder = store.getBuilder();
      this.bindings = tree.bind(functions, variables, converter);
      this.expr = expr;
      this.type = returnType;
      this.types = paramTypes;
      this.node = tree.getRoot();
      this.deferred = tree.isDeferred();
      if (this.node.isLiteralText()) {
         if (returnType == Void.TYPE || returnType == Void.class) {
            throw new ELException(LocalMessages.get("error.method.literal.void", expr));
         }
      } else if (!this.node.isMethodInvocation()) {
         if (!this.node.isLeftValue()) {
            throw new ELException(LocalMessages.get("error.method.invalid", expr));
         }

         if (paramTypes == null) {
            throw new NullPointerException(LocalMessages.get("error.method.notypes"));
         }
      }

   }

   private String getStructuralId() {
      if (this.structure == null) {
         this.structure = this.node.getStructuralId(this.bindings);
      }

      return this.structure;
   }

   public MethodInfo getMethodInfo(ELContext context) throws ELException {
      return this.node.getMethodInfo(this.bindings, context, this.type, this.types);
   }

   public String getExpressionString() {
      return this.expr;
   }

   public Object invoke(ELContext context, Object[] paramValues) throws ELException {
      return this.node.invoke(this.bindings, context, this.type, this.types, paramValues);
   }

   public boolean isLiteralText() {
      return this.node.isLiteralText();
   }

   public boolean isParmetersProvided() {
      return this.node.isMethodInvocation();
   }

   public boolean isDeferred() {
      return this.deferred;
   }

   public boolean equals(Object obj) {
      if (obj != null && obj.getClass() == this.getClass()) {
         TreeMethodExpression other = (TreeMethodExpression)obj;
         if (!this.builder.equals(other.builder)) {
            return false;
         } else if (this.type != other.type) {
            return false;
         } else if (!Arrays.equals(this.types, other.types)) {
            return false;
         } else {
            return this.getStructuralId().equals(other.getStructuralId()) && this.bindings.equals(other.bindings);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.getStructuralId().hashCode();
   }

   public String toString() {
      return "TreeMethodExpression(" + this.expr + ")";
   }

   public void dump(PrintWriter writer) {
      NodePrinter.dump(writer, this.node);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();

      try {
         this.node = this.builder.build(this.expr).getRoot();
      } catch (ELException var3) {
         throw new IOException(var3.getMessage());
      }
   }
}
