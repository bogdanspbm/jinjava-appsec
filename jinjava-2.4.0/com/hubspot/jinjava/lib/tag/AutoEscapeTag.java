package com.hubspot.jinjava.lib.tag;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.tree.Node;
import com.hubspot.jinjava.tree.TagNode;
import com.hubspot.jinjava.util.LengthLimitingStringBuilder;
import java.util.Iterator;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Autoescape the tag's contents",
   hidden = true,
   snippets = {@JinjavaSnippet(
   code = "{% autoescape %}\n<div>Code to escape</div>\n{% endautoescape %}"
)}
)
public class AutoEscapeTag implements Tag {
   private static final long serialVersionUID = 786006577642541285L;

   public String getName() {
      return "autoescape";
   }

   public String getEndTagName() {
      return "endautoescape";
   }

   public String interpret(TagNode tagNode, JinjavaInterpreter interpreter) {
      JinjavaInterpreter.InterpreterScopeClosable c = interpreter.enterScope();
      Throwable var4 = null;

      try {
         String boolFlagStr = StringUtils.trim(tagNode.getHelpers());
         boolean escapeFlag = BooleanUtils.toBoolean(StringUtils.isNotBlank(boolFlagStr) ? boolFlagStr : "true");
         interpreter.getContext().setAutoEscape(escapeFlag);
         LengthLimitingStringBuilder result = new LengthLimitingStringBuilder(interpreter.getConfig().getMaxOutputSize());
         Iterator var8 = tagNode.getChildren().iterator();

         while(var8.hasNext()) {
            Node child = (Node)var8.next();
            result.append((Object)child.render(interpreter));
         }

         String var19 = result.toString();
         return var19;
      } catch (Throwable var17) {
         var4 = var17;
         throw var17;
      } finally {
         if (c != null) {
            if (var4 != null) {
               try {
                  c.close();
               } catch (Throwable var16) {
                  var4.addSuppressed(var16);
               }
            } else {
               c.close();
            }
         }

      }
   }
}
