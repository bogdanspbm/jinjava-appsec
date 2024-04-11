package com.hubspot.jinjava.lib.tag;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.Importable;
import com.hubspot.jinjava.tree.TagNode;
import com.hubspot.jinjava.tree.output.OutputNode;
import com.hubspot.jinjava.tree.output.RenderedOutputNode;
import java.io.Serializable;

public interface Tag extends Importable, Serializable {
   default OutputNode interpretOutput(TagNode tagNode, JinjavaInterpreter interpreter) {
      return new RenderedOutputNode(this.interpret(tagNode, interpreter));
   }

   String interpret(TagNode var1, JinjavaInterpreter var2);

   String getEndTagName();
}
