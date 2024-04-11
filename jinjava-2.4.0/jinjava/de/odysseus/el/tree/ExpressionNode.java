package jinjava.de.odysseus.el.tree;

import jinjava.javax.el.ELContext;
import jinjava.javax.el.MethodInfo;
import jinjava.javax.el.ValueReference;

public interface ExpressionNode extends Node {
   boolean isLiteralText();

   boolean isLeftValue();

   boolean isMethodInvocation();

   Object getValue(Bindings var1, ELContext var2, Class<?> var3);

   ValueReference getValueReference(Bindings var1, ELContext var2);

   Class<?> getType(Bindings var1, ELContext var2);

   boolean isReadOnly(Bindings var1, ELContext var2);

   void setValue(Bindings var1, ELContext var2, Object var3);

   MethodInfo getMethodInfo(Bindings var1, ELContext var2, Class<?> var3, Class<?>[] var4);

   Object invoke(Bindings var1, ELContext var2, Class<?> var3, Class<?>[] var4, Object[] var5);

   String getStructuralId(Bindings var1);
}
