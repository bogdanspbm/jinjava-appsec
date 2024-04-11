package jinjava.de.odysseus.el.tree.impl.ast;

import jinjava.de.odysseus.el.misc.LocalMessages;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELException;
import jinjava.javax.el.MethodInfo;
import jinjava.javax.el.ValueReference;

public abstract class AstRightValue extends AstNode {
   public final boolean isLiteralText() {
      return false;
   }

   public final Class<?> getType(Bindings bindings, ELContext context) {
      return null;
   }

   public final boolean isReadOnly(Bindings bindings, ELContext context) {
      return true;
   }

   public final void setValue(Bindings bindings, ELContext context, Object value) {
      throw new ELException(LocalMessages.get("error.value.set.rvalue", this.getStructuralId(bindings)));
   }

   public final MethodInfo getMethodInfo(Bindings bindings, ELContext context, Class<?> returnType, Class<?>[] paramTypes) {
      return null;
   }

   public final Object invoke(Bindings bindings, ELContext context, Class<?> returnType, Class<?>[] paramTypes, Object[] paramValues) {
      throw new ELException(LocalMessages.get("error.method.invalid", this.getStructuralId(bindings)));
   }

   public final boolean isLeftValue() {
      return false;
   }

   public boolean isMethodInvocation() {
      return false;
   }

   public final ValueReference getValueReference(Bindings bindings, ELContext context) {
      return null;
   }
}
