package com.hubspot.jinjava.tree;

import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.tag.Tag;
import com.hubspot.jinjava.tree.output.OutputNode;
import com.hubspot.jinjava.tree.parse.TagToken;

public class TagNode extends Node {
   private static final long serialVersionUID = -6971280448795354252L;
   private final Tag tag;
   private final TagToken master;
   private final String endName;

   public TagNode(Tag tag, TagToken token) {
      super(token, token.getLineNumber(), token.getStartPosition());
      this.master = token;
      this.tag = tag;
      this.endName = tag.getEndTagName();
   }

   private TagNode(TagNode n) {
      super(n.master, n.getLineNumber(), n.getStartPosition());
      this.tag = n.tag;
      this.master = n.master;
      this.endName = n.endName;
   }

   public OutputNode render(JinjavaInterpreter interpreter) {
      try {
         return this.tag.interpretOutput(this, interpreter);
      } catch (InterpretException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new InterpretException("Error rendering tag", var4, this.master.getLineNumber(), this.master.getStartPosition());
      }
   }

   public String toString() {
      return this.master.toString();
   }

   public String getName() {
      return this.master.getTagName();
   }

   public String getEndName() {
      return this.endName;
   }

   public String getHelpers() {
      return this.master.getHelpers();
   }

   public Tag getTag() {
      return this.tag;
   }
}
