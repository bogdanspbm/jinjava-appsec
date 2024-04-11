package com.hubspot.jinjava;

import com.hubspot.jinjava.interpret.Context;
import com.hubspot.jinjava.random.RandomNumberGeneratorStrategy;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class JinjavaConfig {
   private final Charset charset;
   private final Locale locale;
   private final ZoneId timeZone;
   private final int maxRenderDepth;
   private final long maxOutputSize;
   private final boolean trimBlocks;
   private final boolean lstripBlocks;
   private final boolean readOnlyResolver;
   private final boolean enableRecursiveMacroCalls;
   private Map<Context.Library, Set<String>> disabled;
   private final boolean failOnUnknownTokens;
   private final boolean nestedInterpretationEnabled;
   private final RandomNumberGeneratorStrategy randomNumberGenerator;

   public static JinjavaConfig.Builder newBuilder() {
      return new JinjavaConfig.Builder();
   }

   public JinjavaConfig() {
      this(StandardCharsets.UTF_8, Locale.ENGLISH, ZoneOffset.UTC, 10, new HashMap(), false, false, true, false, false, 0L, true, RandomNumberGeneratorStrategy.THREAD_LOCAL);
   }

   public JinjavaConfig(Charset charset, Locale locale, ZoneId timeZone, int maxRenderDepth) {
      this(charset, locale, timeZone, maxRenderDepth, new HashMap(), false, false, true, false, false, 0L, true, RandomNumberGeneratorStrategy.THREAD_LOCAL);
   }

   private JinjavaConfig(Charset charset, Locale locale, ZoneId timeZone, int maxRenderDepth, Map<Context.Library, Set<String>> disabled, boolean trimBlocks, boolean lstripBlocks, boolean readOnlyResolver, boolean enableRecursiveMacroCalls, boolean failOnUnknownTokens, long maxOutputSize, boolean nestedInterpretationEnabled, RandomNumberGeneratorStrategy randomNumberGenerator) {
      this.charset = charset;
      this.locale = locale;
      this.timeZone = timeZone;
      this.maxRenderDepth = maxRenderDepth;
      this.disabled = disabled;
      this.trimBlocks = trimBlocks;
      this.lstripBlocks = lstripBlocks;
      this.readOnlyResolver = readOnlyResolver;
      this.enableRecursiveMacroCalls = enableRecursiveMacroCalls;
      this.failOnUnknownTokens = failOnUnknownTokens;
      this.maxOutputSize = maxOutputSize;
      this.nestedInterpretationEnabled = nestedInterpretationEnabled;
      this.randomNumberGenerator = randomNumberGenerator;
   }

   public Charset getCharset() {
      return this.charset;
   }

   public Locale getLocale() {
      return this.locale;
   }

   public ZoneId getTimeZone() {
      return this.timeZone;
   }

   public int getMaxRenderDepth() {
      return this.maxRenderDepth;
   }

   public long getMaxOutputSize() {
      return this.maxOutputSize;
   }

   public RandomNumberGeneratorStrategy getRandomNumberGeneratorStrategy() {
      return this.randomNumberGenerator;
   }

   public boolean isTrimBlocks() {
      return this.trimBlocks;
   }

   public boolean isLstripBlocks() {
      return this.lstripBlocks;
   }

   public boolean isReadOnlyResolver() {
      return this.readOnlyResolver;
   }

   public boolean isEnableRecursiveMacroCalls() {
      return this.enableRecursiveMacroCalls;
   }

   public Map<Context.Library, Set<String>> getDisabled() {
      return this.disabled;
   }

   public boolean isFailOnUnknownTokens() {
      return this.failOnUnknownTokens;
   }

   public boolean isNestedInterpretationEnabled() {
      return this.nestedInterpretationEnabled;
   }

   // $FF: synthetic method
   JinjavaConfig(Charset x0, Locale x1, ZoneId x2, int x3, Map x4, boolean x5, boolean x6, boolean x7, boolean x8, boolean x9, long x10, boolean x11, RandomNumberGeneratorStrategy x12, Object x13) {
      this(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12);
   }

   public static class Builder {
      private Charset charset;
      private Locale locale;
      private ZoneId timeZone;
      private int maxRenderDepth;
      private long maxOutputSize;
      private Map<Context.Library, Set<String>> disabled;
      private boolean trimBlocks;
      private boolean lstripBlocks;
      private boolean readOnlyResolver;
      private boolean enableRecursiveMacroCalls;
      private boolean failOnUnknownTokens;
      private boolean nestedInterpretationEnabled;
      private RandomNumberGeneratorStrategy randomNumberGeneratorStrategy;

      private Builder() {
         this.charset = StandardCharsets.UTF_8;
         this.locale = Locale.ENGLISH;
         this.timeZone = ZoneOffset.UTC;
         this.maxRenderDepth = 10;
         this.maxOutputSize = 0L;
         this.disabled = new HashMap();
         this.readOnlyResolver = true;
         this.nestedInterpretationEnabled = true;
         this.randomNumberGeneratorStrategy = RandomNumberGeneratorStrategy.THREAD_LOCAL;
      }

      public JinjavaConfig.Builder withCharset(Charset charset) {
         this.charset = charset;
         return this;
      }

      public JinjavaConfig.Builder withLocale(Locale locale) {
         this.locale = locale;
         return this;
      }

      public JinjavaConfig.Builder withTimeZone(ZoneId timeZone) {
         this.timeZone = timeZone;
         return this;
      }

      public JinjavaConfig.Builder withDisabled(Map<Context.Library, Set<String>> disabled) {
         this.disabled = disabled;
         return this;
      }

      public JinjavaConfig.Builder withMaxRenderDepth(int maxRenderDepth) {
         this.maxRenderDepth = maxRenderDepth;
         return this;
      }

      public JinjavaConfig.Builder withRandomNumberGeneratorStrategy(RandomNumberGeneratorStrategy randomNumberGeneratorStrategy) {
         this.randomNumberGeneratorStrategy = randomNumberGeneratorStrategy;
         return this;
      }

      public JinjavaConfig.Builder withTrimBlocks(boolean trimBlocks) {
         this.trimBlocks = trimBlocks;
         return this;
      }

      public JinjavaConfig.Builder withLstripBlocks(boolean lstripBlocks) {
         this.lstripBlocks = lstripBlocks;
         return this;
      }

      public JinjavaConfig.Builder withEnableRecursiveMacroCalls(boolean enableRecursiveMacroCalls) {
         this.enableRecursiveMacroCalls = enableRecursiveMacroCalls;
         return this;
      }

      public JinjavaConfig.Builder withReadOnlyResolver(boolean readOnlyResolver) {
         this.readOnlyResolver = readOnlyResolver;
         return this;
      }

      public JinjavaConfig.Builder withFailOnUnknownTokens(boolean failOnUnknownTokens) {
         this.failOnUnknownTokens = failOnUnknownTokens;
         return this;
      }

      public JinjavaConfig.Builder withMaxOutputSize(long maxOutputSize) {
         this.maxOutputSize = maxOutputSize;
         return this;
      }

      public JinjavaConfig.Builder withNestedInterpretationEnabled(boolean nestedInterpretationEnabled) {
         this.nestedInterpretationEnabled = nestedInterpretationEnabled;
         return this;
      }

      public JinjavaConfig build() {
         return new JinjavaConfig(this.charset, this.locale, this.timeZone, this.maxRenderDepth, this.disabled, this.trimBlocks, this.lstripBlocks, this.readOnlyResolver, this.enableRecursiveMacroCalls, this.failOnUnknownTokens, this.maxOutputSize, this.nestedInterpretationEnabled, this.randomNumberGeneratorStrategy);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
