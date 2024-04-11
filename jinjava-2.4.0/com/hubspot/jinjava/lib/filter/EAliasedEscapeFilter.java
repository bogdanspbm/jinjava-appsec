package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;

@JinjavaDoc(
   value = "",
   aliasOf = "escape"
)
public class EAliasedEscapeFilter extends EscapeFilter {
   public String getName() {
      return "e";
   }
}
