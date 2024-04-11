package com.hubspot.jinjava.el.ext;

import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.de.odysseus.el.tree.impl.ast.AstBinary;
import jinjava.de.odysseus.el.tree.impl.ast.AstNode;
import jinjava.javax.el.ELContext;

public class OrOperator implements AstBinary.Operator {
   public static final OrOperator OP = new OrOperator();

   public Object eval(Bindings bindings, ELContext context, AstNode left, AstNode right) {
      Object leftResult = left.eval(bindings, context);
      return (Boolean)bindings.convert(leftResult, Boolean.class) ? leftResult : right.eval(bindings, context);
   }
}
