package jinjava.de.odysseus.el.tree.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import jinjava.de.odysseus.el.misc.LocalMessages;
import jinjava.de.odysseus.el.tree.ExpressionNode;
import jinjava.de.odysseus.el.tree.FunctionNode;
import jinjava.de.odysseus.el.tree.IdentifierNode;
import jinjava.de.odysseus.el.tree.Tree;
import jinjava.de.odysseus.el.tree.impl.ast.AstBinary;
import jinjava.de.odysseus.el.tree.impl.ast.AstBoolean;
import jinjava.de.odysseus.el.tree.impl.ast.AstBracket;
import jinjava.de.odysseus.el.tree.impl.ast.AstChoice;
import jinjava.de.odysseus.el.tree.impl.ast.AstComposite;
import jinjava.de.odysseus.el.tree.impl.ast.AstDot;
import jinjava.de.odysseus.el.tree.impl.ast.AstEval;
import jinjava.de.odysseus.el.tree.impl.ast.AstFunction;
import jinjava.de.odysseus.el.tree.impl.ast.AstIdentifier;
import jinjava.de.odysseus.el.tree.impl.ast.AstMethod;
import jinjava.de.odysseus.el.tree.impl.ast.AstNested;
import jinjava.de.odysseus.el.tree.impl.ast.AstNode;
import jinjava.de.odysseus.el.tree.impl.ast.AstNull;
import jinjava.de.odysseus.el.tree.impl.ast.AstNumber;
import jinjava.de.odysseus.el.tree.impl.ast.AstParameters;
import jinjava.de.odysseus.el.tree.impl.ast.AstProperty;
import jinjava.de.odysseus.el.tree.impl.ast.AstString;
import jinjava.de.odysseus.el.tree.impl.ast.AstText;
import jinjava.de.odysseus.el.tree.impl.ast.AstUnary;

public class Parser {
   private static final String EXPR_FIRST;
   protected final Builder context;
   protected final Scanner scanner;
   private List<IdentifierNode> identifiers = Collections.emptyList();
   private List<FunctionNode> functions = Collections.emptyList();
   private List<Parser.LookaheadToken> lookahead = Collections.emptyList();
   private Scanner.Token token;
   private int position;
   protected Map<Scanner.ExtensionToken, Parser.ExtensionHandler> extensions = Collections.emptyMap();

   public Parser(Builder context, String input) {
      this.context = context;
      this.scanner = this.createScanner(input);
   }

   protected Scanner createScanner(String expression) {
      return new Scanner(expression);
   }

   public void putExtensionHandler(Scanner.ExtensionToken token, Parser.ExtensionHandler extension) {
      if (this.extensions.isEmpty()) {
         this.extensions = new HashMap(16);
      }

      this.extensions.put(token, extension);
   }

   protected Parser.ExtensionHandler getExtensionHandler(Scanner.Token token) {
      return (Parser.ExtensionHandler)this.extensions.get(token);
   }

   protected Number parseInteger(String string) throws Parser.ParseException {
      try {
         return Long.valueOf(string);
      } catch (NumberFormatException var3) {
         this.fail(Scanner.Symbol.INTEGER);
         return null;
      }
   }

   protected Number parseFloat(String string) throws Parser.ParseException {
      try {
         return Double.valueOf(string);
      } catch (NumberFormatException var3) {
         this.fail(Scanner.Symbol.FLOAT);
         return null;
      }
   }

   protected AstBinary createAstBinary(AstNode left, AstNode right, AstBinary.Operator operator) {
      return new AstBinary(left, right, operator);
   }

   protected AstBracket createAstBracket(AstNode base, AstNode property, boolean lvalue, boolean strict) {
      return new AstBracket(base, property, lvalue, strict, this.context.isEnabled(Builder.Feature.IGNORE_RETURN_TYPE));
   }

   protected AstChoice createAstChoice(AstNode question, AstNode yes, AstNode no) {
      return new AstChoice(question, yes, no);
   }

   protected AstComposite createAstComposite(List<AstNode> nodes) {
      return new AstComposite(nodes);
   }

