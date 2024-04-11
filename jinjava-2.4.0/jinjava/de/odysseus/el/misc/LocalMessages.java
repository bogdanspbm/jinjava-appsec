package jinjava.de.odysseus.el.misc;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class LocalMessages {
   private static final String BUNDLE_NAME = "jinjava.de.odysseus.el.misc.LocalStrings";
   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("jinjava.de.odysseus.el.misc.LocalStrings");

   public static String get(String key, Object... args) {
      String template = null;

      try {
         template = RESOURCE_BUNDLE.getString(key);
      } catch (MissingResourceException var7) {
         StringBuilder b = new StringBuilder();

         try {
            b.append(RESOURCE_BUNDLE.getString("message.unknown"));
            b.append(": ");
         } catch (MissingResourceException var6) {
         }

         b.append(key);
         if (args != null && args.length > 0) {
            b.append("(");
            b.append(args[0]);

            for(int i = 1; i < args.length; ++i) {
               b.append(", ");
               b.append(args[i]);
            }

            b.append(")");
         }

         return b.toString();
      }

      return MessageFormat.format(template, args);
   }
}
