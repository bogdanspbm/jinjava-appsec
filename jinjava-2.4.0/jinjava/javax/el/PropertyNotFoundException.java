package jinjava.javax.el;

public class PropertyNotFoundException extends ELException {
   private static final long serialVersionUID = 1L;

   public PropertyNotFoundException() {
   }

   public PropertyNotFoundException(String message) {
      super(message);
   }

   public PropertyNotFoundException(Throwable cause) {
      super(cause);
   }

   public PropertyNotFoundException(String message, Throwable cause) {
      super(message, cause);
   }
}
