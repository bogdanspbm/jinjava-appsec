package jinjava.de.odysseus.el;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
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
import jinjava.javax.el.ValueExpression;
import jinjava.javax.el.ValueReference;
import jinjava.javax.el.VariableMapper;

public final class TreeValueExpression extends ValueExpression {
   private static final long serialVersionUID = 1L;
   private final TreeBuilder builder;
   private final Bindings bindings;
   private final String expr;
   private final Class<?> type;
   private final boolean deferred;
   private transient ExpressionNode node;
   private String structure;

   public TreeValueExpression(TreeStore store, FunctionMapper functions, VariableMapper variables, TypeConverter converter, String expr, Class<?> type) {
      Tree tree = store.get(expr);
      this.builder = store.getBuilder();
      this.bindings = tree.bind(functions, variables, converter);
      this.expr = expr;
      this.type = type;
      this.node = tree.getRoot();
      this.deferred = tree.isDeferred();
      if (type == null) {
         throw new NullPointerException(LocalMessages.get("error.value.notype"));
      }
   }

   private String getStructuralId() {
      if (this.structure == null) {
         this.structure = this.node.getStructuralId(this.bindings);
      }

      return this.structure;
   }

   public Class<?> getExpectedType() {
      return this.type;
   }

   public String getExpressionString() {
      return this.expr;
   }

   public Class<?> getType(ELContext context) throws ELException {
      return this.node.getType(this.bindings, context);
   }

   public Object getValue(ELContext context) throws ELException {
      return this.node.getValue(this.bindings, context, this.type);
   }

   public boolean isReadOnly(ELContext context) throws ELException {
      return this.node.isReadOnly(this.bindings, context);
   }

   public void setValue(ELContext context, Object value) throws ELException {
      this.node.setValue(this.bindings, context, value);
   }

   public boolean isLiteralText() {
      return this.node.isLiteralText();
   }

   public ValueReference getValueReference(ELContext context) {
      return this.node.getValueReference(this.bindings, context);
   }

   public boolean isLeftValue() {
      return this.node.isLeftValue();
   }

   public boolean isDeferred() {
      return this.deferred;
   }

   public boolean equals(Object obj) {
      if (obj != null && obj.getClass() == this.getClass()) {
         TreeValueExpression other = (TreeValueExpression)obj;
         if (!this.builder.equals(other.builder)) {
            return false;
         } else if (this.type != other.type) {
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
      return "TreeValueExpression(" + this.expr + ")";
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
