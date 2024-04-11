package jinjava.de.odysseus.el.misc;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import jinjava.javax.el.ELException;

public class TypeConverterImpl implements TypeConverter {
   private static final long serialVersionUID = 1L;

   protected Boolean coerceToBoolean(Object value) {
      if (value != null && !"".equals(value)) {
         if (value instanceof Boolean) {
            return (Boolean)value;
         } else if (value instanceof String) {
            return Boolean.valueOf((String)value);
         } else {
            throw new ELException(LocalMessages.get("error.coerce.type", value, value.getClass(), Boolean.class));
         }
      } else {
         return Boolean.FALSE;
      }
   }

   protected Character coerceToCharacter(Object value) {
      if (value != null && !"".equals(value)) {
         if (value instanceof Character) {
            return (Character)value;
         } else if (value instanceof Number) {
            return (char)((Number)value).shortValue();
         } else if (value instanceof String) {
            return ((String)value).charAt(0);
         } else {
            throw new ELException(LocalMessages.get("error.coerce.type", value, value.getClass(), Character.class));
         }
      } else {
         return '\u0000';
      }
   }

   protected BigDecimal coerceToBigDecimal(Object value) {
      if (value != null && !"".equals(value)) {
         if (value instanceof BigDecimal) {
            return (BigDecimal)value;
         } else if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger)value);
         } else if (value instanceof Number) {
            return new BigDecimal(((Number)value).doubleValue());
         } else if (value instanceof String) {
            try {
               return new BigDecimal((String)value);
            } catch (NumberFormatException var3) {
               throw new ELException(LocalMessages.get("error.coerce.value", value, value.getClass(), BigDecimal.class));
            }
         } else if (value instanceof Character) {
            return new BigDecimal((short)(Character)value);
         } else {
            throw new ELException(LocalMessages.get("error.coerce.type", value, value.getClass(), BigDecimal.class));
         }
      } else {
         return BigDecimal.valueOf(0L);
      }
   }

   protected BigInteger coerceToBigInteger(Object value) {
      if (value != null && !"".equals(value)) {
         if (value instanceof BigInteger) {
            return (BigInteger)value;
         } else if (value instanceof BigDecimal) {
            return ((BigDecimal)value).toBigInteger();
         } else if (value instanceof Number) {
            return BigInteger.valueOf(((Number)value).longValue());
         } else if (value instanceof String) {
            try {
               return new BigInteger((String)value);
            } catch (NumberFormatException var3) {
               throw new ELException(LocalMessages.get("error.coerce.value", value, value.getClass(), BigInteger.class));
            }
         } else if (value instanceof Character) {
            return BigInteger.valueOf((long)((short)(Character)value));
         } else {
            throw new ELException(LocalMessages.get("error.coerce.type", value, value.getClass(), BigInteger.class));
         }
      } else {
         return BigInteger.valueOf(0L);
      }
   }

   protected Double coerceToDouble(Object value) {
      if (value != null && !"".equals(value)) {
         if (value instanceof Double) {
            return (Double)value;
         } else if (value instanceof Number) {
            return ((Number)value).doubleValue();
         } else if (value instanceof String) {
            try {
               return Double.valueOf((String)value);
            } catch (NumberFormatException var3) {
               throw new ELException(LocalMessages.get("error.coerce.value", value, value.getClass(), Double.class));
            }
         } else if (value instanceof Character) {
            return (double)((short)(Character)value);
         } else {
            throw new ELException(LocalMessages.get("error.coerce.type", value, value.getClass(), Double.class));
         }
      } else {
         return 0.0D;
      }
   }

   protected Float coerceToFloat(Object value) {
      if (value != null && !"".equals(value)) {
         if (value instanceof Float) {
            return (Float)value;
         } else if (value instanceof Number) {
            return ((Number)value).floatValue();
         } else if (value instanceof String) {
            try {
               return Float.valueOf((String)value);
            } catch (NumberFormatException var3) {
               throw new ELException(LocalMessages.get("error.coerce.value", value, value.getClass(), Float.class));
            }
         } else if (value instanceof Character) {
            return (float)((short)(Character)value);
         } else {
            throw new ELException(LocalMessages.get("error.coerce.type", value, value.getClass(), Float.class));
         }
      } else {
         return 0.0F;
      }
   }

   protected Long coerceToLong(Object value) {
      if (value != null && !"".equals(value)) {
         if (value instanceof Long) {
            return (Long)value;
         } else if (value instanceof Number) {
            return ((Number)value).longValue();
         } else if (value instanceof String) {
            try {
               return Long.valueOf((String)value);
            } catch (NumberFormatException var3) {
               throw new ELException(LocalMessages.get("error.coerce.value", value, value.getClass(), Long.class));
            }
         } else if (value instanceof Character) {
            return (long)((short)(Character)value);
         } else {
            throw new ELException(LocalMessages.get("error.coerce.type", value, value.getClass(), Long.class));
         }
      } else {
         return 0L;
      }
   }

   protected Integer coerceToInteger(Object value) {
      if (value != null && !"".equals(value)) {
         if (value instanceof Integer) {
            return (Integer)value;
         } else if (value instanceof Number) {
            return ((Number)value).intValue();
         } else if (value instanceof String) {
            try {
               return Integer.valueOf((String)value);
            } catch (NumberFormatException var3) {
               throw new ELException(LocalMessages.get("error.coerce.value", value, value.getClass(), Integer.class));
            }
         } else if (value instanceof Character) {
            return Integer.valueOf((short)(Character)value);
         } else {
            throw new ELException(LocalMessages.get("error.coerce.type", value, value.getClass(), Integer.class));
         }
      } else {
         return 0;
      }
   }

   protected Short coerceToShort(Object value) {
      if (value != null && !"".equals(value)) {
         if (value instanceof Short) {
            return (Short)value;
         } else if (value instanceof Number) {
            return ((Number)value).shortValue();
         } else if (value instanceof String) {
            try {
               return Short.valueOf((String)value);
            } catch (NumberFormatException var3) {
               throw new ELException(LocalMessages.get("error.coerce.value", value, value.getClass(), Short.class));
            }
         } else if (value instanceof Character) {
            return (short)(Character)value;
         } else {
            throw new ELException(LocalMessages.get("error.coerce.type", value, value.getClass(), Short.class));
         }
      } else {
         return Short.valueOf((short)0);
      }
   }

   protected Byte coerceToByte(Object value) {
      if (value != null && !"".equals(value)) {
         if (value instanceof Byte) {
            return (Byte)value;
         } else if (value instanceof Number) {
            return ((Number)value).byteValue();
         } else if (value instanceof String) {
            try {
               return Byte.valueOf((String)value);
            } catch (NumberFormatException var3) {
               throw new ELException(LocalMessages.get("error.coerce.value", value, value.getClass(), Byte.class));
            }
         } else if (value instanceof Character) {
            return Short.valueOf((short)(Character)value).byteValue();
         } else {
            throw new ELException(LocalMessages.get("error.coerce.type", value, value.getClass(), Byte.class));
         }
      } else {
         return 0;
      }
   }

   protected String coerceToString(Object value) {
      if (value == null) {
         return "";
      } else if (value instanceof String) {
         return (String)value;
      } else {
         return value instanceof Enum ? ((Enum)value).name() : value.toString();
      }
   }

   protected <T extends Enum<T>> T coerceToEnum(Object value, Class<T> type) {
      if (value != null && !"".equals(value)) {
         if (type.isInstance(value)) {
            return (Enum)value;
         } else if (value instanceof String) {
            try {
               return Enum.valueOf(type, (String)value);
            } catch (IllegalArgumentException var4) {
               throw new ELException(LocalMessages.get("error.coerce.value", value, value.getClass(), type));
            }
         } else {
            throw new ELException(LocalMessages.get("error.coerce.type", value, value.getClass(), type));
         }
      } else {
         return null;
      }
   }

   protected Object coerceStringToType(String value, Class<?> type) {
      PropertyEditor editor = PropertyEditorManager.findEditor(type);
      if (editor == null) {
         if ("".equals(value)) {
            return null;
         } else {
            throw new ELException(LocalMessages.get("error.coerce.type", value, String.class, type));
         }
      } else {
         if ("".equals(value)) {
            try {
               editor.setAsText(value);
            } catch (IllegalArgumentException var6) {
               return null;
            }
         } else {
            try {
               editor.setAsText(value);
            } catch (IllegalArgumentException var5) {
               throw new ELException(LocalMessages.get("error.coerce.value", value, value.getClass(), type));
            }
         }

         return editor.getValue();
      }
   }

   protected Object coerceToType(Object value, Class<?> type) {
      if (type == String.class) {
         return this.coerceToString(value);
      } else if (type != Long.class && type != Long.TYPE) {
         if (type != Double.class && type != Double.TYPE) {
            if (type != Boolean.class && type != Boolean.TYPE) {
               if (type != Integer.class && type != Integer.TYPE) {
                  if (type != Float.class && type != Float.TYPE) {
                     if (type != Short.class && type != Short.TYPE) {
                        if (type != Byte.class && type != Byte.TYPE) {
                           if (type != Character.class && type != Character.TYPE) {
                              if (type == BigDecimal.class) {
                                 return this.coerceToBigDecimal(value);
                              } else if (type == BigInteger.class) {
                                 return this.coerceToBigInteger(value);
                              } else if (type.getSuperclass() == Enum.class) {
                                 return this.coerceToEnum(value, type);
                              } else if (value != null && value.getClass() != type && !type.isInstance(value)) {
                                 if (value instanceof String) {
                                    return this.coerceStringToType((String)value, type);
                                 } else {
                                    throw new ELException(LocalMessages.get("error.coerce.type", value, value.getClass(), type));
                                 }
                              } else {
                                 return value;
                              }
                           } else {
                              return this.coerceToCharacter(value);
                           }
                        } else {
                           return this.coerceToByte(value);
                        }
                     } else {
                        return this.coerceToShort(value);
                     }
                  } else {
                     return this.coerceToFloat(value);
                  }
               } else {
                  return this.coerceToInteger(value);
               }
            } else {
               return this.coerceToBoolean(value);
            }
         } else {
            return this.coerceToDouble(value);
         }
      } else {
         return this.coerceToLong(value);
      }
   }

   public boolean equals(Object obj) {
      return obj != null && obj.getClass().equals(this.getClass());
   }

   public int hashCode() {
      return this.getClass().hashCode();
   }

   public <T> T convert(Object value, Class<T> type) throws ELException {
      return this.coerceToType(value, type);
   }
}
