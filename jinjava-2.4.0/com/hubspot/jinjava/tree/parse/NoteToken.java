package com.hubspot.jinjava.tree.parse;

public class NoteToken extends Token {
   private static final long serialVersionUID = -3859011447900311329L;

   public NoteToken(String image, int lineNumber, int startPosition) {
      super(image, lineNumber, startPosition);
   }

   public int getType() {
      return 35;
   }

   protected void parse() {
      this.content = "";
   }

   public String toString() {
      return "{# ----comment---- #}";
   }
}
