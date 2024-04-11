package com.hubspot.jinjava.random;

import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class ConstantZeroRandomNumberGenerator extends Random {
   protected int next(int bits) {
      return 0;
   }

   public int nextInt() {
      return 0;
   }

   public int nextInt(int bound) {
      return 0;
   }

   public long nextLong() {
      return 0L;
   }

   public boolean nextBoolean() {
      return false;
   }

   public float nextFloat() {
      return 0.0F;
   }

   public double nextDouble() {
      return 0.0D;
   }

   public synchronized double nextGaussian() {
      return 0.0D;
   }

   public void nextBytes(byte[] bytes) {
      throw new UnsupportedOperationException();
   }

   public IntStream ints(long streamSize) {
      throw new UnsupportedOperationException();
   }

   public IntStream ints() {
      throw new UnsupportedOperationException();
   }

   public IntStream ints(long streamSize, int randomNumberOrigin, int randomNumberBound) {
      throw new UnsupportedOperationException();
   }

   public IntStream ints(int randomNumberOrigin, int randomNumberBound) {
      throw new UnsupportedOperationException();
   }

   public LongStream longs(long streamSize) {
      throw new UnsupportedOperationException();
   }

   public LongStream longs() {
      throw new UnsupportedOperationException();
   }

   public LongStream longs(long streamSize, long randomNumberOrigin, long randomNumberBound) {
      throw new UnsupportedOperationException();
   }

   public LongStream longs(long randomNumberOrigin, long randomNumberBound) {
      throw new UnsupportedOperationException();
   }

   public DoubleStream doubles(long streamSize) {
      throw new UnsupportedOperationException();
   }

   public DoubleStream doubles() {
      throw new UnsupportedOperationException();
   }

   public DoubleStream doubles(long streamSize, double randomNumberOrigin, double randomNumberBound) {
      throw new UnsupportedOperationException();
   }

   public DoubleStream doubles(double randomNumberOrigin, double randomNumberBound) {
      throw new UnsupportedOperationException();
   }
}
