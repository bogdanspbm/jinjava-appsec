package com.hubspot.jinjava.tree.parse;

import com.google.common.collect.AbstractIterator;
import com.hubspot.jinjava.JinjavaConfig;
import com.hubspot.jinjava.util.CharArrayUtils;

public class TokenScanner extends AbstractIterator<Token> {
   private final JinjavaConfig config;
   private final char[] is;
   private final int length;
   private int currPost = 0;
   private int tokenStart = 0;
   private int tokenLength = 0;
   private int tokenKind = -1;
   private int lastStart = 0;
   private int inComment = 0;
   private int inRaw = 0;
   private int inBlock = 0;
   private char inQuote = 0;
   private int currLine = 1;
   private int lastNewlinePos = 0;

   public TokenScanner(String input, JinjavaConfig config) {
      this.config = config;
      this.is = input.toCharArray();
      this.length = this.is.length;
      this.currPost = 0;
      this.tokenStart = 0;
      this.tokenKind = -1;
      this.lastStart = 0;
      this.inComment = 0;
      this.inRaw = 0;
      this.inBlock = 0;
      this.inQuote = 0;
      this.currLine = 1;
      this.lastNewlinePos = 0;
   }

   private Token getNextToken() {
      while(this.currPost < this.length) {
         char c = this.is[this.currPost++];
         if (this.currPost == this.length) {
            return this.getEndToken();
         }

         if (this.inBlock > 0) {
            if (this.inQuote != 0) {
               if (this.inQuote == c) {
                  this.inQuote = 0;
                  continue;
               }

               if (c == '\\') {
                  ++this.currPost;
               }
               continue;
            }

            if (this.inQuote == 0 && (c == '\'' || c == '"')) {
               this.inQuote = c;
               continue;
            }
         }

         switch(c) {
         case '\n':
            ++this.currLine;
            this.lastNewlinePos = this.currPost;
            if (this.inComment <= 0 && this.inBlock <= 0) {
               this.tokenKind = 0;
            }
            break;
         case '#':
            if (!this.matchToken(c)) {
               break;
            }

            if (this.currPost < this.length) {
               c = this.is[this.currPost];
               if (c != '}') {
                  break;
               }

               this.inComment = 0;
               this.tokenLength = this.currPost - this.tokenStart + 1;
               if (this.tokenLength <= 0) {
                  break;
               }

               this.lastStart = this.tokenStart;
               this.tokenStart = ++this.currPost;
               this.tokenKind = 0;
               return this.newToken(35);
            }

            return this.getEndToken();
         case '%':
         case '}':
            if (this.inComment > 0 || !this.matchToken(c)) {
               break;
            }

            if (this.currPost < this.length) {
               c = this.is[this.currPost];
               if (c != '}') {
                  break;
               }

               this.inBlock = 0;
               this.tokenLength = this.currPost - this.tokenStart + 1;
               if (this.tokenLength <= 0) {
                  break;
               }

               this.lastStart = this.tokenStart;
               this.tokenStart = ++this.currPost;
               int kind = this.tokenKind;
               this.tokenKind = 0;
               return this.newToken(kind);
            }

            return this.getEndToken();
         case '{':
            if (this.currPost < this.length) {
               c = this.is[this.currPost];
               switch(c) {
               case '#':
                  if (this.inComment == 1 || this.inRaw == 1) {
                     continue;
                  }

                  this.inComment = 1;
                  this.tokenLength = this.currPost - this.tokenStart - 1;
                  if (this.tokenLength > 0) {
                     this.lastStart = this.tokenStart;
                     this.tokenStart = --this.currPost;
                     this.tokenKind = c;
                     this.inComment = 0;
                     return this.newToken(0);
                  }

                  this.tokenKind = c;
                  continue;
               case '%':
               case '{':
                  if (this.inComment <= 0 && (this.inRaw <= 0 || c != '{' && this.isEndRaw()) && (this.matchToken(c) || this.tokenKind <= 0) && this.inBlock++ <= 0) {
                     this.tokenLength = this.currPost - this.tokenStart - 1;
                     if (this.tokenLength > 0) {
                        this.lastStart = this.tokenStart;
                        this.tokenStart = --this.currPost;
                        this.tokenKind = c;
                        return this.newToken(0);
                     }

                     this.tokenKind = c;
                  }
               default:
                  continue;
               }
            }

            return this.getEndToken();
         default:
            if (this.tokenKind == -1) {
               this.tokenKind = 0;
            }
         }
      }

      return null;
   }

   private boolean isEndRaw() {
      int pos = this.currPost + 1;

      while(pos < this.length && Character.isWhitespace(this.is[pos++])) {
      }

      return pos + 5 >= this.length ? false : CharArrayUtils.charArrayRegionMatches(this.is, pos - 1, "endraw");
   }

   private Token getEndToken() {
      this.tokenLength = this.currPost - this.tokenStart;
      int type = 0;
      if (this.inComment > 0) {
         type = 35;
      }

      return Token.newToken(type, String.valueOf(this.is, this.tokenStart, this.tokenLength), this.currLine, this.tokenStart - this.lastNewlinePos + 1);
   }

   private Token newToken(int kind) {
      Token t = Token.newToken(kind, String.valueOf(this.is, this.lastStart, this.tokenLength), this.currLine, this.lastStart - this.lastNewlinePos + 1);
      if (t instanceof TagToken) {
         if (this.config.isTrimBlocks() && this.currPost < this.length && this.is[this.currPost] == '\n') {
            this.lastNewlinePos = this.currPost++;
            ++this.tokenStart;
         }

         TagToken tt = (TagToken)t;
         if ("raw".equals(tt.getTagName())) {
            this.inRaw = 1;
            return tt;
         }

         if ("endraw".equals(tt.getTagName())) {
            this.inRaw = 0;
            return tt;
         }
      }

      return this.inRaw > 0 && t.getType() != 0 ? Token.newToken(0, t.image, this.currLine, this.tokenStart) : t;
   }

   private boolean matchToken(char kind) {
      if (kind == '{') {
         return this.tokenKind == 125;
      } else if (kind == '}') {
         return this.tokenKind == 123;
      } else {
         return kind == this.tokenKind;
      }
   }

   protected Token computeNext() {
      Token t = this.getNextToken();
      return t == null ? (Token)this.endOfData() : t;
   }
}
