package com.hubspot.jinjava.lib;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.hubspot.jinjava.interpret.DisabledException;
import com.hubspot.jinjava.util.Logging;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class SimpleLibrary<T extends Importable> {
   private Map<String, T> lib;
   private Set<String> disabled;

   protected SimpleLibrary(boolean registerDefaults) {
      this(registerDefaults, (Set)null);
   }

   protected SimpleLibrary(boolean registerDefaults, Set<String> disabled) {
      this.lib = new HashMap();
      this.disabled = new HashSet();
      if (disabled != null) {
         this.disabled = ImmutableSet.copyOf(disabled);
      }

      if (registerDefaults) {
         this.registerDefaults();
      }

   }

   protected abstract void registerDefaults();

   public T fetch(String item) {
      if (this.disabled.contains(item)) {
         throw new DisabledException(item);
      } else {
         return (Importable)this.lib.get(item);
      }
   }

   @SafeVarargs
   public final List<T> registerClasses(Class<? extends T>... itemClass) {
      try {
         List<T> instances = new ArrayList();
         Class[] var3 = itemClass;
         int var4 = itemClass.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Class<? extends T> c = var3[var5];
            T instance = (Importable)c.newInstance();
            this.register(instance);
            instances.add(instance);
         }

         return instances;
      } catch (Exception var8) {
         throw Throwables.propagate(var8);
      }
   }

   public void register(T obj) {
      this.register(obj.getName(), obj);
   }

   public void register(String name, T obj) {
      if (!this.disabled.contains(obj.getName())) {
         this.lib.put(name, obj);
         Logging.ENGINE_LOG.debug(this.getClass().getSimpleName() + ": Registered " + obj.getName());
      }

   }

   public Collection<T> entries() {
      return (Collection)this.lib.values().stream().filter((t) -> {
         return !this.disabled.contains(t.getName());
      }).collect(Collectors.toSet());
   }
}
