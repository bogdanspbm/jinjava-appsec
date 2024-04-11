package com.hubspot.jinjava.el.ext;

import jinjava.de.odysseus.el.misc.TypeConverter;
import jinjava.de.odysseus.el.tree.impl.Parser;
import jinjava.de.odysseus.el.tree.impl.Scanner;
import jinjava.de.odysseus.el.tree.impl.ast.AstBinary;
import jinjava.de.odysseus.el.tree.impl.ast.AstNode;

public class StringConcatOperator extends AstBinary.SimpleOperator {
   public static final Scanner.ExtensionToken TOKEN = new Scanner.ExtensionToken("~");
   public static final StringConcatOperator OP = new StringConcatOperator();
   public static final Parser.ExtensionHandler HANDLER;

   protected Object apply(TypeConverter converter, Object o1, Object o2) {
      String o1s = (String)converter.convert(o1, String.class);
      String o2s = (String)converter.convert(o2, String.class);
      return o1s + o2s;
   }

   static {
      HANDLER = new Parser.ExtensionHandler(Parser.ExtensionPoint.ADD) {
         public AstNode createAstNode(AstNode... children) {
            return new AstBinary(children[0], children[1], StringConcatOperator.OP);
         }
      };
   }
}
