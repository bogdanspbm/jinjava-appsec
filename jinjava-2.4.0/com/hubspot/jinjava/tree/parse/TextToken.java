package com.hubspot.jinjava.tree.parse;

import org.apache.commons.lang3.StringUtils;

public class TextToken extends Token {
   private static final long serialVersionUID = -6168990984496468543L;

   public TextToken(String image, int lineNumber, int startPosition) {
      super(image, lineNumber, startPosition);
   }

   public int getType() {
      return 0;
   }

   protected void parse() {
      this.content = this.image;
   }

   public boolean isBlank() {
      return StringUtils.isBlank(this.content);
   }

   public String trim() {
      return this.content.trim();
   }

   public String output() {
      if (this.isLeftTrim() && this.isRightTrim()) {
         return this.trim();
      } else if (this.isLeftTrim()) {
         return StringUtils.stripStart(this.content, (String)null);
      } else {
         return this.isRightTrim() ? StringUtils.stripEnd(this.content, (String)null) : this.content;
      }
   }

   public String toString() {
      return this.isBlank() ? "{~ ~}" : "{~ " + this.content + " ~}";
   }
}
