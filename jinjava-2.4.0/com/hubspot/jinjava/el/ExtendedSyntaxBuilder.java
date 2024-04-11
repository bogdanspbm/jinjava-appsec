package com.hubspot.jinjava.el;

import com.hubspot.jinjava.el.ext.ExtendedParser;
import jinjava.de.odysseus.el.tree.impl.Builder;
import jinjava.de.odysseus.el.tree.impl.Parser;

public class ExtendedSyntaxBuilder extends Builder {
   private static final long serialVersionUID = 1L;

   public ExtendedSyntaxBuilder() {
   }

   public ExtendedSyntaxBuilder(Builder.Feature... features) {
      super(features);
   }

   protected Parser createParser(String expression) {
      return new ExtendedParser(this, expression);
   }
}
