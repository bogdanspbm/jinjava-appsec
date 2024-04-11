package com.hubspot.jinjava.el.ext;

import jinjava.de.odysseus.el.misc.TypeConverter;
import jinjava.de.odysseus.el.tree.impl.Parser;
import jinjava.de.odysseus.el.tree.impl.Scanner;
import jinjava.de.odysseus.el.tree.impl.ast.AstBinary;
import jinjava.de.odysseus.el.tree.impl.ast.AstNode;

public class TruncDivOperator extends AstBinary.SimpleOperator {
   public static final Scanner.ExtensionToken TOKEN = new Scanner.ExtensionToken("//");
   public static final TruncDivOperator OP = new TruncDivOperator();
   public static final Parser.ExtensionHandler HANDLER;

   protected Object apply(TypeConverter converter, Object a, Object b) {
      boolean aInt = a instanceof Integer || a instanceof Long;
      boolean bInt = b instanceof Integer || b instanceof Long;
      boolean aNum = aInt || a instanceof Double || a instanceof Float;
      boolean bNum = bInt || b instanceof Double || b instanceof Float;
      if (aInt && bInt) {
         Long d = (Long)converter.convert(a, Long.class);
         Long e = (Long)converter.convert(b, Long.class);
         return Math.floorDiv(d, e);
      } else if (aNum && bNum) {
         Double d = (Double)converter.convert(a, Double.class);
         Double e = (Double)converter.convert(b, Double.class);
         return Math.floor(d / e);
      } else {
         throw new IllegalArgumentException(String.format("Unsupported operand type(s) for //: '%s' (%s) and '%s' (%s)", a, a == null ? "null" : a.getClass().getSimpleName(), b, b == null ? "null" : b.getClass().getSimpleName()));
      }
   }

   static {
      HANDLER = new Parser.ExtensionHandler(Parser.ExtensionPoint.MUL) {
         public AstNode createAstNode(AstNode... children) {
            return new AstBinary(children[0], children[1], TruncDivOperator.OP);
         }
      };
   }
}
