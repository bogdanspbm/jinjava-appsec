package jinjava.de.odysseus.el.misc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jinjava.javax.el.ELException;

public class BooleanOperations {
   private static final Set<Class<? extends Number>> SIMPLE_INTEGER_TYPES = new HashSet();
   private static final Set<Class<? extends Number>> SIMPLE_FLOAT_TYPES = new HashSet();

   private static final boolean lt0(TypeConverter converter, Object o1, Object o2) {
      Class<?> t1 = o1.getClass();
      Class<?> t2 = o2.getClass();
      if (!BigDecimal.class.isAssignableFrom(t1) && !BigDecimal.class.isAssignableFrom(t2)) {
         if (!SIMPLE_FLOAT_TYPES.contains(t1) && !SIMPLE_FLOAT_TYPES.contains(t2)) {
            if (!BigInteger.class.isAssignableFrom(t1) && !BigInteger.class.isAssignableFrom(t2)) {
               if (!SIMPLE_INTEGER_TYPES.contains(t1) && !SIMPLE_INTEGER_TYPES.contains(t2)) {
                  if (t1 != String.class && t2 != String.class) {
                     if (o1 instanceof Comparable) {
                        return ((Comparable)o1).compareTo(o2) < 0;
                     } else if (o2 instanceof Comparable) {
                        return ((Comparable)o2).compareTo(o1) > 0;
                     } else {
                        throw new ELException(LocalMessages.get("error.compare.types", o1.getClass(), o2.getClass()));
                     }
                  } else {
                     return ((String)converter.convert(o1, String.class)).compareTo((String)converter.convert(o2, String.class)) < 0;
                  }
               } else {
                  return (Long)converter.convert(o1, Long.class) < (Long)converter.convert(o2, Long.class);
               }
            } else {
               return ((BigInteger)converter.convert(o1, BigInteger.class)).compareTo((BigInteger)converter.convert(o2, BigInteger.class)) < 0;
            }
         } else {
            return (Double)converter.convert(o1, Double.class) < (Double)converter.convert(o2, Double.class);
         }
      } else {
         return ((BigDecimal)converter.convert(o1, BigDecimal.class)).compareTo((BigDecimal)converter.convert(o2, BigDecimal.class)) < 0;
      }
   }

   private static final boolean gt0(TypeConverter converter, Object o1, Object o2) {
      Class<?> t1 = o1.getClass();
      Class<?> t2 = o2.getClass();
      if (!BigDecimal.class.isAssignableFrom(t1) && !BigDecimal.class.isAssignableFrom(t2)) {
         if (!SIMPLE_FLOAT_TYPES.contains(t1) && !SIMPLE_FLOAT_TYPES.contains(t2)) {
            if (!BigInteger.class.isAssignableFrom(t1) && !BigInteger.class.isAssignableFrom(t2)) {
               if (!SIMPLE_INTEGER_TYPES.contains(t1) && !SIMPLE_INTEGER_TYPES.contains(t2)) {
                  if (t1 != String.class && t2 != String.class) {
                     if (o1 instanceof Comparable) {
                        return ((Comparable)o1).compareTo(o2) > 0;
                     } else if (o2 instanceof Comparable) {
                        return ((Comparable)o2).compareTo(o1) < 0;
                     } else {
                        throw new ELException(LocalMessages.get("error.compare.types", o1.getClass(), o2.getClass()));
                     }
                  } else {
                     return ((String)converter.convert(o1, String.class)).compareTo((String)converter.convert(o2, String.class)) > 0;
                  }
               } else {
                  return (Long)converter.convert(o1, Long.class) > (Long)converter.convert(o2, Long.class);
               }
            } else {
               return ((BigInteger)converter.convert(o1, BigInteger.class)).compareTo((BigInteger)converter.convert(o2, BigInteger.class)) > 0;
            }
         } else {
            return (Double)converter.convert(o1, Double.class) > (Double)converter.convert(o2, Double.class);
         }
      } else {
         return ((BigDecimal)converter.convert(o1, BigDecimal.class)).compareTo((BigDecimal)converter.convert(o2, BigDecimal.class)) > 0;
      }
   }

