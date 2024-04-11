package com.hubspot.jinjava.el.ext;

import jinjava.de.odysseus.el.tree.impl.Parser;
import jinjava.de.odysseus.el.tree.impl.Scanner;
import jinjava.de.odysseus.el.tree.impl.ast.AstIdentifier;
import jinjava.de.odysseus.el.tree.impl.ast.AstNode;
import jinjava.javax.el.ELException;

public class NamedParameterOperator {
   public static final Scanner.ExtensionToken TOKEN = new Scanner.ExtensionToken("=");
   public static final Parser.ExtensionHandler HANDLER;

   static {
      HANDLER = new Parser.ExtensionHandler(Parser.ExtensionPoint.ADD) {
         public AstNode createAstNode(AstNode... children) {
            if (!(children[0] instanceof AstIdentifier)) {
               throw new ELException("Expected IDENTIFIER, found " + children[0].toString());
            } else {
               AstIdentifier name = (AstIdentifier)children[0];
               return new AstNamedParameter(name, children[1]);
            }
         }
      };
   }
}
