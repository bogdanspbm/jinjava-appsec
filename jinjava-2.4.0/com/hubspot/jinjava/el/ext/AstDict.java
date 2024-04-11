package com.hubspot.jinjava.el.ext;

import com.hubspot.jinjava.interpret.TemplateStateException;
import com.hubspot.jinjava.objects.collections.PyMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.de.odysseus.el.tree.impl.ast.AstIdentifier;
import jinjava.de.odysseus.el.tree.impl.ast.AstLiteral;
import jinjava.de.odysseus.el.tree.impl.ast.AstNode;
import jinjava.de.odysseus.el.tree.impl.ast.AstString;
import jinjava.javax.el.ELContext;

public class AstDict extends AstLiteral {
   private final Map<AstNode, AstNode> dict;

   public AstDict(Map<AstNode, AstNode> dict) {
      this.dict = dict;
   }

   public Object eval(Bindings bindings, ELContext context) {
      Map<String, Object> resolved = new LinkedHashMap();

      Entry entry;
      String key;
      for(Iterator var4 = this.dict.entrySet().iterator(); var4.hasNext(); resolved.put(key, ((AstNode)entry.getValue()).eval(bindings, context))) {
         entry = (Entry)var4.next();
         if (entry.getKey() instanceof AstString) {
            key = Objects.toString(((AstNode)entry.getKey()).eval(bindings, context));
         } else {
            if (!(entry.getKey() instanceof AstIdentifier)) {
               throw new TemplateStateException("Dict key must be a string or identifier, was: " + entry.getKey());
            }

            key = ((AstIdentifier)entry.getKey()).getName();
         }
      }

      return new PyMap(resolved);
   }

   public void appendStructure(StringBuilder builder, Bindings bindings) {
      throw new UnsupportedOperationException("appendStructure not implemented in " + this.getClass().getSimpleName());
   }

   public String toString() {
      StringBuilder s = new StringBuilder("{");
      Iterator var2 = this.dict.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<AstNode, AstNode> entry = (Entry)var2.next();
         s.append(Objects.toString(entry.getKey())).append(":").append(Objects.toString(entry.getValue()));
      }

      return s.append("}").toString();
   }
}
