package jinjava.de.odysseus.el.tree;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import jinjava.de.odysseus.el.misc.TypeConverter;
import jinjava.javax.el.ValueExpression;

public class Bindings implements TypeConverter {
   private static final long serialVersionUID = 1L;
   private static final Method[] NO_FUNCTIONS = new Method[0];
   private static final ValueExpression[] NO_VARIABLES = new ValueExpression[0];
   private transient Method[] functions;
   private final ValueExpression[] variables;
   private final TypeConverter converter;

   public Bindings(Method[] functions, ValueExpression[] variables) {
      this(functions, variables, TypeConverter.DEFAULT);
   }

   public Bindings(Method[] functions, ValueExpression[] variables, TypeConverter converter) {
      this.functions = functions != null && functions.length != 0 ? functions : NO_FUNCTIONS;
      this.variables = variables != null && variables.length != 0 ? variables : NO_VARIABLES;
      this.converter = converter == null ? TypeConverter.DEFAULT : converter;
   }

   public Method getFunction(int index) {
      return this.functions[index];
   }

   public boolean isFunctionBound(int index) {
      return index >= 0 && index < this.functions.length;
   }

   public ValueExpression getVariable(int index) {
      return this.variables[index];
   }

   public boolean isVariableBound(int index) {
      return index >= 0 && index < this.variables.length && this.variables[index] != null;
   }

   public <T> T convert(Object value, Class<T> type) {
      return this.converter.convert(value, type);
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof Bindings)) {
         return false;
      } else {
         Bindings other = (Bindings)obj;
         return Arrays.equals(this.functions, other.functions) && Arrays.equals(this.variables, other.variables) && this.converter.equals(other.converter);
      }
   }

   public int hashCode() {
      return Arrays.hashCode(this.functions) ^ Arrays.hashCode(this.variables) ^ this.converter.hashCode();
   }

   private void writeObject(ObjectOutputStream out) throws IOException, ClassNotFoundException {
      out.defaultWriteObject();
      Bindings.MethodWrapper[] wrappers = new Bindings.MethodWrapper[this.functions.length];

      for(int i = 0; i < wrappers.length; ++i) {
         wrappers[i] = new Bindings.MethodWrapper(this.functions[i]);
      }

      out.writeObject(wrappers);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      Bindings.MethodWrapper[] wrappers = (Bindings.MethodWrapper[])((Bindings.MethodWrapper[])in.readObject());
      if (wrappers.length == 0) {
         this.functions = NO_FUNCTIONS;
      } else {
         this.functions = new Method[wrappers.length];

         for(int i = 0; i < this.functions.length; ++i) {
            this.functions[i] = wrappers[i].method;
         }
      }

   }

   private static class MethodWrapper implements Serializable {
      private static final long serialVersionUID = 1L;
      private transient Method method;

      private MethodWrapper(Method method) {
         this.method = method;
      }

      private void writeObject(ObjectOutputStream out) throws IOException, ClassNotFoundException {
         out.defaultWriteObject();
         out.writeObject(this.method.getDeclaringClass());
         out.writeObject(this.method.getName());
         out.writeObject(this.method.getParameterTypes());
      }

      private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
         in.defaultReadObject();
         Class<?> type = (Class)in.readObject();
         String name = (String)in.readObject();
         Class[] args = (Class[])((Class[])in.readObject());

         try {
            this.method = type.getDeclaredMethod(name, args);
         } catch (NoSuchMethodException var6) {
            throw new IOException(var6.getMessage());
         }
      }

      // $FF: synthetic method
      MethodWrapper(Method x0, Object x1) {
         this(x0);
      }
   }
}
