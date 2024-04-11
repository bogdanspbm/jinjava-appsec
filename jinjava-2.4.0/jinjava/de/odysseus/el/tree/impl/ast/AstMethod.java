package jinjava.de.odysseus.el.tree.impl.ast;

import jinjava.de.odysseus.el.misc.LocalMessages;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.de.odysseus.el.tree.Node;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELException;
import jinjava.javax.el.MethodInfo;
import jinjava.javax.el.MethodNotFoundException;
import jinjava.javax.el.PropertyNotFoundException;
import jinjava.javax.el.ValueReference;

public class AstMethod extends AstNode {
   private final AstProperty property;
   private final AstParameters params;

   public AstMethod(AstProperty property, AstParameters params) {
      this.property = property;
      this.params = params;
   }

   public boolean isLiteralText() {
      return false;
   }

   public Class<?> getType(Bindings bindings, ELContext context) {
      return null;
   }

   public boolean isReadOnly(Bindings bindings, ELContext context) {
      return true;
   }

   public void setValue(Bindings bindings, ELContext context, Object value) {
      throw new ELException(LocalMessages.get("error.value.set.rvalue", this.getStructuralId(bindings)));
   }

   public MethodInfo getMethodInfo(Bindings bindings, ELContext context, Class<?> returnType, Class<?>[] paramTypes) {
      return null;
   }

   public boolean isLeftValue() {
      return false;
   }

   public boolean isMethodInvocation() {
      return true;
   }

   public final ValueReference getValueReference(Bindings bindings, ELContext context) {
      return null;
   }

   public void appendStructure(StringBuilder builder, Bindings bindings) {
      this.property.appendStructure(builder, bindings);
      this.params.appendStructure(builder, bindings);
   }

   protected Object eval(Bindings bindings, ELContext context, boolean answerNullIfBaseIsNull) {
      Object base = this.property.getPrefix().eval(bindings, context);
      if (base == null) {
         if (answerNullIfBaseIsNull) {
            return null;
         } else {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", this.property.getPrefix()));
         }
      } else {
         Object method = this.property.getProperty(bindings, context);
         if (method == null) {
            throw new PropertyNotFoundException(LocalMessages.get("error.property.method.notfound", "null", base));
         } else {
            String name = (String)bindings.convert(method, String.class);
            context.setPropertyResolved(false);
            Object result = context.getELResolver().invoke(context, base, name, (Class[])null, this.params.eval(bindings, context));
            if (!context.isPropertyResolved()) {
               throw new MethodNotFoundException(LocalMessages.get("error.property.method.notfound", name, base.getClass()));
            } else {
               return result;
            }
         }
      }
   }

   public Object eval(Bindings bindings, ELContext context) {
      return this.eval(bindings, context, true);
   }

   public Object invoke(Bindings bindings, ELContext context, Class<?> returnType, Class<?>[] paramTypes, Object[] paramValues) {
      return this.eval(bindings, context, false);
   }

   public int getCardinality() {
      return 2;
   }

   public Node getChild(int i) {
      return (Node)(i == 0 ? this.property : (i == 1 ? this.params : null));
   }

   public String toString() {
      return "<method>";
   }
}