   protected AstDot createAstDot(AstNode base, String property, boolean lvalue) {
      return new AstDot(base, property, lvalue, this.context.isEnabled(Builder.Feature.IGNORE_RETURN_TYPE));
   }

   protected AstFunction createAstFunction(String name, int index, AstParameters params) {
      return new AstFunction(name, index, params, this.context.isEnabled(Builder.Feature.VARARGS));
   }

   protected AstIdentifier createAstIdentifier(String name, int index) {
      return new AstIdentifier(name, index, this.context.isEnabled(Builder.Feature.IGNORE_RETURN_TYPE));
   }

   protected AstMethod createAstMethod(AstProperty property, AstParameters params) {
      return new AstMethod(property, params);
   }

   protected AstUnary createAstUnary(AstNode child, AstUnary.Operator operator) {
      return new AstUnary(child, operator);
   }

   protected final List<FunctionNode> getFunctions() {
      return this.functions;
   }

   protected final List<IdentifierNode> getIdentifiers() {
      return this.identifiers;
   }

   protected final Scanner.Token getToken() {
      return this.token;
   }

   protected void fail(String expected) throws Parser.ParseException {
      throw new Parser.ParseException(this.position, "'" + this.token.getImage() + "'", expected);
   }

   protected void fail(Scanner.Symbol expected) throws Parser.ParseException {
      this.fail(expected.toString());
   }

   protected final Scanner.Token lookahead(int index) throws Scanner.ScanException, Parser.ParseException {
      if (this.lookahead.isEmpty()) {
         this.lookahead = new LinkedList();
      }

      while(index >= this.lookahead.size()) {
         this.lookahead.add(new Parser.LookaheadToken(this.scanner.next(), this.scanner.getPosition()));
      }

      return ((Parser.LookaheadToken)this.lookahead.get(index)).token;
   }

   protected final Scanner.Token consumeToken() throws Scanner.ScanException, Parser.ParseException {
      Scanner.Token result = this.token;
      if (this.lookahead.isEmpty()) {
         this.token = this.scanner.next();
         this.position = this.scanner.getPosition();
      } else {
         Parser.LookaheadToken next = (Parser.LookaheadToken)this.lookahead.remove(0);
         this.token = next.token;
         this.position = next.position;
      }

      return result;
   }

   protected final Scanner.Token consumeToken(Scanner.Symbol expected) throws Scanner.ScanException, Parser.ParseException {
      if (this.token.getSymbol() != expected) {
         this.fail(expected);
      }

      return this.consumeToken();
   }

   public Tree tree() throws Scanner.ScanException, Parser.ParseException {
      this.consumeToken();
      AstNode t = this.text();
      if (this.token.getSymbol() == Scanner.Symbol.EOF) {
         if (t == null) {
            t = new AstText("");
         }

         return new Tree((ExpressionNode)t, this.functions, this.identifiers, false);
      } else {
         AstEval e = this.eval();
         if (this.token.getSymbol() == Scanner.Symbol.EOF && t == null) {
            return new Tree(e, this.functions, this.identifiers, e.isDeferred());
         } else {
            ArrayList<AstNode> list = new ArrayList();
            if (t != null) {
               list.add(t);
            }

            list.add(e);
            AstNode t = this.text();
            if (t != null) {
               list.add(t);
            }

            while(this.token.getSymbol() != Scanner.Symbol.EOF) {
               if (e.isDeferred()) {
                  list.add(this.eval(true, true));
               } else {
                  list.add(this.eval(true, false));
               }

               t = this.text();
               if (t != null) {
                  list.add(t);
               }
            }

            return new Tree(this.createAstComposite(list), this.functions, this.identifiers, e.isDeferred());
         }
      }
   }

   protected AstNode text() throws Scanner.ScanException, Parser.ParseException {
      AstNode v = null;
      if (this.token.getSymbol() == Scanner.Symbol.TEXT) {
         v = new AstText(this.token.getImage());
         this.consumeToken();
      }

      return v;
   }

   protected AstEval eval() throws Scanner.ScanException, Parser.ParseException {
      AstEval e = this.eval(false, false);
      if (e == null) {
         e = this.eval(false, true);
         if (e == null) {
            this.fail(Scanner.Symbol.START_EVAL_DEFERRED + "|" + Scanner.Symbol.START_EVAL_DYNAMIC);
         }
      }

      return e;
   }

