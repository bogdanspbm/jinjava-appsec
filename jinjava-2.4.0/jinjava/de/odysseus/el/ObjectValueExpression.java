package jinjava.de.odysseus.el;

import jinjava.de.odysseus.el.misc.LocalMessages;
import jinjava.de.odysseus.el.misc.TypeConverter;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELException;
import jinjava.javax.el.ValueExpression;

public final class ObjectValueExpression extends ValueExpression {
   private static final long serialVersionUID = 1L;
   private final TypeConverter converter;
   private final Object object;
   private final Class<?> type;

   public ObjectValueExpression(TypeConverter converter, Object object, Class<?> type) {
      this.converter = converter;
      this.object = object;
      this.type = type;
      if (type == null) {
         throw new NullPointerException(LocalMessages.get("error.value.notype"));
      }
   }

   public boolean equals(Object obj) {
      if (obj != null && obj.getClass() == this.getClass()) {
         ObjectValueExpression other = (ObjectValueExpression)obj;
         if (this.type != other.type) {
            return false;
         } else {
            return this.object == other.object || this.object != null && this.object.equals(other.object);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.object == null ? 0 : this.object.hashCode();
   }

   public Object getValue(ELContext context) {
      return this.converter.convert(this.object, this.type);
   }

   public String getExpressionString() {
      return null;
   }

   public boolean isLiteralText() {
      return false;
   }

   public Class<?> getType(ELContext context) {
      return null;
   }

   public boolean isReadOnly(ELContext context) {
      return true;
   }

   public void setValue(ELContext context, Object value) {
      throw new ELException(LocalMessages.get("error.value.set.rvalue", "<object value expression>"));
   }

   public String toString() {
      return "ValueExpression(" + this.object + ")";
   }

   public Class<?> getExpectedType() {
      return this.type;
   }
}
