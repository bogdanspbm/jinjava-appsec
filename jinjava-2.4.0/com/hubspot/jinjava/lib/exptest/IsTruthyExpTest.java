package com.hubspot.jinjava.lib.exptest;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.util.ObjectTruthValue;

@JinjavaDoc(
   value = "Return true if object is 'truthy'",
   snippets = {@JinjavaSnippet(
   code = "{% if variable is truthy %}\n      <!--code to render a  boolean variable is True-->\n{% endif %}"
)}
)
public class IsTruthyExpTest implements ExpTest {
   public String getName() {
      return "truthy";
   }

   public boolean evaluate(Object var, JinjavaInterpreter interpreter, Object... args) {
      return ObjectTruthValue.evaluate(var);
   }
}
