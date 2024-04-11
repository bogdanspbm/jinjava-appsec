package com.hubspot.jinjava.el.ext;

import jinjava.de.odysseus.el.misc.TypeConverter;
import jinjava.de.odysseus.el.tree.impl.Parser;
import jinjava.de.odysseus.el.tree.impl.Scanner;
import jinjava.de.odysseus.el.tree.impl.ast.AstNode;
import jinjava.de.odysseus.el.tree.impl.ast.AstUnary;

public class AbsOperator {
   public static final Scanner.ExtensionToken TOKEN = new Scanner.ExtensionToken("+");
   public static final Parser.ExtensionHandler HANDLER;

   static {
      HANDLER = new Parser.ExtensionHandler(Parser.ExtensionPoint.UNARY) {
         public AstNode createAstNode(AstNode... children) {
            return new AstUnary(children[0], new AstUnary.SimpleOperator() {
               protected Object apply(TypeConverter converter, Object o) {
                  if (o == null) {
                     return null;
                  } else if (o instanceof Float) {
                     return Math.abs((Float)o);
                  } else if (o instanceof Double) {
                     return Math.abs((Double)o);
                  } else if (o instanceof Integer) {
                     return Math.abs((Integer)o);
                  } else if (o instanceof Long) {
                     return Math.abs((Long)o);
                  } else {
                     throw new IllegalArgumentException("Unable to apply abs operator on object of type: " + o.getClass());
                  }
               }
            });
         }
      };
   }
}
