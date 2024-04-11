package com.hubspot.jinjava.el.ext;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jinjava.de.odysseus.el.tree.impl.Builder;
import jinjava.de.odysseus.el.tree.impl.Parser;
import jinjava.de.odysseus.el.tree.impl.Scanner;
import jinjava.de.odysseus.el.tree.impl.ast.AstBinary;
import jinjava.de.odysseus.el.tree.impl.ast.AstBracket;
import jinjava.de.odysseus.el.tree.impl.ast.AstDot;
import jinjava.de.odysseus.el.tree.impl.ast.AstFunction;
import jinjava.de.odysseus.el.tree.impl.ast.AstNested;
import jinjava.de.odysseus.el.tree.impl.ast.AstNode;
import jinjava.de.odysseus.el.tree.impl.ast.AstNull;
import jinjava.de.odysseus.el.tree.impl.ast.AstParameters;
import jinjava.de.odysseus.el.tree.impl.ast.AstProperty;
import jinjava.javax.el.ELException;

public class ExtendedParser extends Parser {
   public static final String INTERPRETER = "____int3rpr3t3r____";
   public static final String FILTER_PREFIX = "filter:";
   public static final String EXPTEST_PREFIX = "exptest:";
   static final Scanner.ExtensionToken PIPE = new Scanner.ExtensionToken("|");
   static final Scanner.ExtensionToken IS = new Scanner.ExtensionToken("is");
   static final Scanner.Token IF;
   static final Scanner.Token ELSE;
   static final Scanner.ExtensionToken LITERAL_DICT_START;
   static final Scanner.ExtensionToken LITERAL_DICT_END;
   static final Scanner.ExtensionToken TRUNC_DIV;
   static final Scanner.ExtensionToken POWER_OF;
   private static final Parser.ExtensionHandler NULL_EXT_HANDLER;

   public ExtendedParser(Builder context, String input) {
      super(context, input);
      this.putExtensionHandler(AbsOperator.TOKEN, AbsOperator.HANDLER);
      this.putExtensionHandler(NamedParameterOperator.TOKEN, NamedParameterOperator.HANDLER);
      this.putExtensionHandler(StringConcatOperator.TOKEN, StringConcatOperator.HANDLER);
      this.putExtensionHandler(TruncDivOperator.TOKEN, TruncDivOperator.HANDLER);
      this.putExtensionHandler(PowerOfOperator.TOKEN, PowerOfOperator.HANDLER);
      this.putExtensionHandler(CollectionMembershipOperator.TOKEN, CollectionMembershipOperator.HANDLER);
      this.putExtensionHandler(PIPE, new Parser.ExtensionHandler(Parser.ExtensionPoint.AND) {
         public AstNode createAstNode(AstNode... children) {
            throw new ELException("Illegal use of '|' operator");
         }
      });
      this.putExtensionHandler(LITERAL_DICT_START, NULL_EXT_HANDLER);
      this.putExtensionHandler(LITERAL_DICT_END, NULL_EXT_HANDLER);
   }

   protected AstNode interpreter() {
      return this.identifier("____int3rpr3t3r____");
   }

   protected AstNode expr(boolean required) throws Scanner.ScanException, Parser.ParseException {
      AstNode v = this.or(required);
      if (v == null) {
         return null;
      } else {
         if (this.getToken().getSymbol() == Scanner.Symbol.QUESTION) {
            AstNode cond;
            if (!this.getToken().getImage().equals("if")) {
               this.consumeToken();
               cond = this.expr(true);
               this.consumeToken(Scanner.Symbol.COLON);
               AstNode b = this.expr(true);
               v = this.createAstChoice((AstNode)v, cond, b);
            } else {
               this.consumeToken();
               cond = this.expr(true);
               AstNode elseNode = new AstNull();
               if (this.getToken().getImage().equals("else")) {
                  this.consumeToken();
                  elseNode = this.expr(true);
               }

               v = this.createAstChoice(cond, (AstNode)v, (AstNode)elseNode);
            }
         }

         return (AstNode)v;
      }
   }

