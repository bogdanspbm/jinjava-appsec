package com.hubspot.jinjava.objects.collections;

import com.google.common.collect.ForwardingMap;
import com.hubspot.jinjava.objects.PyWrapper;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class PyMap extends ForwardingMap<String, Object> implements PyWrapper {
   private Map<String, Object> map;

   public PyMap(Map<String, Object> map) {
      this.map = map;
   }

   protected Map<String, Object> delegate() {
      return this.map;
   }

   public String toString() {
      return this.delegate().toString();
   }

   public Map<String, Object> toMap() {
      return this.map;
   }

   public Set<Entry<String, Object>> items() {
      return this.entrySet();
   }

   public void update(Map<? extends String, ? extends Object> m) {
      this.putAll(m);
   }
}
