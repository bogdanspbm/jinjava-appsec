package com.hubspot.jinjava.tree.parse;

import com.hubspot.jinjava.interpret.TemplateSyntaxException;
import com.hubspot.jinjava.util.WhitespaceUtils;

public class TagToken extends Token {
   private static final long serialVersionUID = -4927751270481832992L;
   private String tagName;
   private String helpers;

   public TagToken(String image, int lineNumber, int startPosition) {
      super(image, lineNumber, startPosition);
   }

   public int getType() {
      return 37;
   }

   protected void parse() {
      if (this.image.length() < 4) {
         throw new TemplateSyntaxException(this.image, "Malformed tag token", this.getLineNumber(), this.getStartPosition());
      } else {
         this.content = this.image.substring(2, this.image.length() - 2);
         if (WhitespaceUtils.startsWith(this.content, "-")) {
            this.setLeftTrim(true);
            this.content = WhitespaceUtils.unwrap(this.content, "-", "");
         }

         if (WhitespaceUtils.endsWith(this.content, "-")) {
            this.setRightTrim(true);
            this.content = WhitespaceUtils.unwrap(this.content, "", "-");
         }

         int nameStart = -1;
         int pos = 0;

         for(int len = this.content.length(); pos < len; ++pos) {
            char c = this.content.charAt(pos);
            if (nameStart == -1 && Character.isJavaIdentifierStart(c)) {
               nameStart = pos;
            } else if (nameStart != -1 && !Character.isJavaIdentifierPart(c)) {
               break;
            }
         }

         if (pos < this.content.length()) {
            this.tagName = this.content.substring(nameStart, pos).toLowerCase();
            this.helpers = this.content.substring(pos);
         } else {
            this.tagName = this.content.toLowerCase().trim();
            this.helpers = "";
         }

      }
   }

   public String getTagName() {
      return this.tagName;
   }

   public String getHelpers() {
      return this.helpers;
   }

   public String toString() {
      return this.helpers.length() == 0 ? "{% " + this.tagName + " %}" : "{% " + this.tagName + " " + this.helpers + " %}";
   }
}
