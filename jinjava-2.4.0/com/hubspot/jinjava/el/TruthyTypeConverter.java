package com.hubspot.jinjava.el;

import com.hubspot.jinjava.util.ObjectTruthValue;
import jinjava.de.odysseus.el.misc.TypeConverterImpl;

public class TruthyTypeConverter extends TypeConverterImpl {
   private static final long serialVersionUID = 1L;

   protected Boolean coerceToBoolean(Object value) {
      return ObjectTruthValue.evaluate(value);
   }
}
