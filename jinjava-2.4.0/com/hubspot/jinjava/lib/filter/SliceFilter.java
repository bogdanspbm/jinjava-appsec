package com.hubspot.jinjava.lib.filter;

import com.google.common.collect.Iterators;
import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.util.ForLoop;
import com.hubspot.jinjava.util.ObjectIterator;
import org.apache.commons.lang3.math.NumberUtils;

@JinjavaDoc(
   value = "Slice an iterator and return a list of lists containing those items.",
   params = {@JinjavaParam(
   value = "value",
   type = "sequence",
   desc = "The sequence or dict that the filter is applied to"
), @JinjavaParam(
   value = "slices",
   type = "number",
   desc = "Specifies how many items will be sliced"
), @JinjavaParam(
   value = "fill_with",
   desc = "Used to fill missing values on the last iteration"
)},
   snippets = {@JinjavaSnippet(
   desc = "create a div containing three ul tags that represent columns",
   code = "{% set items = ['laptops', 'tablets', 'smartphones', 'smart watches', 'TVs'] %}\n<div class=\"columwrapper\">\n  {% for column in items|slice(3) %}\n    <ul class=\"column-{{ loop.index }}\">\n    {% for item in column %}\n      <li>{{ item }}</li>\n    {% endfor %}\n    </ul>\n  {% endfor %}\n</div>\n"
)}
)
public class SliceFilter implements Filter {
   public String getName() {
      return "slice";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      ForLoop loop = ObjectIterator.getLoop(var);
      if (args.length == 0) {
         throw new InterpretException(this.getName() + " requires number of slices argument", interpreter.getLineNumber());
      } else {
         int slices = NumberUtils.toInt(args[0], 3);
         return Iterators.paddedPartition(loop, slices);
      }
   }
}
