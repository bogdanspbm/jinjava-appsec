package jinjava.de.odysseus.el.misc;

import java.math.BigDecimal;
import java.math.BigInteger;
import jinjava.javax.el.ELException;

public class NumberOperations {
   private static final Long LONG_ZERO = 0L;

   private static final boolean isDotEe(String value) {
      int length = value.length();
      int i = 0;

      while(i < length) {
         switch(value.charAt(i)) {
         case '.':
         case 'E':
         case 'e':
            return true;
         default:
            ++i;
         }
      }

      return false;
   }

   private static final boolean isDotEe(Object value) {
      return value instanceof String && isDotEe((String)value);
   }

   private static final boolean isFloatOrDouble(Object value) {
      return value instanceof Float || value instanceof Double;
   }

   private static final boolean isFloatOrDoubleOrDotEe(Object value) {
      return isFloatOrDouble(value) || isDotEe(value);
   }

   private static final boolean isBigDecimalOrBigInteger(Object value) {
      return value instanceof BigDecimal || value instanceof BigInteger;
   }

   private static final boolean isBigDecimalOrFloatOrDoubleOrDotEe(Object value) {
      return value instanceof BigDecimal || isFloatOrDoubleOrDotEe(value);
   }

   public static final Number add(TypeConverter converter, Object o1, Object o2) {
      if (o1 == null && o2 == null) {
         return LONG_ZERO;
      } else if (!(o1 instanceof BigDecimal) && !(o2 instanceof BigDecimal)) {
         if (!isFloatOrDoubleOrDotEe(o1) && !isFloatOrDoubleOrDotEe(o2)) {
            return (Number)(!(o1 instanceof BigInteger) && !(o2 instanceof BigInteger) ? (Long)converter.convert(o1, Long.class) + (Long)converter.convert(o2, Long.class) : ((BigInteger)converter.convert(o1, BigInteger.class)).add((BigInteger)converter.convert(o2, BigInteger.class)));
         } else {
            return (Number)(!(o1 instanceof BigInteger) && !(o2 instanceof BigInteger) ? (Double)converter.convert(o1, Double.class) + (Double)converter.convert(o2, Double.class) : ((BigDecimal)converter.convert(o1, BigDecimal.class)).add((BigDecimal)converter.convert(o2, BigDecimal.class)));
         }
      } else {
         return ((BigDecimal)converter.convert(o1, BigDecimal.class)).add((BigDecimal)converter.convert(o2, BigDecimal.class));
      }
   }

   public static final Number sub(TypeConverter converter, Object o1, Object o2) {
      if (o1 == null && o2 == null) {
         return LONG_ZERO;
      } else if (!(o1 instanceof BigDecimal) && !(o2 instanceof BigDecimal)) {
         if (!isFloatOrDoubleOrDotEe(o1) && !isFloatOrDoubleOrDotEe(o2)) {
            return (Number)(!(o1 instanceof BigInteger) && !(o2 instanceof BigInteger) ? (Long)converter.convert(o1, Long.class) - (Long)converter.convert(o2, Long.class) : ((BigInteger)converter.convert(o1, BigInteger.class)).subtract((BigInteger)converter.convert(o2, BigInteger.class)));
         } else {
            return (Number)(!(o1 instanceof BigInteger) && !(o2 instanceof BigInteger) ? (Double)converter.convert(o1, Double.class) - (Double)converter.convert(o2, Double.class) : ((BigDecimal)converter.convert(o1, BigDecimal.class)).subtract((BigDecimal)converter.convert(o2, BigDecimal.class)));
         }
      } else {
         return ((BigDecimal)converter.convert(o1, BigDecimal.class)).subtract((BigDecimal)converter.convert(o2, BigDecimal.class));
      }
   }

   public static final Number mul(TypeConverter converter, Object o1, Object o2) {
      if (o1 == null && o2 == null) {
         return LONG_ZERO;
      } else if (!(o1 instanceof BigDecimal) && !(o2 instanceof BigDecimal)) {
         if (!isFloatOrDoubleOrDotEe(o1) && !isFloatOrDoubleOrDotEe(o2)) {
            return (Number)(!(o1 instanceof BigInteger) && !(o2 instanceof BigInteger) ? (Long)converter.convert(o1, Long.class) * (Long)converter.convert(o2, Long.class) : ((BigInteger)converter.convert(o1, BigInteger.class)).multiply((BigInteger)converter.convert(o2, BigInteger.class)));
         } else {
            return (Number)(!(o1 instanceof BigInteger) && !(o2 instanceof BigInteger) ? (Double)converter.convert(o1, Double.class) * (Double)converter.convert(o2, Double.class) : ((BigDecimal)converter.convert(o1, BigDecimal.class)).multiply((BigDecimal)converter.convert(o2, BigDecimal.class)));
         }
      } else {
         return ((BigDecimal)converter.convert(o1, BigDecimal.class)).multiply((BigDecimal)converter.convert(o2, BigDecimal.class));
      }
   }

   public static final Number div(TypeConverter converter, Object o1, Object o2) {
      if (o1 == null && o2 == null) {
         return LONG_ZERO;
      } else {
         return (Number)(!isBigDecimalOrBigInteger(o1) && !isBigDecimalOrBigInteger(o2) ? (Double)converter.convert(o1, Double.class) / (Double)converter.convert(o2, Double.class) : ((BigDecimal)converter.convert(o1, BigDecimal.class)).divide((BigDecimal)converter.convert(o2, BigDecimal.class), 4));
      }
   }

   public static final Number mod(TypeConverter converter, Object o1, Object o2) {
      if (o1 == null && o2 == null) {
         return LONG_ZERO;
      } else if (!isBigDecimalOrFloatOrDoubleOrDotEe(o1) && !isBigDecimalOrFloatOrDoubleOrDotEe(o2)) {
         return (Number)(!(o1 instanceof BigInteger) && !(o2 instanceof BigInteger) ? (Long)converter.convert(o1, Long.class) % (Long)converter.convert(o2, Long.class) : ((BigInteger)converter.convert(o1, BigInteger.class)).remainder((BigInteger)converter.convert(o2, BigInteger.class)));
      } else {
         return (Double)converter.convert(o1, Double.class) % (Double)converter.convert(o2, Double.class);
      }
   }

   public static final Number neg(TypeConverter converter, Object value) {
      if (value == null) {
         return LONG_ZERO;
      } else if (value instanceof BigDecimal) {
         return ((BigDecimal)value).negate();
      } else if (value instanceof BigInteger) {
         return ((BigInteger)value).negate();
      } else if (value instanceof Double) {
         return -(Double)value;
      } else if (value instanceof Float) {
         return -(Float)value;
      } else if (value instanceof String) {
         return (Number)(isDotEe((String)value) ? -(Double)converter.convert(value, Double.class) : -(Long)converter.convert(value, Long.class));
      } else if (value instanceof Long) {
         return -(Long)value;
      } else if (value instanceof Integer) {
         return -(Integer)value;
      } else if (value instanceof Short) {
         return (short)(-(Short)value);
      } else if (value instanceof Byte) {
         return (byte)(-(Byte)value);
      } else {
         throw new ELException(LocalMessages.get("error.negate", value.getClass()));
      }
   }
}
