package com.hubspot.jinjava.lib.tag;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.interpret.TemplateSyntaxException;
import com.hubspot.jinjava.lib.fn.MacroFunction;
import com.hubspot.jinjava.tree.Node;
import com.hubspot.jinjava.tree.TagNode;
import com.hubspot.jinjava.util.HelperStringTokenizer;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@JinjavaDoc(
   value = "Alternative to the import tag that lets you import and use specific macros from one template to another",
   params = {@JinjavaParam(
   value = "path",
   desc = "Design Manager path to file to import from"
), @JinjavaParam(
   value = "macro_name",
   desc = "Name of macro or comma separated macros to import (import macro_name)"
)},
   snippets = {@JinjavaSnippet(
   desc = "This example uses an html file containing two macros.",
   code = "{% macro header(tag, title_text) %}\n    <header> <{{ tag }}>{{ title_text }} </{{tag}}> </header>\n{% endmacro %}\n{% macro footer(tag, footer_text) %}\n    <footer> <{{ tag }}>{{ footer_text }} </{{tag}}> </footer>\n{% endmacro %}"
), @JinjavaSnippet(
   desc = "The macro html file is accessed from a different template, but only the footer macro is imported and executed",
   code = "{% from 'custom/page/web_page_basic/my_macros.html' import footer %}\n{{ footer('h2', 'My footer info') }}"
)}
)
public class FromTag implements Tag {
   private static final long serialVersionUID = 6152691434172265022L;

   public String getName() {
      return "from";
   }

   public String interpret(TagNode tagNode, JinjavaInterpreter interpreter) {
      List<String> helper = (new HelperStringTokenizer(tagNode.getHelpers())).splitComma(true).allTokens();
      if (helper.size() >= 3 && ((String)helper.get(1)).equals("import")) {
         String templateFile = interpreter.resolveString((String)helper.get(0), tagNode.getLineNumber(), tagNode.getStartPosition());
         Map<String, String> imports = new LinkedHashMap();

         String fromName;
         String importName;
         for(PeekingIterator args = Iterators.peekingIterator(helper.subList(2, helper.size()).iterator()); args.hasNext(); imports.put(fromName, importName)) {
            fromName = (String)args.next();
            importName = fromName;
            if (args.hasNext() && args.peek() != null && ((String)args.peek()).equals("as")) {
               args.next();
               importName = (String)args.next();
            }
         }

         try {
            fromName = interpreter.getResource(templateFile);
            Node node = interpreter.parse(fromName);
            JinjavaInterpreter child = new JinjavaInterpreter(interpreter);
            child.render(node);
            interpreter.getErrors().addAll(child.getErrors());
            Iterator var10 = imports.entrySet().iterator();

            while(var10.hasNext()) {
               Entry<String, String> importMapping = (Entry)var10.next();
               Object val = child.getContext().getGlobalMacro((String)importMapping.getKey());
               if (val != null) {
                  interpreter.getContext().addGlobalMacro((MacroFunction)val);
               } else {
                  Object val = child.getContext().get(importMapping.getKey());
                  if (val != null) {
                     interpreter.getContext().put(importMapping.getValue(), val);
                  }
               }
            }

            return "";
         } catch (IOException var13) {
            throw new InterpretException(var13.getMessage(), var13, tagNode.getLineNumber(), tagNode.getStartPosition());
         }
      } else {
         throw new TemplateSyntaxException(tagNode.getMaster().getImage(), "Tag 'from' expects import list: " + helper, tagNode.getLineNumber(), tagNode.getStartPosition());
      }
   }

   public String getEndTagName() {
      return null;
   }
}
