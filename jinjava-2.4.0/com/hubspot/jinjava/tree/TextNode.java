package com.hubspot.jinjava.tree;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.tree.output.OutputNode;
import com.hubspot.jinjava.tree.output.RenderedOutputNode;
import com.hubspot.jinjava.tree.parse.TextToken;

public class TextNode extends Node {
   private static final long serialVersionUID = 127827773323298439L;
   private final TextToken master;

   public TextNode(TextToken token) {
      super(token, token.getLineNumber(), token.getStartPosition());
      this.master = token;
   }

   public OutputNode render(JinjavaInterpreter interpreter) {
      return new RenderedOutputNode(this.master.output());
   }

   public String toString() {
      return this.master.toString();
   }

   public String getName() {
      return this.getClass().getSimpleName();
   }
}
