package jinjava.javax.el;

import java.util.EventObject;

public class ELContextEvent extends EventObject {
   private static final long serialVersionUID = 1L;

   public ELContextEvent(ELContext source) {
      super(source);
   }

   public ELContext getELContext() {
      return (ELContext)this.getSource();
   }
}
