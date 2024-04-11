package com.hubspot.jinjava.util;

import com.hubspot.jinjava.interpret.OutputTooBigException;
import java.io.Serializable;
import java.util.stream.IntStream;

public class LengthLimitingStringBuilder implements Serializable, CharSequence {
   private static final long serialVersionUID = -1891922886257965755L;
   private final StringBuilder builder = new StringBuilder();
   private long length = 0L;
   private final long maxLength;

   public LengthLimitingStringBuilder(long maxLength) {
      this.maxLength = maxLength;
   }

   public int length() {
      return this.builder.length();
   }

   public char charAt(int index) {
      return this.builder.charAt(index);
   }

   public CharSequence subSequence(int start, int end) {
      return this.builder.subSequence(start, end);
   }

   public String toString() {
      return this.builder.toString();
   }

   public IntStream chars() {
      return this.builder.chars();
   }

   public IntStream codePoints() {
      return this.builder.codePoints();
   }

   public void append(Object obj) {
      this.append(String.valueOf(obj));
   }

   public void append(String str) {
      this.length += (long)str.length();
      if (this.maxLength > 0L && this.length > this.maxLength) {
         throw new OutputTooBigException(this.maxLength, this.length);
      } else {
         this.builder.append(str);
      }
   }
}
