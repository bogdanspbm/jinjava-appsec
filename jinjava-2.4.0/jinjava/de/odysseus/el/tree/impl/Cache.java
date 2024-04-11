package jinjava.de.odysseus.el.tree.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import jinjava.de.odysseus.el.tree.Tree;
import jinjava.de.odysseus.el.tree.TreeCache;

public final class Cache implements TreeCache {
   private final ConcurrentMap<String, Tree> map;
   private final ConcurrentLinkedQueue<String> queue;
   private final AtomicInteger size;
   private final int capacity;

   public Cache(int capacity) {
      this(capacity, 16);
   }

   public Cache(int capacity, int concurrencyLevel) {
      this.map = new ConcurrentHashMap(16, 0.75F, concurrencyLevel);
      this.queue = new ConcurrentLinkedQueue();
      this.size = new AtomicInteger();
      this.capacity = capacity;
   }

   public int size() {
      return this.size.get();
   }

   public Tree get(String expression) {
      return (Tree)this.map.get(expression);
   }

   public void put(String expression, Tree tree) {
      if (this.map.putIfAbsent(expression, tree) == null) {
         this.queue.offer(expression);
         if (this.size.incrementAndGet() > this.capacity) {
            this.size.decrementAndGet();
            this.map.remove(this.queue.poll());
         }
      }

   }
}
