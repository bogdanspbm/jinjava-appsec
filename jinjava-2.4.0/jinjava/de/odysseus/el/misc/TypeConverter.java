package jinjava.de.odysseus.el.misc;

import java.io.Serializable;
import jinjava.javax.el.ELException;

public interface TypeConverter extends Serializable {
   TypeConverter DEFAULT = new TypeConverterImpl();

   <T> T convert(Object var1, Class<T> var2) throws ELException;
}
