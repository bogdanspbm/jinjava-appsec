package jinjava.de.odysseus.el;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.EnumSet;
import java.util.Properties;
import jinjava.de.odysseus.el.misc.TypeConverter;
import jinjava.de.odysseus.el.tree.TreeBuilder;
import jinjava.de.odysseus.el.tree.TreeStore;
import jinjava.de.odysseus.el.tree.impl.Builder;
import jinjava.de.odysseus.el.tree.impl.Cache;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELException;
import jinjava.javax.el.ExpressionFactory;

public class ExpressionFactoryImpl extends ExpressionFactory {
   public static final String PROP_METHOD_INVOCATIONS = "jinjava.javax.el.methodInvocations";
   public static final String PROP_VAR_ARGS = "jinjava.javax.el.varArgs";
   public static final String PROP_NULL_PROPERTIES = "jinjava.javax.el.nullProperties";
   public static final String PROP_IGNORE_RETURN_TYPE = "jinjava.javax.el.ignoreReturnType";
   public static final String PROP_CACHE_SIZE = "jinjava.javax.el.cacheSize";
   private final TreeStore store;
   private final TypeConverter converter;

   public ExpressionFactoryImpl() {
      this(ExpressionFactoryImpl.Profile.JEE6);
   }

   public ExpressionFactoryImpl(ExpressionFactoryImpl.Profile profile) {
      Properties properties = this.loadProperties("el.properties");
      this.store = this.createTreeStore(1000, profile, properties);
      this.converter = this.createTypeConverter(properties);
   }

   public ExpressionFactoryImpl(Properties properties) {
      this(ExpressionFactoryImpl.Profile.JEE6, properties);
   }

   public ExpressionFactoryImpl(ExpressionFactoryImpl.Profile profile, Properties properties) {
      this.store = this.createTreeStore(1000, profile, properties);
      this.converter = this.createTypeConverter(properties);
   }

   public ExpressionFactoryImpl(Properties properties, TypeConverter converter) {
      this(ExpressionFactoryImpl.Profile.JEE6, properties, converter);
   }

   public ExpressionFactoryImpl(ExpressionFactoryImpl.Profile profile, Properties properties, TypeConverter converter) {
      this.store = this.createTreeStore(1000, profile, properties);
      this.converter = converter;
   }

   public ExpressionFactoryImpl(TreeStore store) {
      this(store, TypeConverter.DEFAULT);
   }

   public ExpressionFactoryImpl(TreeStore store, TypeConverter converter) {
      this.store = store;
      this.converter = converter;
   }

   private Properties loadDefaultProperties() {
      String home = System.getProperty("java.home");
      String path = home + File.separator + "lib" + File.separator + "el.properties";
      File file = new File(path);

      try {
         if (file.exists()) {
            Properties properties = new Properties();
            FileInputStream input = null;

            try {
               properties.load(input = new FileInputStream(file));
            } catch (IOException var15) {
               throw new ELException("Cannot read default EL properties", var15);
            } finally {
               try {
                  input.close();
               } catch (IOException var14) {
               }

            }

            if (this.getClass().getName().equals(properties.getProperty("jinjava.javax.el.ExpressionFactory"))) {
               return properties;
            }
         }
      } catch (SecurityException var17) {
      }

      return this.getClass().getName().equals(System.getProperty("jinjava.javax.el.ExpressionFactory")) ? System.getProperties() : null;
   }

   private Properties loadProperties(String path) {
      Properties properties = new Properties(this.loadDefaultProperties());
      InputStream input = null;

      try {
         input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
      } catch (SecurityException var15) {
         input = ClassLoader.getSystemResourceAsStream(path);
      }

      if (input != null) {
         try {
            properties.load(input);
         } catch (IOException var13) {
            throw new ELException("Cannot read EL properties", var13);
         } finally {
            try {
               input.close();
            } catch (IOException var12) {
            }

         }
      }

      return properties;
   }

   private boolean getFeatureProperty(ExpressionFactoryImpl.Profile profile, Properties properties, Builder.Feature feature, String property) {
      return Boolean.valueOf(properties.getProperty(property, String.valueOf(profile.contains(feature))));
   }

