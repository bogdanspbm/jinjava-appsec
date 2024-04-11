package com.hubspot.jinjava.lib.tag;

import com.google.common.collect.Lists;
import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.interpret.TemplateSyntaxException;
import com.hubspot.jinjava.tree.Node;
import com.hubspot.jinjava.tree.TagNode;
import com.hubspot.jinjava.util.ForLoop;
import com.hubspot.jinjava.util.HelperStringTokenizer;
import com.hubspot.jinjava.util.LengthLimitingStringBuilder;
import com.hubspot.jinjava.util.ObjectIterator;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "Outputs the inner content for each item in the given iterable",
   params = {@JinjavaParam(
   value = "items_to_iterate",
   desc = "Specifies the name of a single item in the sequence or dict."
)},
   snippets = {@JinjavaSnippet(
   code = "{% for item in items %}\n    {{ item }}\n{% endfor %}"
), @JinjavaSnippet(
   desc = "Iterating over dictionary values",
   code = "{% for value in dictionary %}\n    {{ value }}\n{% endfor %}"
), @JinjavaSnippet(
   desc = "Iterating over dictionary entries",
   code = "{% for key, value in dictionary.items() %}\n    {{ key }}: {{ value }}\n{% endfor %}"
), @JinjavaSnippet(
   desc = "Standard blog listing loop",
   code = "{% for content in contents %}\n    Post content variables\n{% endfor %}"
)}
)
public class ForTag implements Tag {
   private static final long serialVersionUID = 6175143875754966497L;
   private static final String LOOP = "loop";
   private static final String TAGNAME = "for";
   private static final String ENDTAGNAME = "endfor";

   public String interpret(TagNode tagNode, JinjavaInterpreter interpreter) {
      String helpers = tagNode.getHelpers();
      String[] parts = helpers.split("\\s+in\\s+");
      if (2 == parts.length && !parts[1].contains("'") && !parts[1].contains("\"")) {
         helpers = parts[0] + " in " + parts[1].replace(" ", "");
      }

      List<String> helper = (new HelperStringTokenizer(helpers)).splitComma(true).allTokens();
      List<String> loopVars = Lists.newArrayList();

      int inPos;
      String loopExpr;
      for(inPos = 0; inPos < helper.size(); ++inPos) {
         loopExpr = (String)helper.get(inPos);
         if ("in".equals(loopExpr)) {
            break;
         }

         loopVars.add(loopExpr);
      }

      if (inPos >= helper.size()) {
         throw new TemplateSyntaxException(tagNode.getMaster().getImage(), "Tag 'for' expects valid 'in' clause, got: " + tagNode.getHelpers(), tagNode.getLineNumber(), tagNode.getStartPosition());
      } else {
         loopExpr = StringUtils.join(helper.subList(inPos + 1, helper.size()), ",");
         Object collection = interpreter.resolveELExpression(loopExpr, tagNode.getLineNumber());
         ForLoop loop = ObjectIterator.getLoop(collection);
         JinjavaInterpreter.InterpreterScopeClosable c = interpreter.enterScope();
         Throwable var12 = null;

         String var33;
         try {
            interpreter.getContext().put("loop", loop);
            LengthLimitingStringBuilder buff = new LengthLimitingStringBuilder(interpreter.getConfig().getMaxOutputSize());

            while(loop.hasNext()) {
               Object val = loop.next();
               Iterator var15;
               if (loopVars.size() == 1) {
                  interpreter.getContext().put(loopVars.get(0), val);
               } else {
                  var15 = loopVars.iterator();

                  label211:
                  while(true) {
                     while(true) {
                        if (!var15.hasNext()) {
                           break label211;
                        }

                        String loopVar = (String)var15.next();
                        if (Entry.class.isAssignableFrom(val.getClass())) {
                           Entry<String, Object> entry = (Entry)val;
                           Object entryVal = null;
                           if (loopVars.indexOf(loopVar) == 0) {
                              entryVal = entry.getKey();
                           } else if (loopVars.indexOf(loopVar) == 1) {
                              entryVal = entry.getValue();
                           }

                           interpreter.getContext().put(loopVar, entryVal);
                        } else {
                           try {
                              PropertyDescriptor[] valProps = Introspector.getBeanInfo(val.getClass()).getPropertyDescriptors();
                              PropertyDescriptor[] var18 = valProps;
                              int var19 = valProps.length;

                              for(int var20 = 0; var20 < var19; ++var20) {
                                 PropertyDescriptor valProp = var18[var20];
                                 if (loopVar.equals(valProp.getName())) {
                                    interpreter.getContext().put(loopVar, valProp.getReadMethod().invoke(val));
                                    break;
                                 }
                              }
                           } catch (Exception var30) {
                              throw new InterpretException(var30.getMessage(), var30, tagNode.getLineNumber(), tagNode.getStartPosition());
                           }
                        }
                     }
                  }
               }

               var15 = tagNode.getChildren().iterator();

               while(var15.hasNext()) {
                  Node node = (Node)var15.next();
                  buff.append((Object)node.render(interpreter));
               }
            }

            var33 = buff.toString();
         } catch (Throwable var31) {
            var12 = var31;
            throw var31;
         } finally {
            if (c != null) {
               if (var12 != null) {
                  try {
                     c.close();
                  } catch (Throwable var29) {
                     var12.addSuppressed(var29);
                  }
               } else {
                  c.close();
               }
            }

         }

         return var33;
      }
   }

   public String getEndTagName() {
      return "endfor";
   }

   public String getName() {
      return "for";
   }
}
