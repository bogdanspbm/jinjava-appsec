package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;

@JinjavaDoc(
   value = "",
   aliasOf = "default"
)
public class DAliasedDefaultFilter extends DefaultFilter {
   public String getName() {
      return "d";
   }
}
