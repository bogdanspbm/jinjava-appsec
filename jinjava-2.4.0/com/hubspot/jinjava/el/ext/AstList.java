package com.hubspot.jinjava.el.ext;

import com.hubspot.jinjava.objects.collections.PyList;
import java.util.ArrayList;
import java.util.List;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.de.odysseus.el.tree.impl.ast.AstLiteral;
import jinjava.de.odysseus.el.tree.impl.ast.AstParameters;
import jinjava.javax.el.ELContext;
import org.apache.commons.lang3.StringUtils;

public class AstList extends AstLiteral {
   private final AstParameters elements;

   public AstList(AstParameters elements) {
      this.elements = elements;
   }

   public Object eval(Bindings bindings, ELContext context) {
      List<Object> list = new ArrayList();

      for(int i = 0; i < this.elements.getCardinality(); ++i) {
         list.add(this.elements.getChild(i).eval(bindings, context));
      }

      return new PyList(list);
   }

   public void appendStructure(StringBuilder builder, Bindings bindings) {
      throw new UnsupportedOperationException("appendStructure not implemented in " + this.getClass().getSimpleName());
   }

   protected String elementsToString() {
      List<String> els = new ArrayList(this.elements.getCardinality());

      for(int i = 0; i < this.elements.getCardinality(); ++i) {
         els.add(this.elements.getChild(i).toString());
      }

      return StringUtils.join(els, ", ");
   }

   public String toString() {
      return String.format("[%s]", this.elementsToString());
   }
}
