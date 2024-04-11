package com.hubspot.jinjava.util;

import com.hubspot.jinjava.interpret.InterpretException;

public final class WhitespaceUtils {
   public static boolean startsWith(String s, String prefix) {
      if (s == null) {
         return false;
      } else {
         for(int i = 0; i < s.length(); ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
               return s.regionMatches(i, prefix, 0, prefix.length());
            }
         }

         return false;
      }
   }

   public static boolean endsWith(String s, String suffix) {
      if (s == null) {
         return false;
      } else {
         for(int i = s.length() - 1; i >= 0; --i) {
            if (!Character.isWhitespace(s.charAt(i))) {
               return s.regionMatches(i - suffix.length() + 1, suffix, 0, suffix.length());
            }
         }

         return false;
      }
   }

   public static boolean isWrappedWith(String s, String prefix, String suffix) {
      return startsWith(s, prefix) && endsWith(s, suffix);
   }

   public static boolean isQuoted(String s) {
      if (startsWith(s, "'")) {
         if (!endsWith(s, "'")) {
            throw new InterpretException("Unbalanced quotes: " + s);
         } else {
            return true;
         }
      } else if (startsWith(s, "\"")) {
         if (!endsWith(s, "\"")) {
            throw new InterpretException("Unbalanced quotes: " + s);
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public static String unquote(String s) {
      if (s == null) {
         return "";
      } else if (startsWith(s, "'")) {
         return unwrap(s, "'", "'");
      } else {
         return startsWith(s, "\"") ? unwrap(s, "\"", "\"") : s.trim();
      }
   }

   public static String unwrap(String s, String prefix, String suffix) {
      int start = 0;

      int end;
      for(end = s.length() - 1; start < s.length() && Character.isWhitespace(s.charAt(start)); ++start) {
      }

      while(end >= 0 && Character.isWhitespace(s.charAt(end))) {
         --end;
      }

      return s.substring(start + prefix.length(), end - suffix.length() + 1);
   }

   private WhitespaceUtils() {
   }
}
