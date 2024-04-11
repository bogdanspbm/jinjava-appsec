package jinjava.javax.el;

public class MethodNotFoundException extends ELException {
   private static final long serialVersionUID = 1L;

   public MethodNotFoundException() {
   }

   public MethodNotFoundException(String message) {
      super(message);
   }

   public MethodNotFoundException(Throwable cause) {
      super(cause);
   }

   public MethodNotFoundException(String message, Throwable cause) {
      super(message, cause);
   }
}
