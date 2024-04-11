package com.hubspot.jinjava.el.ext;

import com.hubspot.jinjava.objects.collections.PyList;
import java.util.Collections;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.de.odysseus.el.tree.impl.ast.AstParameters;
import jinjava.javax.el.ELContext;

public class AstTuple extends AstList {
   public AstTuple(AstParameters elements) {
      super(elements);
   }

   public Object eval(Bindings bindings, ELContext context) {
      PyList list = (PyList)super.eval(bindings, context);
      return new PyList(Collections.unmodifiableList(list.toList()));
   }

   public String toString() {
      return String.format("(%s)", this.elementsToString());
   }
}
