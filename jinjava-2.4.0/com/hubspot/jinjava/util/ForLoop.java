package com.hubspot.jinjava.util;

import java.util.Iterator;

public class ForLoop implements Iterator<Object> {
   private static final int NULL_VALUE = Integer.MIN_VALUE;
   private int index = -1;
   private int counter = 0;
   private int revindex = Integer.MIN_VALUE;
   private int revcounter = Integer.MIN_VALUE;
   private int length = Integer.MIN_VALUE;
   private boolean first = true;
   private boolean last;
   private int depth;
   private Iterator<?> it;

   public ForLoop(Iterator<?> ite, int len) {
      this.length = len;
      if (len < 2) {
         this.revindex = 1;
         this.revcounter = 2;
         this.last = true;
      } else {
         this.revindex = len;
         this.revcounter = len + 1;
         this.last = false;
      }

      this.it = ite;
   }

   public ForLoop(Iterator<?> ite) {
      this.it = ite;
      if (this.it.hasNext()) {
         this.last = false;
      } else {
         this.length = 0;
         this.revindex = 1;
         this.revcounter = 2;
         this.last = true;
      }

   }

   public Object next() {
      Object res;
      if (this.it.hasNext()) {
         ++this.index;
         ++this.counter;
         if (this.length != Integer.MIN_VALUE) {
            --this.revindex;
            --this.revcounter;
         }

         res = this.it.next();
         if (!this.it.hasNext()) {
            this.last = true;
            this.length = this.counter;
            this.revindex = 0;
            this.revcounter = 1;
         }

         if (this.index > 0) {
            this.first = false;
         }
      } else {
         res = null;
      }

      return res;
   }

   public int getIndex() {
      return this.index + 1;
   }

   public int getIndex0() {
      return this.index;
   }

   public int getDepth() {
      return this.depth + 1;
   }

   public int getDepth0() {
      return this.depth;
   }

   public int getCounter() {
      return this.counter;
   }

   public int getRevindex() {
      return this.getRevindex0() + 1;
   }

   public int getRevindex0() {
      if (this.revindex == Integer.MIN_VALUE) {
         Logging.ENGINE_LOG.warn("can't compute items' length while looping.");
      }

      return this.revindex;
   }

   public int getRevcounter() {
      if (this.revcounter == Integer.MIN_VALUE) {
         Logging.ENGINE_LOG.warn("can't compute items' length while looping.");
      }

      return this.revcounter;
   }

   public int getLength() {
      if (this.revcounter == Integer.MIN_VALUE) {
         Logging.ENGINE_LOG.warn("can't compute items' length while looping.");
      }

      return this.length;
   }

   public boolean isFirst() {
      return this.first;
   }

   public boolean isLast() {
      return this.last;
   }

   public boolean hasNext() {
      return this.it.hasNext();
   }

   public Object cycle(Object... items) {
      int i = this.getIndex0() % items.length;
      return items[i];
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }
}
