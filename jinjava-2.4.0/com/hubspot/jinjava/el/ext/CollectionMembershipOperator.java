package com.hubspot.jinjava.el.ext;

import java.util.Collection;
import java.util.Objects;
import jinjava.de.odysseus.el.misc.TypeConverter;
import jinjava.de.odysseus.el.tree.impl.Parser;
import jinjava.de.odysseus.el.tree.impl.Scanner;
import jinjava.de.odysseus.el.tree.impl.ast.AstBinary;
import jinjava.de.odysseus.el.tree.impl.ast.AstNode;
import org.apache.commons.lang3.StringUtils;

public class CollectionMembershipOperator extends AstBinary.SimpleOperator {
   public static final CollectionMembershipOperator OP = new CollectionMembershipOperator();
   public static final Scanner.ExtensionToken TOKEN = new Scanner.ExtensionToken("in");
   public static final Parser.ExtensionHandler HANDLER;

   protected Object apply(TypeConverter converter, Object o1, Object o2) {
      if (o2 == null) {
         return Boolean.FALSE;
      } else if (CharSequence.class.isAssignableFrom(o2.getClass())) {
         return StringUtils.contains((CharSequence)o2, Objects.toString(o1, ""));
      } else {
         return Collection.class.isAssignableFrom(o2.getClass()) ? ((Collection)o2).contains(o1) : Boolean.FALSE;
      }
   }

   static {
      HANDLER = new Parser.ExtensionHandler(Parser.ExtensionPoint.CMP) {
         public AstNode createAstNode(AstNode... children) {
            return new AstBinary(children[0], children[1], CollectionMembershipOperator.OP);
         }
      };
   }
}
