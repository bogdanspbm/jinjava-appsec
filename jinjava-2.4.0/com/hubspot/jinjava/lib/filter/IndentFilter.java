package com.hubspot.jinjava.lib.filter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

@JinjavaDoc(
   value = "Uses whitespace to indent a string.",
   params = {@JinjavaParam(
   value = "s",
   desc = "The string to indent"
), @JinjavaParam(
   value = "width",
   type = "number",
   defaultValue = "4",
   desc = "Amount of whitespace to indent"
), @JinjavaParam(
   value = "indentfirst",
   type = "boolean",
   defaultValue = "False",
   desc = "If True, first line will be indented"
)},
   snippets = {@JinjavaSnippet(
   desc = "Since HubSpot's compiler automatically strips whitespace, this filter will only work in tags where whitespace is retained, such as a <pre>",
   code = "<pre>\n    {% set var = \"string to indent\" %}\n    {{ var|indent(2, true) }}\n</pre>"
)}
)
public class IndentFilter implements Filter {
   private static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
   private static final Joiner NEWLINE_JOINER = Joiner.on('\n');

   public String getName() {
      return "indent";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      int width = 4;
      if (args.length > 0) {
         width = NumberUtils.toInt(args[0], 4);
      }

      boolean indentFirst = false;
      if (args.length > 1) {
         indentFirst = BooleanUtils.toBoolean(args[1]);
      }

      List<String> indentedLines = new ArrayList();
      Iterator var7 = NEWLINE_SPLITTER.split(Objects.toString(var, "")).iterator();

      while(var7.hasNext()) {
         String line = (String)var7.next();
         int thisWidth = indentedLines.size() == 0 && !indentFirst ? 0 : width;
         indentedLines.add(StringUtils.repeat(' ', thisWidth) + line);
      }

      return NEWLINE_JOINER.join(indentedLines);
   }
}
