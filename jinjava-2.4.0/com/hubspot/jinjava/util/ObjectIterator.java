package com.hubspot.jinjava.util;

import com.google.common.collect.Iterators;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public final class ObjectIterator {
   private ObjectIterator() {
   }

   public static ForLoop getLoop(Object obj) {
      if (obj == null) {
         return new ForLoop(Collections.emptyIterator(), 0);
      } else {
         Collection clt;
         if (obj instanceof Collection) {
            clt = (Collection)obj;
            return new ForLoop(clt.iterator(), clt.size());
         } else if (obj.getClass().isArray()) {
            Object[] arr = (Object[])((Object[])obj);
            return new ForLoop(Iterators.forArray(arr), arr.length);
         } else if (obj instanceof Map) {
            clt = ((Map)obj).values();
            return new ForLoop(clt.iterator(), clt.size());
         } else if (obj instanceof Iterable) {
            return new ForLoop(((Iterable)obj).iterator());
         } else {
            return obj instanceof Iterator ? new ForLoop((Iterator)obj) : new ForLoop(Iterators.singletonIterator(obj), 1);
         }
      }
   }
}
