package com.hubspot.jinjava.lib.tag;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.interpret.TemplateSyntaxException;
import com.hubspot.jinjava.tree.Node;
import com.hubspot.jinjava.tree.TagNode;
import com.hubspot.jinjava.util.LengthLimitingStringBuilder;
import com.hubspot.jinjava.util.ObjectTruthValue;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Outputs inner content if expression evaluates to true, otherwise evaluates any elif blocks, finally outputting content of any else block present",
   snippets = {@JinjavaSnippet(
   code = "{% if condition %}\nIf the condition is true print this to template.\n{% endif %}"
), @JinjavaSnippet(
   code = "{% if number <= 2 %}\nVarible named number is less than or equal to 2.\n{% elif number <= 4 %}\nVarible named number is less than or equal to 4.\n{% elif number <= 6 %}\nVarible named number is less than or equal to 6.\n{% else %}\nVarible named number is greater than 6.\n{% endif %}"
)}
)
public class IfTag implements Tag {
   private static final long serialVersionUID = -3784039314941268904L;
   private static final String TAGNAME = "if";
   private static final String ENDTAGNAME = "endif";

   public String interpret(TagNode tagNode, JinjavaInterpreter interpreter) {
      if (StringUtils.isBlank(tagNode.getHelpers())) {
         throw new TemplateSyntaxException(tagNode.getMaster().getImage(), "Tag 'if' expects expression", tagNode.getLineNumber(), tagNode.getStartPosition());
      } else {
         Iterator<Node> nodeIterator = tagNode.getChildren().iterator();

         TagNode nextIfElseTagNode;
         for(nextIfElseTagNode = tagNode; nextIfElseTagNode != null && !this.evaluateIfElseTagNode(nextIfElseTagNode, interpreter); nextIfElseTagNode = this.findNextIfElseTagNode(nodeIterator)) {
         }

         LengthLimitingStringBuilder sb = new LengthLimitingStringBuilder(interpreter.getConfig().getMaxOutputSize());
         if (nextIfElseTagNode != null) {
            while(nodeIterator.hasNext()) {
               Node n = (Node)nodeIterator.next();
               if (n.getName().equals("elif") || n.getName().equals("else")) {
                  break;
               }

               sb.append((Object)n.render(interpreter));
            }
         }

         return sb.toString();
      }
   }

   private TagNode findNextIfElseTagNode(Iterator<Node> nodeIterator) {
      while(true) {
         if (nodeIterator.hasNext()) {
            Node node = (Node)nodeIterator.next();
            if (!TagNode.class.isAssignableFrom(node.getClass())) {
               continue;
            }

            TagNode tag = (TagNode)node;
            if (!tag.getName().equals("elif") && !tag.getName().equals("else")) {
               continue;
            }

            return tag;
         }

         return null;
      }
   }

   protected boolean evaluateIfElseTagNode(TagNode tagNode, JinjavaInterpreter interpreter) {
      return tagNode.getName().equals("else") ? true : ObjectTruthValue.evaluate(interpreter.resolveELExpression(tagNode.getHelpers(), tagNode.getLineNumber()));
   }

   public String getEndTagName() {
      return "endif";
   }

   public String getName() {
      return "if";
   }
}
