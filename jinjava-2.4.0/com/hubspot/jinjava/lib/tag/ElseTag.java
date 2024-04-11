package com.hubspot.jinjava.lib.tag;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.tree.TagNode;

@JinjavaDoc(
   value = "",
   hidden = true
)
public class ElseTag implements Tag {
   private static final long serialVersionUID = 1082768429113702148L;
   static final String ELSE = "else";

   public String interpret(TagNode tagNode, JinjavaInterpreter interpreter) {
      return "";
   }

   public String getEndTagName() {
      return null;
   }

   public String getName() {
      return "else";
   }
}
