package com.hubspot.jinjava.tree.parse;

import com.hubspot.jinjava.interpret.UnexpectedTokenException;
import java.io.Serializable;

public abstract class Token implements Serializable {
   private static final long serialVersionUID = 3359084948763661809L;
   protected final String image;
   protected String content;
   protected final int lineNumber;
   protected final int startPosition;
   private boolean leftTrim;
   private boolean rightTrim;
   private boolean rightTrimAfterEnd;

   public Token(String image, int lineNumber, int startPosition) {
      this.image = image;
      this.lineNumber = lineNumber;
      this.startPosition = startPosition;
      this.parse();
   }

   public Token(String image, int lineNumber) {
      this(image, lineNumber, -1);
   }

   public String getImage() {
      return this.image;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public boolean isLeftTrim() {
      return this.leftTrim;
   }

   public boolean isRightTrim() {
      return this.rightTrim;
   }

   public boolean isRightTrimAfterEnd() {
      return this.rightTrimAfterEnd;
   }

   public void setLeftTrim(boolean leftTrim) {
      this.leftTrim = leftTrim;
   }

   public void setRightTrim(boolean rightTrim) {
      this.rightTrim = rightTrim;
   }

   public void setRightTrimAfterEnd(boolean rightTrimAfterEnd) {
      this.rightTrimAfterEnd = rightTrimAfterEnd;
   }

   public int getStartPosition() {
      return this.startPosition;
   }

   public String toString() {
      return this.image;
   }

   protected abstract void parse();

   public abstract int getType();

   static Token newToken(int tokenKind, String image, int lineNumber, int startPosition) {
      switch(tokenKind) {
      case 0:
         return new TextToken(image, lineNumber, startPosition);
      case 35:
         return new NoteToken(image, lineNumber, startPosition);
      case 37:
         return new TagToken(image, lineNumber, startPosition);
      case 123:
         return new ExpressionToken(image, lineNumber, startPosition);
      default:
         throw new UnexpectedTokenException(String.valueOf((char)tokenKind), lineNumber, startPosition);
      }
   }
}
