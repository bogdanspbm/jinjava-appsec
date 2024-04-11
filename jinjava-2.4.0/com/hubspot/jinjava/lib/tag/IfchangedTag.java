package com.hubspot.jinjava.lib.tag;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.interpret.TemplateSyntaxException;
import com.hubspot.jinjava.tree.Node;
import com.hubspot.jinjava.tree.TagNode;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Outputs the tag contents if the given variable has changed since a prior invocation of this tag",
   hidden = true,
   snippets = {@JinjavaSnippet(
   code = "{% ifchanged var %}\nVariable to test if changed\n{% endifchanged %}"
)}
)
public class IfchangedTag implements Tag {
   private static final long serialVersionUID = 3567908136629704724L;
   private static final String LASTKEY = "'IF\"CHG";
   private static final String TAGNAME = "ifchanged";
   private static final String ENDTAGNAME = "endifchanged";

   public String interpret(TagNode tagNode, JinjavaInterpreter interpreter) {
      if (StringUtils.isBlank(tagNode.getHelpers())) {
         throw new TemplateSyntaxException(tagNode.getMaster().getImage(), "Tag 'ifchanged' expects a variable parameter", tagNode.getLineNumber(), tagNode.getStartPosition());
      } else {
         boolean isChanged = true;
         String var = tagNode.getHelpers().trim();
         Object older = interpreter.getContext().get("'IF\"CHG" + var);
         Object test = interpreter.retraceVariable(var, tagNode.getLineNumber(), tagNode.getStartPosition());
         if (older == null) {
            if (test == null) {
               isChanged = false;
            }
         } else if (older.equals(test)) {
            isChanged = false;
         }

         interpreter.getContext().put("'IF\"CHG" + var, test);
         if (!isChanged) {
            return "";
         } else {
            StringBuilder sb = new StringBuilder();
            Iterator var8 = tagNode.getChildren().iterator();

            while(var8.hasNext()) {
               Node node = (Node)var8.next();
               sb.append(node.render(interpreter));
            }

            return sb.toString();
         }
      }
   }

   public String getEndTagName() {
      return "endifchanged";
   }

   public String getName() {
      return "ifchanged";
   }
}
