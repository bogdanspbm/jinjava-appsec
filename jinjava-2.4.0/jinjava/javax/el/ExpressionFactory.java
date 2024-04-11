package jinjava.javax.el;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.Properties;

public abstract class ExpressionFactory {
   public static ExpressionFactory newInstance() {
      return newInstance((Properties)null);
   }

   public static ExpressionFactory newInstance(Properties properties) {
      ClassLoader classLoader;
      try {
         classLoader = Thread.currentThread().getContextClassLoader();
      } catch (SecurityException var190) {
         classLoader = ExpressionFactory.class.getClassLoader();
      }

      String className = null;
      String serviceId = "META-INF/services/" + ExpressionFactory.class.getName();
      Object input = classLoader.getResourceAsStream(serviceId);

      try {
         if (input != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)input, "UTF-8"));
            className = reader.readLine();
            reader.close();
         }
      } catch (IOException var189) {
      } finally {
         if (input != null) {
            try {
               ((InputStream)input).close();
            } catch (Exception var184) {
            } finally {
               input = null;
            }
         }

      }

      if (className == null || className.trim().length() == 0) {
         try {
            String home = System.getProperty("java.home");
            if (home != null) {
               String path = home + File.separator + "lib" + File.separator + "el.properties";
               File file = new File(path);
               if (file.exists()) {
                  input = new FileInputStream(file);
                  Properties props = new Properties();
                  props.load((InputStream)input);
                  className = props.getProperty(ExpressionFactory.class.getName());
               }
            }
         } catch (IOException var187) {
         } catch (SecurityException var188) {
         } finally {
            if (input != null) {
               try {
                  ((InputStream)input).close();
               } catch (IOException var182) {
               } finally {
                  input = null;
               }
            }

         }
      }

      if (className == null || className.trim().length() == 0) {
         try {
            className = System.getProperty(ExpressionFactory.class.getName());
         } catch (Exception var186) {
         }
      }

      if (className == null || className.trim().length() == 0) {
         className = "jinjava.de.odysseus.el.ExpressionFactoryImpl";
      }

      return newInstance(properties, className, classLoader);
   }

   private static ExpressionFactory newInstance(Properties properties, String className, ClassLoader classLoader) {
      Class clazz = null;

      try {
         clazz = classLoader.loadClass(className.trim());
         if (!ExpressionFactory.class.isAssignableFrom(clazz)) {
            throw new ELException("Invalid expression factory class: " + clazz.getName());
         }
      } catch (ClassNotFoundException var8) {
         throw new ELException("Could not find expression factory class", var8);
      }

      try {
         if (properties != null) {
            Constructor constructor = null;

            try {
               constructor = clazz.getConstructor(Properties.class);
            } catch (Exception var6) {
            }

            if (constructor != null) {
               return (ExpressionFactory)constructor.newInstance(properties);
            }
         }

         return (ExpressionFactory)clazz.newInstance();
      } catch (Exception var7) {
         throw new ELException("Could not create expression factory instance", var7);
      }
   }

   public abstract Object coerceToType(Object var1, Class<?> var2);

   public abstract MethodExpression createMethodExpression(ELContext var1, String var2, Class<?> var3, Class<?>[] var4);

   public abstract ValueExpression createValueExpression(ELContext var1, String var2, Class<?> var3);

   public abstract ValueExpression createValueExpression(Object var1, Class<?> var2);
}
