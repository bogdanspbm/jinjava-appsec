package com.hubspot.jinjava.el.ext;

import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.de.odysseus.el.tree.impl.ast.AstIdentifier;
import jinjava.de.odysseus.el.tree.impl.ast.AstLiteral;
import jinjava.de.odysseus.el.tree.impl.ast.AstNode;
import jinjava.javax.el.ELContext;

public class AstNamedParameter extends AstLiteral {
   private final AstIdentifier name;
   private final AstNode value;

   public AstNamedParameter(AstIdentifier name, AstNode value) {
      this.name = name;
      this.value = value;
   }

   public Object eval(Bindings bindings, ELContext context) {
      return new NamedParameter(this.name.getName(), this.value.eval(bindings, context));
   }

   public void appendStructure(StringBuilder builder, Bindings bindings) {
      throw new UnsupportedOperationException("appendStructure not implemented in " + this.getClass().getSimpleName());
   }
}
