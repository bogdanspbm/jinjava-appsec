package com.hubspot.jinjava.objects.date;

import com.hubspot.jinjava.objects.PyWrapper;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.math.NumberUtils;

public final class PyishDate extends Date implements Serializable, PyWrapper {
   private static final long serialVersionUID = 1L;
   private final ZonedDateTime date;

   public PyishDate(ZonedDateTime dt) {
      super(dt.toInstant().toEpochMilli());
      this.date = dt;
   }

   public PyishDate(Date d) {
      this(ZonedDateTime.ofInstant(d.toInstant(), ZoneOffset.UTC));
   }

   public PyishDate(String publishDateStr) {
      this(NumberUtils.toLong((String)Objects.requireNonNull(publishDateStr), 0L));
   }

   public PyishDate(Long epochMillis) {
      this(ZonedDateTime.ofInstant(Instant.ofEpochMilli((Long)Optional.ofNullable(epochMillis).orElseGet(System::currentTimeMillis)), ZoneOffset.UTC));
   }

   public PyishDate(Instant instant) {
      this(ZonedDateTime.ofInstant(instant, ZoneOffset.UTC));
   }

   public String isoformat() {
      return this.strftime("yyyy-MM-dd");
   }

   public String strftime(String fmt) {
      return StrftimeFormatter.format(this.date, fmt);
   }

   public int getYear() {
      return this.date.getYear();
   }

   public int getMonth() {
      return this.date.getMonthValue();
   }

   public int getDay() {
      return this.date.getDayOfMonth();
   }

   public int getHour() {
      return this.date.getHour();
   }

   public int getMinute() {
      return this.date.getMinute();
   }

   public int getSecond() {
      return this.date.getSecond();
   }

   public int getMicrosecond() {
      return this.date.get(ChronoField.MILLI_OF_SECOND);
   }

   public Date toDate() {
      return Date.from(this.date.toInstant());
   }

   public ZonedDateTime toDateTime() {
      return this.date;
   }

   public String toString() {
      return this.strftime("yyyy-MM-dd HH:mm:ss");
   }

   public int hashCode() {
      return Objects.hashCode(this.date);
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         PyishDate that = (PyishDate)obj;
         return Objects.equals(this.toDateTime(), that.toDateTime());
      } else {
         return false;
      }
   }
}