   protected AstEval eval(boolean required, boolean deferred) throws Scanner.ScanException, Parser.ParseException {
      AstEval v = null;
      Scanner.Symbol start_eval = deferred ? Scanner.Symbol.START_EVAL_DEFERRED : Scanner.Symbol.START_EVAL_DYNAMIC;
      if (this.token.getSymbol() == start_eval) {
         this.consumeToken();
         v = new AstEval(this.expr(true), deferred);
         this.consumeToken(Scanner.Symbol.END_EVAL);
      } else if (required) {
         this.fail(start_eval);
      }

      return v;
   }

   protected AstNode expr(boolean required) throws Scanner.ScanException, Parser.ParseException {
      AstNode v = this.or(required);
      if (v == null) {
         return null;
      } else {
         if (this.token.getSymbol() == Scanner.Symbol.QUESTION) {
            this.consumeToken();
            AstNode a = this.expr(true);
            this.consumeToken(Scanner.Symbol.COLON);
            AstNode b = this.expr(true);
            v = this.createAstChoice((AstNode)v, a, b);
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
            switch(this.token.getSymbol()) {
            case OR:
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.and(true), AstBinary.OR);
               break;
            case EXTENSION:
               if (this.getExtensionHandler(this.token).getExtensionPoint() == Parser.ExtensionPoint.OR) {
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

   protected AstNode and(boolean required) throws Scanner.ScanException, Parser.ParseException {
      AstNode v = this.eq(required);
      if (v == null) {
         return null;
      } else {
         while(true) {
            switch(this.token.getSymbol()) {
            case EXTENSION:
               if (this.getExtensionHandler(this.token).getExtensionPoint() != Parser.ExtensionPoint.AND) {
                  return (AstNode)v;
               }

               v = this.getExtensionHandler(this.consumeToken()).createAstNode((AstNode)v, this.eq(true));
               break;
            case AND:
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.eq(true), AstBinary.AND);
               break;
            default:
               return (AstNode)v;
            }
         }
      }
   }

   protected AstNode eq(boolean required) throws Scanner.ScanException, Parser.ParseException {
      AstNode v = this.cmp(required);
      if (v == null) {
         return null;
      } else {
         while(true) {
            switch(this.token.getSymbol()) {
            case EXTENSION:
               if (this.getExtensionHandler(this.token).getExtensionPoint() == Parser.ExtensionPoint.EQ) {
                  v = this.getExtensionHandler(this.consumeToken()).createAstNode((AstNode)v, this.cmp(true));
                  break;
               }

               return (AstNode)v;
            case AND:
            default:
               return (AstNode)v;
            case EQ:
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.cmp(true), AstBinary.EQ);
               break;
            case NE:
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.cmp(true), AstBinary.NE);
            }
         }
      }
   }

   protected AstNode cmp(boolean required) throws Scanner.ScanException, Parser.ParseException {
      AstNode v = this.add(required);
      if (v == null) {
         return null;
      } else {
         while(true) {
            switch(this.token.getSymbol()) {
            case EXTENSION:
               if (this.getExtensionHandler(this.token).getExtensionPoint() == Parser.ExtensionPoint.CMP) {
                  v = this.getExtensionHandler(this.consumeToken()).createAstNode((AstNode)v, this.add(true));
                  break;
               }

               return (AstNode)v;
            case AND:
            case EQ:
            case NE:
            default:
               return (AstNode)v;
            case LT:
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.add(true), AstBinary.LT);
               break;
            case LE:
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.add(true), AstBinary.LE);
               break;
            case GE:
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.add(true), AstBinary.GE);
               break;
            case GT:
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.add(true), AstBinary.GT);
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
            switch(this.token.getSymbol()) {
            case EXTENSION:
               if (this.getExtensionHandler(this.token).getExtensionPoint() != Parser.ExtensionPoint.ADD) {
                  return (AstNode)v;
               }

               v = this.getExtensionHandler(this.consumeToken()).createAstNode((AstNode)v, this.mul(true));
               break;
            case PLUS:
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.mul(true), AstBinary.ADD);
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

   protected AstNode mul(boolean required) throws Scanner.ScanException, Parser.ParseException {
      AstNode v = this.unary(required);
      if (v == null) {
         return null;
      } else {
         while(true) {
            switch(this.token.getSymbol()) {
            case EXTENSION:
               if (this.getExtensionHandler(this.token).getExtensionPoint() != Parser.ExtensionPoint.MUL) {
                  return (AstNode)v;
               }

               v = this.getExtensionHandler(this.consumeToken()).createAstNode((AstNode)v, this.unary(true));
               break;
            case MUL:
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.unary(true), AstBinary.MUL);
               break;
            case DIV:
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.unary(true), AstBinary.DIV);
               break;
            case MOD:
               this.consumeToken();
               v = this.createAstBinary((AstNode)v, this.unary(true), AstBinary.MOD);
               break;
            default:
               return (AstNode)v;
            }
         }
      }
   }

   protected AstNode unary(boolean required) throws Scanner.ScanException, Parser.ParseException {
      AstNode v = null;
      switch(this.token.getSymbol()) {
      case EXTENSION:
         if (this.getExtensionHandler(this.token).getExtensionPoint() == Parser.ExtensionPoint.UNARY) {
            v = this.getExtensionHandler(this.consumeToken()).createAstNode(this.unary(true));
            break;
         }
      default:
         v = this.value();
         break;
      case MINUS:
         this.consumeToken();
         v = this.createAstUnary(this.unary(true), AstUnary.NEG);
         break;
      case NOT:
         this.consumeToken();
         v = this.createAstUnary(this.unary(true), AstUnary.NOT);
         break;
      case EMPTY:
         this.consumeToken();
         v = this.createAstUnary(this.unary(true), AstUnary.EMPTY);
      }

      if (v == null && required) {
         this.fail(EXPR_FIRST);
      }

      return (AstNode)v;
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
         switch(this.token.getSymbol()) {
         case DOT:
            this.consumeToken();
            String name = this.consumeToken(Scanner.Symbol.IDENTIFIER).getImage();
            AstDot dot = this.createAstDot((AstNode)v, name, lvalue);
            if (this.token.getSymbol() == Scanner.Symbol.LPAREN && this.context.isEnabled(Builder.Feature.METHOD_INVOCATIONS)) {
               v = this.createAstMethod(dot, this.params());
               break;
            }

            v = dot;
            break;
         case LBRACK:
            this.consumeToken();
            AstNode property = this.expr(true);
            boolean strict = !this.context.isEnabled(Builder.Feature.NULL_PROPERTIES);
            this.consumeToken(Scanner.Symbol.RBRACK);
            AstBracket bracket = this.createAstBracket((AstNode)v, property, lvalue, strict);
            if (this.token.getSymbol() == Scanner.Symbol.LPAREN && this.context.isEnabled(Builder.Feature.METHOD_INVOCATIONS)) {
               v = this.createAstMethod(bracket, this.params());
               break;
            }

            v = bracket;
            break;
         default:
            return (AstNode)v;
         }
      }
   }

   protected AstNode nonliteral() throws Scanner.ScanException, Parser.ParseException {
      AstNode v = null;
      switch(this.token.getSymbol()) {
      case IDENTIFIER:
         String name = this.consumeToken().getImage();
         if (this.token.getSymbol() == Scanner.Symbol.COLON && this.lookahead(0).getSymbol() == Scanner.Symbol.IDENTIFIER && this.lookahead(1).getSymbol() == Scanner.Symbol.LPAREN) {
            this.consumeToken();
            name = name + ":" + this.token.getImage();
            this.consumeToken();
         }

         if (this.token.getSymbol() == Scanner.Symbol.LPAREN) {
            v = this.function(name, this.params());
         } else {
            v = this.identifier(name);
         }
         break;
      case LPAREN:
         this.consumeToken();
         AstNode v = this.expr(true);
         this.consumeToken(Scanner.Symbol.RPAREN);
         v = new AstNested(v);
      }

      return (AstNode)v;
   }

   protected AstParameters params() throws Scanner.ScanException, Parser.ParseException {
      this.consumeToken(Scanner.Symbol.LPAREN);
      List<AstNode> l = Collections.emptyList();
      AstNode v = this.expr(false);
      if (v != null) {
         l = new ArrayList();
         ((List)l).add(v);

         while(this.token.getSymbol() == Scanner.Symbol.COMMA) {
            this.consumeToken();
            ((List)l).add(this.expr(true));
         }
      }

      this.consumeToken(Scanner.Symbol.RPAREN);
      return new AstParameters((List)l);
   }

   protected AstNode literal() throws Scanner.ScanException, Parser.ParseException {
      AstNode v = null;
      switch(this.token.getSymbol()) {
      case EXTENSION:
         if (this.getExtensionHandler(this.token).getExtensionPoint() == Parser.ExtensionPoint.LITERAL) {
            v = this.getExtensionHandler(this.consumeToken()).createAstNode();
         }
      case AND:
      case EQ:
      case NE:
      case LT:
      case LE:
      case GE:
      case GT:
      case PLUS:
      case MINUS:
      case MUL:
      case DIV:
      case MOD:
      case NOT:
      case EMPTY:
      case DOT:
      case LBRACK:
      case IDENTIFIER:
      case LPAREN:
      default:
         break;
      case TRUE:
         v = new AstBoolean(true);
         this.consumeToken();
         break;
      case FALSE:
         v = new AstBoolean(false);
         this.consumeToken();
         break;
      case STRING:
         v = new AstString(this.token.getImage());
         this.consumeToken();
         break;
      case INTEGER:
         v = new AstNumber(this.parseInteger(this.token.getImage()));
         this.consumeToken();
         break;
      case FLOAT:
         v = new AstNumber(this.parseFloat(this.token.getImage()));
         this.consumeToken();
         break;
      case NULL:
         v = new AstNull();
         this.consumeToken();
      }

      return (AstNode)v;
   }

   protected final AstFunction function(String name, AstParameters params) {
      if (this.functions.isEmpty()) {
         this.functions = new ArrayList(4);
      }

      AstFunction function = this.createAstFunction(name, this.functions.size(), params);
      this.functions.add(function);
      return function;
   }

   protected final AstIdentifier identifier(String name) {
      if (this.identifiers.isEmpty()) {
         this.identifiers = new ArrayList(4);
      }

      AstIdentifier identifier = this.createAstIdentifier(name, this.identifiers.size());
      this.identifiers.add(identifier);
      return identifier;
   }

   static {
      EXPR_FIRST = Scanner.Symbol.IDENTIFIER + "|" + Scanner.Symbol.STRING + "|" + Scanner.Symbol.FLOAT + "|" + Scanner.Symbol.INTEGER + "|" + Scanner.Symbol.TRUE + "|" + Scanner.Symbol.FALSE + "|" + Scanner.Symbol.NULL + "|" + Scanner.Symbol.MINUS + "|" + Scanner.Symbol.NOT + "|" + Scanner.Symbol.EMPTY + "|" + Scanner.Symbol.LPAREN;
   }

   public abstract static class ExtensionHandler {
      private final Parser.ExtensionPoint point;

      public ExtensionHandler(Parser.ExtensionPoint point) {
         this.point = point;
      }

      public Parser.ExtensionPoint getExtensionPoint() {
         return this.point;
      }

      public abstract AstNode createAstNode(AstNode... var1);
   }

   public static enum ExtensionPoint {
      OR,
      AND,
      EQ,
      CMP,
      ADD,
      MUL,
      UNARY,
      LITERAL;
   }

   private static final class LookaheadToken {
      final Scanner.Token token;
      final int position;

      LookaheadToken(Scanner.Token token, int position) {
         this.token = token;
         this.position = position;
      }
   }

   public static class ParseException extends Exception {
      final int position;
      final String encountered;
      final String expected;

      public ParseException(int position, String encountered, String expected) {
         super(LocalMessages.get("error.parse", position, encountered, expected));
         this.position = position;
         this.encountered = encountered;
         this.expected = expected;
      }
   }
}
