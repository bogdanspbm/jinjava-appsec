package com.hubspot.jinjava.objects.date;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

public class StrftimeFormatter {
   public static final String DEFAULT_DATE_FORMAT = "%H:%M / %d-%m-%Y";
   private static final String[] CONVERSIONS = new String[255];

   private static String toJavaDateTimeFormat(String strftime) {
      if (!StringUtils.contains(strftime, 37)) {
         return replaceL(strftime);
      } else {
         StringBuilder result = new StringBuilder();

         for(int i = 0; i < strftime.length(); ++i) {
            char c = strftime.charAt(i);
            if (c == '%') {
               ++i;
               c = strftime.charAt(i);
               boolean stripLeadingZero = false;
               if (c == '-') {
                  stripLeadingZero = true;
                  ++i;
                  c = strftime.charAt(i);
               }

               if (stripLeadingZero) {
                  result.append(CONVERSIONS[c].substring(1));
               } else {
                  result.append(CONVERSIONS[c]);
               }
            } else if (!Character.isLetter(c)) {
               result.append(c);
            } else {
               result.append("'");

               while(Character.isLetter(c)) {
                  result.append(c);
                  ++i;
                  if (i < strftime.length()) {
                     c = strftime.charAt(i);
                  } else {
                     c = 0;
                  }
               }

               result.append("'");
               --i;
            }
         }

         return replaceL(result.toString());
      }
   }

   private static String replaceL(String s) {
      return StringUtils.replaceChars(s, 'L', 'M');
   }

   public static DateTimeFormatter formatter(String strftime) {
      return formatter(strftime, Locale.ENGLISH);
   }

   public static DateTimeFormatter formatter(String strftime, Locale locale) {
      String var2 = strftime.toLowerCase();
      byte var3 = -1;
      switch(var2.hashCode()) {
      case -1078030475:
         if (var2.equals("medium")) {
            var3 = 1;
         }
         break;
      case 3154575:
         if (var2.equals("full")) {
            var3 = 3;
         }
         break;
      case 3327612:
         if (var2.equals("long")) {
            var3 = 2;
         }
         break;
      case 109413500:
         if (var2.equals("short")) {
            var3 = 0;
         }
      }

      DateTimeFormatter fmt;
      switch(var3) {
      case 0:
         fmt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
         break;
      case 1:
         fmt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
         break;
      case 2:
         fmt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
         break;
      case 3:
         fmt = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL);
         break;
      default:
         try {
            fmt = DateTimeFormatter.ofPattern(toJavaDateTimeFormat(strftime));
         } catch (IllegalArgumentException var6) {
            throw new InvalidDateFormatException(strftime, var6);
         }
      }

      return fmt.withLocale(locale);
   }

   public static String format(ZonedDateTime d) {
      return format(d, "%H:%M / %d-%m-%Y");
   }

   public static String format(ZonedDateTime d, Locale locale) {
      return format(d, "%H:%M / %d-%m-%Y", locale);
   }

   public static String format(ZonedDateTime d, String strftime) {
      return format(d, strftime, Locale.ENGLISH);
   }

   public static String format(ZonedDateTime d, String strftime, Locale locale) {
      return formatter(strftime, locale).format(d);
   }

   static {
      CONVERSIONS[97] = "EEE";
      CONVERSIONS[65] = "EEEE";
      CONVERSIONS[98] = "MMM";
      CONVERSIONS[66] = "MMMM";
      CONVERSIONS[99] = "EEE MMM dd HH:mm:ss yyyy";
      CONVERSIONS[100] = "dd";
      CONVERSIONS[101] = "d";
      CONVERSIONS[102] = "SSSS";
      CONVERSIONS[72] = "HH";
      CONVERSIONS[104] = "hh";
      CONVERSIONS[73] = "hh";
      CONVERSIONS[106] = "DDD";
      CONVERSIONS[107] = "H";
      CONVERSIONS[108] = "h";
      CONVERSIONS[109] = "MM";
      CONVERSIONS[77] = "mm";
      CONVERSIONS[112] = "a";
      CONVERSIONS[83] = "ss";
      CONVERSIONS[85] = "ww";
      CONVERSIONS[119] = "e";
      CONVERSIONS[87] = "ww";
      CONVERSIONS[120] = "MM/dd/yy";
      CONVERSIONS[88] = "HH:mm:ss";
      CONVERSIONS[121] = "yy";
      CONVERSIONS[89] = "yyyy";
      CONVERSIONS[122] = "Z";
      CONVERSIONS[90] = "ZZZ";
      CONVERSIONS[37] = "%";
   }
}
