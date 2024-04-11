package com.hubspot.jinjava.util;

public class CharArrayUtils {
   public static boolean charArrayRegionMatches(char[] value, int startPos, CharSequence toMatch) {
      int matchLen = toMatch.length();
      int endPos = startPos + matchLen;
      if (endPos > value.length) {
         return false;
      } else {
         int matchIndex = 0;

         for(int i = startPos; i < endPos; ++matchIndex) {
            if (value[i] != toMatch.charAt(matchIndex)) {
               return false;
            }

            ++i;
         }

         return true;
      }
   }
}
