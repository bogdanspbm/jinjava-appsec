package com.hubspot.jinjava.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ScopeMap<K, V> implements Map<K, V> {
   private final Map<K, V> scope;
   private final ScopeMap<K, V> parent;

   public ScopeMap() {
      this((ScopeMap)null);
   }

   public ScopeMap(ScopeMap<K, V> parent) {
      this.scope = new HashMap();
      this.parent = parent;
   }

   public ScopeMap(ScopeMap<K, V> parent, Map<K, V> scope) {
      this(parent);
      this.scope.putAll(scope);
   }

   public ScopeMap<K, V> getParent() {
      return this.parent;
   }

   public Map<K, V> getScope() {
      return this.scope;
   }

   public int size() {
      return this.keySet().size();
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean containsKey(Object key) {
      return this.get(key) != null;
   }

   public boolean containsValue(Object value) {
      if (this.scope.containsValue(value)) {
         return true;
      } else {
         return this.parent != null ? this.parent.containsValue(value) : false;
      }
   }

   public V get(Object key, V defVal) {
      V val = this.get(key);
      return val != null ? val : defVal;
   }

   public V get(Object key) {
      V val = this.scope.get(key);
      if (val != null) {
         return val;
      } else {
         return this.parent != null ? this.parent.get(key) : null;
      }
   }

   public V put(K key, V value) {
      return this.scope.put(key, value);
   }

   public V remove(Object key) {
      return this.scope.remove(key);
   }

   public void putAll(Map<? extends K, ? extends V> m) {
      this.scope.putAll(m);
   }

   public void clear() {
      this.scope.clear();
   }

   public Set<K> keySet() {
      Set<K> keys = new HashSet();
      if (this.parent != null) {
         keys.addAll(this.parent.keySet());
      }

      keys.addAll(this.scope.keySet());
      return keys;
   }

   public Collection<V> values() {
      Set<Entry<K, V>> entrySet = this.entrySet();
      Collection<V> values = new ArrayList(entrySet.size());
      Iterator var3 = entrySet.iterator();

      while(var3.hasNext()) {
         Entry<K, V> entry = (Entry)var3.next();
         values.add(entry.getValue());
      }

      return values;
   }

   @SuppressFBWarnings(
      justification = "using overridden get() to do scoped retrieve with parent fallback",
      value = {"WMI_WRONG_MAP_ITERATOR"}
   )
   public Set<Entry<K, V>> entrySet() {
      Set<Entry<K, V>> entries = new HashSet();
      Iterator var2 = this.keySet().iterator();

      while(var2.hasNext()) {
         K key = var2.next();
         entries.add(new ScopeMap.ScopeMapEntry(key, this.get(key), this));
      }

      return entries;
   }

   public static class ScopeMapEntry<K, V> implements Entry<K, V> {
      private final Map<K, V> map;
      private final K key;
      private V value;

      public ScopeMapEntry(K key, V value, Map<K, V> map) {
         this.key = key;
         this.value = value;
         this.map = map;
      }

      public K getKey() {
         return this.key;
      }

      public V getValue() {
         return this.value;
      }

      public V setValue(V value) {
         this.value = value;
         this.map.put(this.key, value);
         return value;
      }
   }
}
