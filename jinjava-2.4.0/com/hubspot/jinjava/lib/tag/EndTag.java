package com.hubspot.jinjava.lib.tag;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.tree.TagNode;

@JinjavaDoc(
   value = "",
   hidden = true
)
public final class EndTag implements Tag {
   private static final long serialVersionUID = -3309842733119867221L;
   private final String endTagName;

   public EndTag(Tag tag) {
      this.endTagName = tag.getEndTagName();
   }

   public String getName() {
      return this.endTagName;
   }

   public String interpret(TagNode tagNode, JinjavaInterpreter interpreter) {
      return "";
   }

   public String getEndTagName() {
      return this.endTagName;
   }
}
