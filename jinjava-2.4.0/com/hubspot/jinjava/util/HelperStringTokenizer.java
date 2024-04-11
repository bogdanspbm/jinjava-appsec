package com.hubspot.jinjava.util;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import java.util.List;

public class HelperStringTokenizer extends AbstractIterator<String> {
   private final char[] value;
   private final int length;
   private int currPost = 0;
   private int tokenStart = 0;
   private char lastChar = ' ';
   private boolean useComma = false;
   private char quoteChar = 0;
   private boolean inQuote = false;

   public HelperStringTokenizer(String s) {
      this.value = s.toCharArray();
      this.length = this.value.length;
   }

   public HelperStringTokenizer splitComma(boolean onOrOff) {
      this.useComma = onOrOff;
      return this;
   }

   protected String computeNext() {
      while(true) {
         if (this.currPost < this.length) {
            String token = this.makeToken();
            this.lastChar = this.value[this.currPost - 1];
            if (token == null) {
               continue;
            }

            return token;
         }

         this.endOfData();
         return null;
      }
   }

   private String makeToken() {
      char c = this.value[this.currPost++];
      if (c == '"' || c == '\'') {
         if (this.inQuote) {
            if (this.quoteChar == c) {
               this.inQuote = false;
            }
         } else {
            this.inQuote = true;
            this.quoteChar = c;
         }
      }

      if ((Character.isWhitespace(c) || this.useComma && c == ',') && !this.inQuote) {
         return this.newToken();
      } else {
         return this.currPost == this.length ? this.getEndToken() : null;
      }
   }

   private String getEndToken() {
      return String.copyValueOf(this.value, this.tokenStart, this.currPost - this.tokenStart);
   }

   private String newToken() {
      int lastStart = this.tokenStart;
      this.tokenStart = this.currPost;
      return !Character.isWhitespace(this.lastChar) && (!this.useComma || this.lastChar != ',') ? String.copyValueOf(this.value, lastStart, this.currPost - lastStart - 1) : null;
   }

   public List<String> allTokens() {
      return Lists.newArrayList(this);
   }
}
