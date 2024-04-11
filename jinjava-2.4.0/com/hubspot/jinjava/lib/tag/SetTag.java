package com.hubspot.jinjava.lib.tag;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.interpret.TemplateSyntaxException;
import com.hubspot.jinjava.tree.TagNode;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Assigns the value or result of a statement to a variable",
   params = {@JinjavaParam(
   value = "var",
   type = "variable identifier",
   desc = "The name of the variable"
), @JinjavaParam(
   value = "expr",
   type = "expression",
   desc = "The value stored in the variable (string, number, boolean, or sequence"
)},
   snippets = {@JinjavaSnippet(
   desc = "Set a variable in with a set statement and print the variable in a expression",
   code = "{% set primaryColor = \"#F7761F\" %}\n{{ primaryColor }}\n"
), @JinjavaSnippet(
   desc = "You can combine multiple values or variables into a sequence variable",
   code = "{% set var_one = \"String 1\" %}\n{% set var_two = \"String 2\" %}\n{% set sequence = [var_one,  var_two] %}"
)}
)
public class SetTag implements Tag {
   private static final long serialVersionUID = -8558479410226781539L;
   private static final String TAGNAME = "set";

   public String getName() {
      return "set";
   }

   public String interpret(TagNode tagNode, JinjavaInterpreter interpreter) {
      if (!tagNode.getHelpers().contains("=")) {
         throw new TemplateSyntaxException(tagNode.getMaster().getImage(), "Tag 'set' expects an assignment expression with '=', but was: " + tagNode.getHelpers(), tagNode.getLineNumber(), tagNode.getStartPosition());
      } else {
         int eqPos = tagNode.getHelpers().indexOf(61);
         String var = tagNode.getHelpers().substring(0, eqPos).trim();
         String expr = tagNode.getHelpers().substring(eqPos + 1, tagNode.getHelpers().length());
         if (var.length() == 0) {
            throw new TemplateSyntaxException(tagNode.getMaster().getImage(), "Tag 'set' requires a var name to assign to", tagNode.getLineNumber(), tagNode.getStartPosition());
         } else if (StringUtils.isBlank(expr)) {
            throw new TemplateSyntaxException(tagNode.getMaster().getImage(), "Tag 'set' requires an expression to assign to a var", tagNode.getLineNumber(), tagNode.getStartPosition());
         } else {
            String[] varTokens = var.split(",");
            if (varTokens.length > 1) {
               List<Object> exprVals = (List)interpreter.resolveELExpression("[" + expr + "]", tagNode.getLineNumber());
               if (varTokens.length != exprVals.size()) {
                  throw new TemplateSyntaxException(tagNode.getMaster().getImage(), "Tag 'set' declares an uneven number of variables and assigned values", tagNode.getLineNumber(), tagNode.getStartPosition());
               }

               for(int i = 0; i < varTokens.length; ++i) {
                  String varItem = varTokens[i].trim();
                  Object val = exprVals.get(i);
                  interpreter.getContext().put(varItem, val);
               }
            } else {
               Object val = interpreter.resolveELExpression(expr, tagNode.getLineNumber());
               interpreter.getContext().put(var, val);
            }

            return "";
         }
      }
   }

   public String getEndTagName() {
      return null;
   }
}
