package com.hubspot.jinjava.lib.tag;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.tree.Node;
import com.hubspot.jinjava.tree.TagNode;
import com.hubspot.jinjava.util.LengthLimitingStringBuilder;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Process all inner HubL as plain text",
   snippets = {@JinjavaSnippet(
   code = "{% raw %}\n    The personalization token for a contact's first name is {{ contact.firstname }}\n{% endraw %}"
)}
)
public class RawTag implements Tag {
   private static final long serialVersionUID = -6963360187396753883L;

   public String getName() {
      return "raw";
   }

   public String getEndTagName() {
      return "endraw";
   }

   public String interpret(TagNode tagNode, JinjavaInterpreter interpreter) {
      LengthLimitingStringBuilder result = new LengthLimitingStringBuilder(interpreter.getConfig().getMaxOutputSize());
      Iterator var4 = tagNode.getChildren().iterator();

      while(var4.hasNext()) {
         Node n = (Node)var4.next();
         result.append(this.renderNodeRaw(n));
      }

      return result.toString();
   }

   public String renderNodeRaw(Node n) {
      StringBuilder result = new StringBuilder(n.getMaster().getImage());
      Iterator var3 = n.getChildren().iterator();

      while(var3.hasNext()) {
         Node child = (Node)var3.next();
         result.append(this.renderNodeRaw(child));
      }

      if (TagNode.class.isAssignableFrom(n.getClass())) {
         TagNode t = (TagNode)n;
         if (StringUtils.isNotBlank(t.getEndName())) {
            result.append("{% ").append(t.getEndName()).append(" %}");
         }
      }

      return result.toString();
   }
}
