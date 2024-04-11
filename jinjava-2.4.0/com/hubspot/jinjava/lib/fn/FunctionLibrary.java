package com.hubspot.jinjava.lib.fn;

import com.google.common.collect.Lists;
import com.hubspot.jinjava.lib.SimpleLibrary;
import java.util.Set;

public class FunctionLibrary extends SimpleLibrary<ELFunctionDefinition> {
   public FunctionLibrary(boolean registerDefaults, Set<String> disabled) {
      super(registerDefaults, disabled);
   }

   protected void registerDefaults() {
      this.register(new ELFunctionDefinition("", "datetimeformat", Functions.class, "dateTimeFormat", new Class[]{Object.class, String[].class}));
      this.register(new ELFunctionDefinition("", "unixtimestamp", Functions.class, "unixtimestamp", new Class[]{Object.class}));
      this.register(new ELFunctionDefinition("", "truncate", Functions.class, "truncate", new Class[]{Object.class, Object[].class}));
      this.register(new ELFunctionDefinition("", "range", Functions.class, "range", new Class[]{Object.class, Object[].class}));
      this.register(new ELFunctionDefinition("", "type", TypeFunction.class, "type", new Class[]{Object.class}));
      this.register(new ELFunctionDefinition("", "super", Functions.class, "renderSuperBlock", new Class[0]));
      this.register(new ELFunctionDefinition("fn", "list", Lists.class, "newArrayList", new Class[]{Object[].class}));
      this.register(new ELFunctionDefinition("fn", "immutable_list", Functions.class, "immutableListOf", new Class[]{Object[].class}));
   }

   public void addFunction(ELFunctionDefinition fn) {
      this.register(fn);
   }

   public ELFunctionDefinition getFunction(String name) {
      return (ELFunctionDefinition)this.fetch(name);
   }
}
