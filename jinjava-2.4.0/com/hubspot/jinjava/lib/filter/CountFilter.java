package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;

@JinjavaDoc(
   value = "",
   aliasOf = "length"
)
public class CountFilter extends LengthFilter {
   public String getName() {
      return "count";
   }
}