   public static final boolean lt(TypeConverter converter, Object o1, Object o2) {
      if (o1 == o2) {
         return false;
      } else {
         return o1 != null && o2 != null ? lt0(converter, o1, o2) : false;
      }
   }

   public static final boolean gt(TypeConverter converter, Object o1, Object o2) {
      if (o1 == o2) {
         return false;
      } else {
         return o1 != null && o2 != null ? gt0(converter, o1, o2) : false;
      }
   }

   public static final boolean ge(TypeConverter converter, Object o1, Object o2) {
      if (o1 == o2) {
         return true;
      } else if (o1 != null && o2 != null) {
         return !lt0(converter, o1, o2);
      } else {
         return false;
      }
   }

   public static final boolean le(TypeConverter converter, Object o1, Object o2) {
      if (o1 == o2) {
         return true;
      } else if (o1 != null && o2 != null) {
         return !gt0(converter, o1, o2);
      } else {
         return false;
      }
   }

   public static final boolean eq(TypeConverter converter, Object o1, Object o2) {
      if (o1 == o2) {
         return true;
      } else if (o1 != null && o2 != null) {
         Class<?> t1 = o1.getClass();
         Class<?> t2 = o2.getClass();
         if (!BigDecimal.class.isAssignableFrom(t1) && !BigDecimal.class.isAssignableFrom(t2)) {
            if (!SIMPLE_FLOAT_TYPES.contains(t1) && !SIMPLE_FLOAT_TYPES.contains(t2)) {
               if (!BigInteger.class.isAssignableFrom(t1) && !BigInteger.class.isAssignableFrom(t2)) {
                  if (!SIMPLE_INTEGER_TYPES.contains(t1) && !SIMPLE_INTEGER_TYPES.contains(t2)) {
                     if (t1 != Boolean.class && t2 != Boolean.class) {
                        if (o1 instanceof Enum) {
                           return o1 == converter.convert(o2, o1.getClass());
                        } else if (o2 instanceof Enum) {
                           return converter.convert(o1, o2.getClass()) == o2;
                        } else {
                           return t1 != String.class && t2 != String.class ? o1.equals(o2) : ((String)converter.convert(o1, String.class)).equals(converter.convert(o2, String.class));
                        }
                     } else {
                        return ((Boolean)converter.convert(o1, Boolean.class)).equals(converter.convert(o2, Boolean.class));
                     }
                  } else {
                     return ((Long)converter.convert(o1, Long.class)).equals(converter.convert(o2, Long.class));
                  }
               } else {
                  return ((BigInteger)converter.convert(o1, BigInteger.class)).equals(converter.convert(o2, BigInteger.class));
               }
            } else {
               return ((Double)converter.convert(o1, Double.class)).equals(converter.convert(o2, Double.class));
            }
         } else {
            return ((BigDecimal)converter.convert(o1, BigDecimal.class)).equals(converter.convert(o2, BigDecimal.class));
         }
      } else {
         return false;
      }
   }

   public static final boolean ne(TypeConverter converter, Object o1, Object o2) {
      return !eq(converter, o1, o2);
   }

   public static final boolean empty(TypeConverter converter, Object o) {
      if (o != null && !"".equals(o)) {
         if (o instanceof Object[]) {
            return ((Object[])((Object[])o)).length == 0;
         } else if (o instanceof Map) {
            return ((Map)o).isEmpty();
         } else {
            return o instanceof Collection ? ((Collection)o).isEmpty() : false;
         }
      } else {
         return true;
      }
   }

   static {
      SIMPLE_INTEGER_TYPES.add(Byte.class);
      SIMPLE_INTEGER_TYPES.add(Short.class);
      SIMPLE_INTEGER_TYPES.add(Integer.class);
      SIMPLE_INTEGER_TYPES.add(Long.class);
      SIMPLE_FLOAT_TYPES.add(Float.class);
      SIMPLE_FLOAT_TYPES.add(Double.class);
   }
}
