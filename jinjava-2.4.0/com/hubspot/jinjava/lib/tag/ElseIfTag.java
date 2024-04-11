package com.hubspot.jinjava.lib.tag;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.tree.TagNode;

@JinjavaDoc(
   value = "",
   hidden = true
)
public class ElseIfTag implements Tag {
   private static final long serialVersionUID = -7988057025956316803L;
   static final String ELSEIF = "elif";

   public String interpret(TagNode tagNode, JinjavaInterpreter interpreter) {
      return "";
   }

   public String getEndTagName() {
      return null;
   }

   public String getName() {
      return "elif";
   }
}
