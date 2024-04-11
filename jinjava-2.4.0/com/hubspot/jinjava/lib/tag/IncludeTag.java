package com.hubspot.jinjava.lib.tag;

import com.google.common.collect.ImmutableMap;
import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.IncludeTagCycleException;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.interpret.TemplateError;
import com.hubspot.jinjava.interpret.TemplateSyntaxException;
import com.hubspot.jinjava.interpret.errorcategory.BasicTemplateErrorCategory;
import com.hubspot.jinjava.tree.Node;
import com.hubspot.jinjava.tree.TagNode;
import com.hubspot.jinjava.util.HelperStringTokenizer;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

@JinjavaDoc(
   value = "includes multiple files in one template or stylesheet",
   params = {@JinjavaParam(
   value = "path",
   desc = "Design Manager path to the file that you would like to include"
)},
   snippets = {@JinjavaSnippet(
   code = "{% include \"custom/page/web_page_basic/my_footer.html\" %}"
), @JinjavaSnippet(
   code = "{% include \"generated_global_groups/2781996615.html\" %}"
), @JinjavaSnippet(
   code = "{% include \"hubspot/styles/patches/recommended.css\" %}"
)}
)
public class IncludeTag implements Tag {
   private static final long serialVersionUID = -8391753639874726854L;

   public String interpret(TagNode tagNode, JinjavaInterpreter interpreter) {
      HelperStringTokenizer helper = new HelperStringTokenizer(tagNode.getHelpers());
      if (!helper.hasNext()) {
         throw new TemplateSyntaxException(tagNode.getMaster().getImage(), "Tag 'include' expects template path", tagNode.getLineNumber(), tagNode.getStartPosition());
      } else {
         String path = StringUtils.trimToEmpty((String)helper.next());
         String templateFile = interpreter.resolveString(path, tagNode.getLineNumber(), tagNode.getStartPosition());

         try {
            interpreter.getContext().getIncludePathStack().push(templateFile, tagNode.getLineNumber(), tagNode.getStartPosition());
         } catch (IncludeTagCycleException var17) {
            interpreter.addError(new TemplateError(TemplateError.ErrorType.WARNING, TemplateError.ErrorReason.EXCEPTION, TemplateError.ErrorItem.TAG, "Include cycle detected for path: '" + templateFile + "'", (String)null, tagNode.getLineNumber(), tagNode.getStartPosition(), var17, BasicTemplateErrorCategory.INCLUDE_CYCLE_DETECTED, ImmutableMap.of("path", templateFile)));
            return "";
         }

         String var10;
         try {
            String template = interpreter.getResource(templateFile);
            Node node = interpreter.parse(template);
            interpreter.getContext().addDependency("coded_files", templateFile);
            JinjavaInterpreter child = new JinjavaInterpreter(interpreter);
            String result = child.render(node);
            interpreter.getErrors().addAll(child.getErrors());
            var10 = result;
         } catch (IOException var15) {
            throw new InterpretException(var15.getMessage(), var15, tagNode.getLineNumber(), tagNode.getStartPosition());
         } finally {
            interpreter.getContext().getIncludePathStack().pop();
         }

         return var10;
      }
   }

   public String getEndTagName() {
      return null;
   }

   public String getName() {
      return "include";
   }
}
