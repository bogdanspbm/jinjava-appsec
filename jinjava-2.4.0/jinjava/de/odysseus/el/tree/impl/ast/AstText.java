package jinjava.de.odysseus.el.tree.impl.ast;

import jinjava.de.odysseus.el.misc.LocalMessages;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELException;
import jinjava.javax.el.MethodInfo;
import jinjava.javax.el.ValueReference;

public final class AstText extends AstNode {
   private final String value;

   public AstText(String value) {
      this.value = value;
   }

   public boolean isLiteralText() {
      return true;
   }

   public boolean isLeftValue() {
      return false;
   }

   public boolean isMethodInvocation() {
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

   public ValueReference getValueReference(Bindings bindings, ELContext context) {
      return null;
   }

   public Object eval(Bindings bindings, ELContext context) {
      return this.value;
   }

   public MethodInfo getMethodInfo(Bindings bindings, ELContext context, Class<?> returnType, Class<?>[] paramTypes) {
      return null;
   }

   public Object invoke(Bindings bindings, ELContext context, Class<?> returnType, Class<?>[] paramTypes, Object[] paramValues) {
      return returnType == null ? this.value : bindings.convert(this.value, returnType);
   }

   public String toString() {
      return "\"" + this.value + "\"";
   }

   public void appendStructure(StringBuilder b, Bindings bindings) {
      int end = this.value.length() - 1;

      for(int i = 0; i < end; ++i) {
         char c = this.value.charAt(i);
         if ((c == '#' || c == '$') && this.value.charAt(i + 1) == '{') {
            b.append('\\');
         }

         b.append(c);
      }

      if (end >= 0) {
         b.append(this.value.charAt(end));
      }

   }

   public int getCardinality() {
      return 0;
   }

   public AstNode getChild(int i) {
      return null;
   }
}
