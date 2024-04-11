package com.hubspot.jinjava.lib.exptest;

import com.hubspot.jinjava.lib.SimpleLibrary;
import java.util.Set;

public class ExpTestLibrary extends SimpleLibrary<ExpTest> {
   public ExpTestLibrary(boolean registerDefaults, Set<String> disabled) {
      super(registerDefaults, disabled);
   }

   protected void registerDefaults() {
      this.registerClasses(new Class[]{IsDefinedExpTest.class, IsDivisibleByExpTest.class, IsEqualToExpTest.class, IsEvenExpTest.class, IsIterableExpTest.class, IsLowerExpTest.class, IsMappingExpTest.class, IsNoneExpTest.class, IsNumberExpTest.class, IsOddExpTest.class, IsSameAsExpTest.class, IsSequenceExpTest.class, IsStringExpTest.class, IsStringContainingExpTest.class, IsStringStartingWithExpTest.class, IsTruthyExpTest.class, IsUndefinedExpTest.class, IsUpperExpTest.class, IsContainingAllExpTest.class, IsContainingExpTest.class, IsWithinExpTest.class});
   }

   public ExpTest getExpTest(String name) {
      return (ExpTest)this.fetch(name);
   }

   public void addExpTest(ExpTest expTest) {
      this.register(expTest);
   }
}
