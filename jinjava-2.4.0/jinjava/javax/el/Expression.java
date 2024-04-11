package jinjava.javax.el;

import java.io.Serializable;

public abstract class Expression implements Serializable {
   private static final long serialVersionUID = 1L;

   public abstract boolean equals(Object var1);

   public abstract String getExpressionString();

   public abstract int hashCode();

   public abstract boolean isLiteralText();
}
