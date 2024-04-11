package com.hubspot.jinjava.tree;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.tree.output.OutputNode;
import com.hubspot.jinjava.tree.parse.Token;

public class RootNode extends Node {
   private static final long serialVersionUID = 5904181260202954424L;

   RootNode() {
      super((Token)null, 0, 0);
   }

   public OutputNode render(JinjavaInterpreter interpreter) {
      throw new UnsupportedOperationException("Please render RootNode by interpreter");
   }

   public String getName() {
      return this.getClass().getSimpleName();
   }
}
