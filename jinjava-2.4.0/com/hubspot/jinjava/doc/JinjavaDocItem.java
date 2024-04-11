package com.hubspot.jinjava.doc;

import java.util.Map;

public abstract class JinjavaDocItem {
   private final String name;
   private final String desc;
   private final String aliasOf;
   private final boolean deprecated;
   private final JinjavaDocParam[] params;
   private final JinjavaDocSnippet[] snippets;
   private final Map<String, String> meta;

   public JinjavaDocItem(String name, String desc, String aliasOf, boolean deprecated, JinjavaDocParam[] params, JinjavaDocSnippet[] snippets, Map<String, String> meta) {
      this.name = name;
      this.desc = desc;
      this.aliasOf = aliasOf;
      this.deprecated = deprecated;
      this.params = (JinjavaDocParam[])params.clone();
      this.snippets = (JinjavaDocSnippet[])snippets.clone();
      this.meta = meta;
   }

   public String getName() {
      return this.name;
   }

   public String getDesc() {
      return this.desc;
   }

   public String getAliasOf() {
      return this.aliasOf;
   }

   public boolean isDeprecated() {
      return this.deprecated;
   }

   public JinjavaDocParam[] getParams() {
      return (JinjavaDocParam[])this.params.clone();
   }

   public JinjavaDocSnippet[] getSnippets() {
      return (JinjavaDocSnippet[])this.snippets.clone();
   }

   public Map<String, String> getMeta() {
      return this.meta;
   }
}
