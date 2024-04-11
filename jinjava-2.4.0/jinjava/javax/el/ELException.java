package jinjava.javax.el;

public class ELException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public ELException() {
   }

   public ELException(String message) {
      super(message);
   }

   public ELException(Throwable cause) {
      super(cause);
   }

   public ELException(String message, Throwable cause) {
      super(message, cause);
   }
}