   protected TreeStore createTreeStore(int defaultCacheSize, ExpressionFactoryImpl.Profile profile, Properties properties) {
      TreeBuilder builder = null;
      if (properties == null) {
         builder = this.createTreeBuilder((Properties)null, profile.features());
      } else {
         EnumSet<Builder.Feature> features = EnumSet.noneOf(Builder.Feature.class);
         if (this.getFeatureProperty(profile, properties, Builder.Feature.METHOD_INVOCATIONS, "jinjava.javax.el.methodInvocations")) {
            features.add(Builder.Feature.METHOD_INVOCATIONS);
         }

         if (this.getFeatureProperty(profile, properties, Builder.Feature.VARARGS, "jinjava.javax.el.varArgs")) {
            features.add(Builder.Feature.VARARGS);
         }

         if (this.getFeatureProperty(profile, properties, Builder.Feature.NULL_PROPERTIES, "jinjava.javax.el.nullProperties")) {
            features.add(Builder.Feature.NULL_PROPERTIES);
         }

         if (this.getFeatureProperty(profile, properties, Builder.Feature.IGNORE_RETURN_TYPE, "jinjava.javax.el.ignoreReturnType")) {
            features.add(Builder.Feature.IGNORE_RETURN_TYPE);
         }

         builder = this.createTreeBuilder(properties, (Builder.Feature[])features.toArray(new Builder.Feature[0]));
      }

      int cacheSize = defaultCacheSize;
      if (properties != null && properties.containsKey("jinjava.javax.el.cacheSize")) {
         try {
            cacheSize = Integer.parseInt(properties.getProperty("jinjava.javax.el.cacheSize"));
         } catch (NumberFormatException var7) {
            throw new ELException("Cannot parse EL property javax.el.cacheSize", var7);
         }
      }

      Cache cache = cacheSize > 0 ? new Cache(cacheSize) : null;
      return new TreeStore(builder, cache);
   }

   protected TypeConverter createTypeConverter(Properties properties) {
      Class<?> clazz = this.load(TypeConverter.class, properties);
      if (clazz == null) {
         return TypeConverter.DEFAULT;
      } else {
         try {
            return (TypeConverter)TypeConverter.class.cast(clazz.newInstance());
         } catch (Exception var4) {
            throw new ELException("TypeConverter " + clazz + " could not be instantiated", var4);
         }
      }
   }

   protected TreeBuilder createTreeBuilder(Properties properties, Builder.Feature... features) {
      Class<?> clazz = this.load(TreeBuilder.class, properties);
      if (clazz == null) {
         return new Builder(features);
      } else {
         try {
            if (Builder.class.isAssignableFrom(clazz)) {
               Constructor<?> constructor = clazz.getConstructor(Builder.Feature[].class);
               if (constructor == null) {
                  if (features != null && features.length != 0) {
                     throw new ELException("Builder " + clazz + " is missing constructor (can't pass features)");
                  } else {
                     return (TreeBuilder)TreeBuilder.class.cast(clazz.newInstance());
                  }
               } else {
                  return (TreeBuilder)TreeBuilder.class.cast(constructor.newInstance(features));
               }
            } else {
               return (TreeBuilder)TreeBuilder.class.cast(clazz.newInstance());
            }
         } catch (Exception var5) {
            throw new ELException("TreeBuilder " + clazz + " could not be instantiated", var5);
         }
      }
   }

   private Class<?> load(Class<?> clazz, Properties properties) {
      if (properties != null) {
         String className = properties.getProperty(clazz.getName());
         if (className != null) {
            ClassLoader loader;
            try {
               loader = Thread.currentThread().getContextClassLoader();
            } catch (Exception var8) {
               throw new ELException("Could not get context class loader", var8);
            }

            try {
               return loader == null ? Class.forName(className) : loader.loadClass(className);
            } catch (ClassNotFoundException var6) {
               throw new ELException("Class " + className + " not found", var6);
            } catch (Exception var7) {
               throw new ELException("Class " + className + " could not be instantiated", var7);
            }
         }
      }

      return null;
   }

   public final Object coerceToType(Object obj, Class<?> targetType) {
      return this.converter.convert(obj, targetType);
   }

   public final ObjectValueExpression createValueExpression(Object instance, Class<?> expectedType) {
      return new ObjectValueExpression(this.converter, instance, expectedType);
   }

   public final TreeValueExpression createValueExpression(ELContext context, String expression, Class<?> expectedType) {
      return new TreeValueExpression(this.store, context.getFunctionMapper(), context.getVariableMapper(), this.converter, expression, expectedType);
   }

   public final TreeMethodExpression createMethodExpression(ELContext context, String expression, Class<?> expectedReturnType, Class<?>[] expectedParamTypes) {
      return new TreeMethodExpression(this.store, context.getFunctionMapper(), context.getVariableMapper(), this.converter, expression, expectedReturnType, expectedParamTypes);
   }

   public static enum Profile {
      JEE5(EnumSet.noneOf(Builder.Feature.class)),
      JEE6(EnumSet.of(Builder.Feature.METHOD_INVOCATIONS, Builder.Feature.VARARGS));

      private final EnumSet<Builder.Feature> features;

      private Profile(EnumSet<Builder.Feature> features) {
         this.features = features;
      }

      Builder.Feature[] features() {
         return (Builder.Feature[])this.features.toArray(new Builder.Feature[this.features.size()]);
      }

      boolean contains(Builder.Feature feature) {
         return this.features.contains(feature);
      }
   }
}