   protected AstNode or(boolean required) throws Scanner.ScanException, Parser.ParseException {
      AstNode v = this.and(required);
      if (v == null) {
         return null;
      } else {
         while(true) {
            switch(this.getToken().getSymbol()) {
            case OR:
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.and(true), OrOperator.OP);
               break;
            case EXTENSION:
               if (this.getExtensionHandler(this.getToken()).getExtensionPoint() == Parser.ExtensionPoint.OR) {
                  v = this.getExtensionHandler(this.consumeToken()).createAstNode((AstNode)v, this.and(true));
                  break;
               }

               return (AstNode)v;
            default:
               return (AstNode)v;
            }
         }
      }
   }

   protected AstNode add(boolean required) throws Scanner.ScanException, Parser.ParseException {
      AstNode v = this.mul(required);
      if (v == null) {
         return null;
      } else {
         while(true) {
            switch(this.getToken().getSymbol()) {
            case EXTENSION:
               if (this.getExtensionHandler(this.getToken()).getExtensionPoint() != Parser.ExtensionPoint.ADD) {
                  return (AstNode)v;
               }

               v = this.getExtensionHandler(this.consumeToken()).createAstNode((AstNode)v, this.mul(true));
               break;
            case PLUS:
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.mul(true), AdditionOperator.OP);
               break;
            case MINUS:
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.mul(true), AstBinary.SUB);
               break;
            default:
               return (AstNode)v;
            }
         }
      }
   }

   protected AstParameters params() throws Scanner.ScanException, Parser.ParseException {
      return this.params(Scanner.Symbol.LPAREN, Scanner.Symbol.RPAREN);
   }

   protected AstParameters params(Scanner.Symbol left, Scanner.Symbol right) throws Scanner.ScanException, Parser.ParseException {
      this.consumeToken(left);
      List<AstNode> l = Collections.emptyList();
      AstNode v = this.expr(false);
      if (v != null) {
         l = new ArrayList();
         ((List)l).add(v);

         while(this.getToken().getSymbol() == Scanner.Symbol.COMMA) {
            this.consumeToken();
            ((List)l).add(this.expr(true));
         }
      }

      this.consumeToken(right);
      return new AstParameters((List)l);
   }

   protected AstDict dict() throws Scanner.ScanException, Parser.ParseException {
      this.consumeToken();
      Map<AstNode, AstNode> dict = new LinkedHashMap();
      AstNode k = this.expr(false);
      if (k != null) {
         this.consumeToken(Scanner.Symbol.COLON);
         AstNode v = this.expr(true);
         dict.put(k, v);

         while(this.getToken().getSymbol() == Scanner.Symbol.COMMA) {
            this.consumeToken();
            k = this.expr(false);
            if (k != null) {
               this.consumeToken(Scanner.Symbol.COLON);
               v = this.expr(true);
               dict.put(k, v);
            }
         }
      }

      if (!this.getToken().getImage().equals("}")) {
         this.fail("}");
      }

      this.consumeToken();
      return new AstDict(dict);
   }

   protected AstFunction createAstFunction(String name, int index, AstParameters params) {
      return new AstMacroFunction(name, index, params, this.context.isEnabled(Builder.Feature.VARARGS));
   }

   protected AstNode nonliteral() throws Scanner.ScanException, Parser.ParseException {
      AstNode v = null;
      switch(this.getToken().getSymbol()) {
      case IDENTIFIER:
         String name = this.consumeToken().getImage();
         if (this.getToken().getSymbol() == Scanner.Symbol.COLON && this.lookahead(0).getSymbol() == Scanner.Symbol.IDENTIFIER && this.lookahead(1).getSymbol() == Scanner.Symbol.LPAREN) {
            this.consumeToken();
            name = name + ":" + this.getToken().getImage();
            this.consumeToken();
         }

         if (this.getToken().getSymbol() == Scanner.Symbol.LPAREN) {
            v = this.function(name, this.params());
         } else {
            v = this.identifier(name);
         }
         break;
      case LPAREN:
         int var3 = 0;

         Scanner.Symbol s;
         do {
            s = this.lookahead(var3++).getSymbol();
            if (s == Scanner.Symbol.COMMA) {
               return new AstTuple(this.params());
            }
         } while(s != Scanner.Symbol.RPAREN && s != Scanner.Symbol.EOF);

         this.consumeToken();
         AstNode v = this.expr(true);
         this.consumeToken(Scanner.Symbol.RPAREN);
         v = new AstNested(v);
      }

      return (AstNode)v;
   }

   protected AstNode literal() throws Scanner.ScanException, Parser.ParseException {
      AstNode v = null;
      switch(this.getToken().getSymbol()) {
      case EXTENSION:
         if (this.getToken() == LITERAL_DICT_START) {
            v = this.dict();
         } else if (this.getToken() == LITERAL_DICT_END) {
            return null;
         }
         break;
      case LPAREN:
         v = new AstTuple(this.params());
         break;
      case LBRACK:
         v = new AstList(this.params(Scanner.Symbol.LBRACK, Scanner.Symbol.RBRACK));
      }

      return (AstNode)(v != null ? v : super.literal());
   }

   protected AstRangeBracket createAstRangeBracket(AstNode base, AstNode rangeStart, AstNode rangeMax, boolean lvalue, boolean strict) {
      return new AstRangeBracket(base, rangeStart, rangeMax, lvalue, strict, this.context.isEnabled(Builder.Feature.IGNORE_RETURN_TYPE));
   }

   protected AstNode value() throws Scanner.ScanException, Parser.ParseException {
      boolean lvalue = true;
      AstNode v = this.nonliteral();
      if (v == null) {
         v = this.literal();
         if (v == null) {
            return null;
         }

         lvalue = false;
      }

      while(true) {
         switch(this.getToken().getSymbol()) {
         case LBRACK:
            this.consumeToken();
            AstNode property = this.expr(false);
            boolean strict = !this.context.isEnabled(Builder.Feature.NULL_PROPERTIES);
            Scanner.Token nextToken = this.consumeToken();
            if (nextToken.getSymbol() == Scanner.Symbol.COLON) {
               AstNode rangeMax = this.expr(false);
               this.consumeToken(Scanner.Symbol.RBRACK);
               v = this.createAstRangeBracket((AstNode)v, property, rangeMax, lvalue, strict);
            } else {
               if (nextToken.getSymbol() == Scanner.Symbol.RBRACK) {
                  AstBracket bracket = this.createAstBracket((AstNode)v, property, lvalue, strict);
                  if (this.getToken().getSymbol() == Scanner.Symbol.LPAREN && this.context.isEnabled(Builder.Feature.METHOD_INVOCATIONS)) {
                     v = this.createAstMethod(bracket, this.params());
                     continue;
                  }

                  v = bracket;
                  continue;
               }

               this.fail(Scanner.Symbol.RBRACK);
            }
            break;
         case DOT:
            this.consumeToken();
            String name = this.consumeToken(Scanner.Symbol.IDENTIFIER).getImage();
            AstDot dot = this.createAstDot((AstNode)v, name, lvalue);
            if (this.getToken().getSymbol() == Scanner.Symbol.LPAREN && this.context.isEnabled(Builder.Feature.METHOD_INVOCATIONS)) {
               v = this.createAstMethod(dot, this.params());
               break;
            }

            v = dot;
            break;
         default:
            ArrayList filterParams;
            String exptestName;
            if ("|".equals(this.getToken().getImage()) && this.lookahead(0).getSymbol() == Scanner.Symbol.IDENTIFIER) {
               do {
                  this.consumeToken();
                  exptestName = this.consumeToken().getImage();
                  filterParams = Lists.newArrayList(new AstNode[]{(AstNode)v, this.interpreter()});
                  if (this.getToken().getSymbol() == Scanner.Symbol.LPAREN) {
                     AstParameters astParameters = this.params();

                     for(int i = 0; i < astParameters.getCardinality(); ++i) {
                        filterParams.add(astParameters.getChild(i));
                     }
                  }

                  AstProperty filterProperty = this.createAstDot(this.identifier("filter:" + exptestName), "filter", true);
                  v = this.createAstMethod(filterProperty, new AstParameters(filterParams));
               } while("|".equals(this.getToken().getImage()));
            } else if ("is".equals(this.getToken().getImage()) && this.lookahead(0).getSymbol() == Scanner.Symbol.IDENTIFIER) {
               this.consumeToken();
               exptestName = this.consumeToken().getImage();
               filterParams = Lists.newArrayList(new AstNode[]{(AstNode)v, this.interpreter()});
               AstNode arg = this.expr(false);
               if (arg != null) {
                  filterParams.add(arg);
               }

               AstProperty exptestProperty = this.createAstDot(this.identifier("exptest:" + exptestName), "evaluate", true);
               v = this.createAstMethod(exptestProperty, new AstParameters(filterParams));
            } else if ("//".equals(this.getToken().getImage()) && this.lookahead(0).getSymbol() == Scanner.Symbol.IDENTIFIER) {
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.mul(true), TruncDivOperator.OP);
            } else if ("**".equals(this.getToken().getImage()) && this.lookahead(0).getSymbol() == Scanner.Symbol.IDENTIFIER) {
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.mul(true), PowerOfOperator.OP);
            }

            return (AstNode)v;
         }
      }
   }

   protected Scanner createScanner(String expression) {
      return new ExtendedScanner(expression);
   }

   static {
      IF = new Scanner.Token(Scanner.Symbol.QUESTION, "if");
      ELSE = new Scanner.Token(Scanner.Symbol.COLON, "else");
      LITERAL_DICT_START = new Scanner.ExtensionToken("{");
      LITERAL_DICT_END = new Scanner.ExtensionToken("}");
      TRUNC_DIV = new Scanner.ExtensionToken("//");
      POWER_OF = new Scanner.ExtensionToken("**");
      ExtendedScanner.addKeyToken(IF);
      ExtendedScanner.addKeyToken(ELSE);
      ExtendedScanner.addKeyToken(TruncDivOperator.TOKEN);
      ExtendedScanner.addKeyToken(PowerOfOperator.TOKEN);
      ExtendedScanner.addKeyToken(CollectionMembershipOperator.TOKEN);
      NULL_EXT_HANDLER = new Parser.ExtensionHandler((Parser.ExtensionPoint)null) {
         public AstNode createAstNode(AstNode... children) {
            return null;
         }
      };
   }
}
