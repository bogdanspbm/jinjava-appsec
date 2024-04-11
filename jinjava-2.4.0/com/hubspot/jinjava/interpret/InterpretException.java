package com.hubspot.jinjava.interpret;

public class InterpretException extends RuntimeException {
   private static final long serialVersionUID = -3471306977643126138L;
   private int lineNumber;
   private int startPosition;

   public InterpretException(String msg) {
      super(msg);
      this.lineNumber = -1;
      this.startPosition = -1;
   }

   public InterpretException(String msg, Throwable e) {
      super(msg, e);
      this.lineNumber = -1;
      this.startPosition = -1;
   }

   public InterpretException(String msg, int lineNumber) {
      this(msg, lineNumber, -1);
   }

   public InterpretException(String msg, int lineNumber, int startPosition) {
      this(msg);
      this.lineNumber = lineNumber;
      this.startPosition = startPosition;
   }

   public InterpretException(String msg, Throwable e, int lineNumber, int startPosition) {
      this(msg, e);
      this.lineNumber = lineNumber;
      this.startPosition = startPosition;
   }

   public InterpretException(String msg, Throwable throwable, int lineNumber) {
      this(msg, throwable, lineNumber, -1);
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public int getStartPosition() {
      return this.startPosition;
   }
}
