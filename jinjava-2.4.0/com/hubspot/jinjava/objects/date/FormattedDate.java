package com.hubspot.jinjava.objects.date;

import java.time.ZonedDateTime;

public class FormattedDate {
   private final String format;
   private final String language;
   private final ZonedDateTime date;

   public FormattedDate(String format, String language, ZonedDateTime date) {
      this.format = format;
      this.language = language;
      this.date = date;
   }

   public String getFormat() {
      return this.format;
   }

   public ZonedDateTime getDate() {
      return this.date;
   }

   public String getLanguage() {
      return this.language;
   }
}
