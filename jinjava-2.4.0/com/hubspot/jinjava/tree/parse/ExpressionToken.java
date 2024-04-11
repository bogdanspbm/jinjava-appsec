package com.hubspot.jinjava.tree.parse;

import com.hubspot.jinjava.util.WhitespaceUtils;
import org.apache.commons.lang3.StringUtils;

public class ExpressionToken extends Token {
   private static final long serialVersionUID = 6336768632140743908L;
   private String expr;

   public ExpressionToken(String image, int lineNumber, int startPosition) {
      super(image, lineNumber, startPosition);
   }

   public String toString() {
      return "{{ " + this.getExpr() + "}}";
   }

   public int getType() {
      return 123;
   }

   protected void parse() {
      this.expr = WhitespaceUtils.unwrap(this.image, "{{", "}}");
      if (WhitespaceUtils.startsWith(this.expr, "-")) {
         this.setLeftTrim(true);
         this.expr = WhitespaceUtils.unwrap(this.expr, "-", "");
      }

      if (WhitespaceUtils.endsWith(this.expr, "-")) {
         this.setRightTrim(true);
         this.expr = WhitespaceUtils.unwrap(this.expr, "", "-");
      }

      this.expr = StringUtils.trimToEmpty(this.expr);
   }

   public String getExpr() {
      return this.expr;
   }
}
