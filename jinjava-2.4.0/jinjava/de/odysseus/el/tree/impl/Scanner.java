package jinjava.de.odysseus.el.tree.impl;

import java.util.HashMap;
import jinjava.de.odysseus.el.misc.LocalMessages;

public class Scanner {
   private static final HashMap<String, Scanner.Token> KEYMAP = new HashMap();
   private static final HashMap<Scanner.Symbol, Scanner.Token> FIXMAP = new HashMap();
   private Scanner.Token token;
   private int position;
   private final String input;
   protected final StringBuilder builder = new StringBuilder();

   private static void addFixToken(Scanner.Token token) {
      FIXMAP.put(token.getSymbol(), token);
   }

   private static void addKeyToken(Scanner.Token token) {
      KEYMAP.put(token.getImage(), token);
   }

   protected Scanner(String input) {
      this.input = input;
   }

   public String getInput() {
      return this.input;
   }

   public Scanner.Token getToken() {
      return this.token;
   }

   public int getPosition() {
      return this.position;
   }

   protected boolean isDigit(char c) {
      return c >= '0' && c <= '9';
   }

   protected Scanner.Token keyword(String s) {
      return (Scanner.Token)KEYMAP.get(s);
   }

   protected Scanner.Token fixed(Scanner.Symbol symbol) {
      return (Scanner.Token)FIXMAP.get(symbol);
   }

   protected Scanner.Token token(Scanner.Symbol symbol, String value, int length) {
      return new Scanner.Token(symbol, value, length);
   }

   protected boolean isEval() {
      return this.token != null && this.token.getSymbol() != Scanner.Symbol.TEXT && this.token.getSymbol() != Scanner.Symbol.END_EVAL;
   }

   protected Scanner.Token nextText() throws Scanner.ScanException {
      this.builder.setLength(0);
      int i = this.position;
      int l = this.input.length();

      boolean escaped;
      for(escaped = false; i < l; ++i) {
         char c = this.input.charAt(i);
         switch(c) {
         case '#':
         case '$':
            if (i + 1 < l && this.input.charAt(i + 1) == '{') {
               if (!escaped) {
                  return this.token(Scanner.Symbol.TEXT, this.builder.toString(), i - this.position);
               }

               this.builder.append(c);
            } else {
               if (escaped) {
                  this.builder.append('\\');
               }

               this.builder.append(c);
            }

            escaped = false;
            break;
         case '\\':
            if (escaped) {
               this.builder.append('\\');
            } else {
               escaped = true;
            }
            break;
         default:
            if (escaped) {
               this.builder.append('\\');
            }

            this.builder.append(c);
            escaped = false;
         }
      }

      if (escaped) {
         this.builder.append('\\');
      }

      return this.token(Scanner.Symbol.TEXT, this.builder.toString(), i - this.position);
   }

   protected Scanner.Token nextString() throws Scanner.ScanException {
      this.builder.setLength(0);
      char quote = this.input.charAt(this.position);
      int i = this.position + 1;
      int l = this.input.length();

      while(i < l) {
         char c = this.input.charAt(i++);
         if (c == '\\') {
            if (i == l) {
               throw new Scanner.ScanException(this.position, "unterminated string", quote + " or \\");
            }

            c = this.input.charAt(i++);
            if (c != '\\' && c != quote) {
               throw new Scanner.ScanException(this.position, "invalid escape sequence \\" + c, "\\" + quote + " or \\\\");
            }

            this.builder.append(c);
         } else {
            if (c == quote) {
               return this.token(Scanner.Symbol.STRING, this.builder.toString(), i - this.position);
            }

            this.builder.append(c);
         }
      }

      throw new Scanner.ScanException(this.position, "unterminated string", String.valueOf(quote));
   }

   protected Scanner.Token nextNumber() throws Scanner.ScanException {
      int i = this.position;

      int l;
      for(l = this.input.length(); i < l && this.isDigit(this.input.charAt(i)); ++i) {
      }

      Scanner.Symbol symbol = Scanner.Symbol.INTEGER;
      if (i < l && this.input.charAt(i) == '.') {
         ++i;

         while(i < l && this.isDigit(this.input.charAt(i))) {
            ++i;
         }

         symbol = Scanner.Symbol.FLOAT;
      }

      if (i < l && (this.input.charAt(i) == 'e' || this.input.charAt(i) == 'E')) {
         int e = i++;
         if (i < l && (this.input.charAt(i) == '+' || this.input.charAt(i) == '-')) {
            ++i;
         }

         if (i < l && this.isDigit(this.input.charAt(i))) {
            ++i;

            while(i < l && this.isDigit(this.input.charAt(i))) {
               ++i;
            }

            symbol = Scanner.Symbol.FLOAT;
         } else {
            i = e;
         }
      }

      return this.token(symbol, this.input.substring(this.position, i), i - this.position);
   }

   protected Scanner.Token nextEval() throws Scanner.ScanException {
      char c1 = this.input.charAt(this.position);
      char c2 = this.position < this.input.length() - 1 ? this.input.charAt(this.position + 1) : 0;
      switch(c1) {
      case '!':
         if (c2 == '=') {
            return this.fixed(Scanner.Symbol.NE);
         }

         return this.fixed(Scanner.Symbol.NOT);
      case '"':
      case '\'':
         return this.nextString();
      case '#':
      case '$':
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
      case ';':
      case '@':
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
      case 'G':
      case 'H':
      case 'I':
      case 'J':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'S':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      case 'Z':
      case '\\':
      case '^':
      case '_':
      case '`':
      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f':
      case 'g':
      case 'h':
      case 'i':
      case 'j':
      case 'k':
      case 'l':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      case 's':
      case 't':
      case 'u':
      case 'v':
      case 'w':
      case 'x':
      case 'y':
      case 'z':
      case '{':
      default:
         break;
      case '%':
         return this.fixed(Scanner.Symbol.MOD);
      case '&':
         if (c2 == '&') {
            return this.fixed(Scanner.Symbol.AND);
         }
         break;
      case '(':
         return this.fixed(Scanner.Symbol.LPAREN);
      case ')':
         return this.fixed(Scanner.Symbol.RPAREN);
      case '*':
         return this.fixed(Scanner.Symbol.MUL);
      case '+':
         return this.fixed(Scanner.Symbol.PLUS);
      case ',':
         return this.fixed(Scanner.Symbol.COMMA);
      case '-':
         return this.fixed(Scanner.Symbol.MINUS);
      case '.':
         if (!this.isDigit(c2)) {
            return this.fixed(Scanner.Symbol.DOT);
         }
         break;
      case '/':
         return this.fixed(Scanner.Symbol.DIV);
      case ':':
         return this.fixed(Scanner.Symbol.COLON);
      case '<':
         if (c2 == '=') {
            return this.fixed(Scanner.Symbol.LE);
         }

         return this.fixed(Scanner.Symbol.LT);
      case '=':
         if (c2 == '=') {
            return this.fixed(Scanner.Symbol.EQ);
         }
         break;
      case '>':
         if (c2 == '=') {
            return this.fixed(Scanner.Symbol.GE);
         }

         return this.fixed(Scanner.Symbol.GT);
      case '?':
         return this.fixed(Scanner.Symbol.QUESTION);
      case '[':
         return this.fixed(Scanner.Symbol.LBRACK);
      case ']':
         return this.fixed(Scanner.Symbol.RBRACK);
      case '|':
         if (c2 == '|') {
            return this.fixed(Scanner.Symbol.OR);
         }
      }

      if (!this.isDigit(c1) && c1 != '.') {
         if (!Character.isJavaIdentifierStart(c1)) {
            throw new Scanner.ScanException(this.position, "invalid character '" + c1 + "'", "expression token");
         } else {
            int i = this.position + 1;

            for(int l = this.input.length(); i < l && Character.isJavaIdentifierPart(this.input.charAt(i)); ++i) {
            }

            String name = this.input.substring(this.position, i);
            Scanner.Token keyword = this.keyword(name);
            return keyword == null ? this.token(Scanner.Symbol.IDENTIFIER, name, i - this.position) : keyword;
         }
      } else {
         return this.nextNumber();
      }
   }

   protected Scanner.Token nextToken() throws Scanner.ScanException {
      if (this.isEval()) {
         return this.input.charAt(this.position) == '}' ? this.fixed(Scanner.Symbol.END_EVAL) : this.nextEval();
      } else {
         if (this.position + 1 < this.input.length() && this.input.charAt(this.position + 1) == '{') {
            switch(this.input.charAt(this.position)) {
            case '#':
               return this.fixed(Scanner.Symbol.START_EVAL_DEFERRED);
            case '$':
               return this.fixed(Scanner.Symbol.START_EVAL_DYNAMIC);
            }
         }

         return this.nextText();
      }
   }

   public Scanner.Token next() throws Scanner.ScanException {
      if (this.token != null) {
         this.position += this.token.getSize();
      }

      int length = this.input.length();
      if (this.isEval()) {
         while(this.position < length && Character.isWhitespace(this.input.charAt(this.position))) {
            ++this.position;
         }
      }

      return this.position == length ? (this.token = this.fixed(Scanner.Symbol.EOF)) : (this.token = this.nextToken());
   }

   static {
      addFixToken(new Scanner.Token(Scanner.Symbol.PLUS, "+"));
      addFixToken(new Scanner.Token(Scanner.Symbol.MINUS, "-"));
      addFixToken(new Scanner.Token(Scanner.Symbol.MUL, "*"));
      addFixToken(new Scanner.Token(Scanner.Symbol.DIV, "/"));
      addFixToken(new Scanner.Token(Scanner.Symbol.MOD, "%"));
      addFixToken(new Scanner.Token(Scanner.Symbol.LPAREN, "("));
      addFixToken(new Scanner.Token(Scanner.Symbol.RPAREN, ")"));
      addFixToken(new Scanner.Token(Scanner.Symbol.NOT, "!"));
      addFixToken(new Scanner.Token(Scanner.Symbol.AND, "&&"));
      addFixToken(new Scanner.Token(Scanner.Symbol.OR, "||"));
      addFixToken(new Scanner.Token(Scanner.Symbol.EQ, "=="));
      addFixToken(new Scanner.Token(Scanner.Symbol.NE, "!="));
      addFixToken(new Scanner.Token(Scanner.Symbol.LT, "<"));
      addFixToken(new Scanner.Token(Scanner.Symbol.LE, "<="));
      addFixToken(new Scanner.Token(Scanner.Symbol.GT, ">"));
      addFixToken(new Scanner.Token(Scanner.Symbol.GE, ">="));
      addFixToken(new Scanner.Token(Scanner.Symbol.QUESTION, "?"));
      addFixToken(new Scanner.Token(Scanner.Symbol.COLON, ":"));
      addFixToken(new Scanner.Token(Scanner.Symbol.COMMA, ","));
      addFixToken(new Scanner.Token(Scanner.Symbol.DOT, "."));
      addFixToken(new Scanner.Token(Scanner.Symbol.LBRACK, "["));
      addFixToken(new Scanner.Token(Scanner.Symbol.RBRACK, "]"));
      addFixToken(new Scanner.Token(Scanner.Symbol.START_EVAL_DEFERRED, "#{"));
      addFixToken(new Scanner.Token(Scanner.Symbol.START_EVAL_DYNAMIC, "${"));
      addFixToken(new Scanner.Token(Scanner.Symbol.END_EVAL, "}"));
      addFixToken(new Scanner.Token(Scanner.Symbol.EOF, (String)null, 0));
      addKeyToken(new Scanner.Token(Scanner.Symbol.NULL, "null"));
      addKeyToken(new Scanner.Token(Scanner.Symbol.TRUE, "true"));
      addKeyToken(new Scanner.Token(Scanner.Symbol.FALSE, "false"));
      addKeyToken(new Scanner.Token(Scanner.Symbol.EMPTY, "empty"));
      addKeyToken(new Scanner.Token(Scanner.Symbol.DIV, "div"));
      addKeyToken(new Scanner.Token(Scanner.Symbol.MOD, "mod"));
      addKeyToken(new Scanner.Token(Scanner.Symbol.NOT, "not"));
      addKeyToken(new Scanner.Token(Scanner.Symbol.AND, "and"));
      addKeyToken(new Scanner.Token(Scanner.Symbol.OR, "or"));
      addKeyToken(new Scanner.Token(Scanner.Symbol.LE, "le"));
      addKeyToken(new Scanner.Token(Scanner.Symbol.LT, "lt"));
      addKeyToken(new Scanner.Token(Scanner.Symbol.EQ, "eq"));
      addKeyToken(new Scanner.Token(Scanner.Symbol.NE, "ne"));
      addKeyToken(new Scanner.Token(Scanner.Symbol.GE, "ge"));
      addKeyToken(new Scanner.Token(Scanner.Symbol.GT, "gt"));
      addKeyToken(new Scanner.Token(Scanner.Symbol.INSTANCEOF, "instanceof"));
   }

   public static enum Symbol {
      EOF,
      PLUS("'+'"),
      MINUS("'-'"),
      MUL("'*'"),
      DIV("'/'|'div'"),
      MOD("'%'|'mod'"),
      LPAREN("'('"),
      RPAREN("')'"),
      IDENTIFIER,
      NOT("'!'|'not'"),
      AND("'&&'|'and'"),
      OR("'||'|'or'"),
      EMPTY("'empty'"),
      INSTANCEOF("'instanceof'"),
      INTEGER,
      FLOAT,
      TRUE("'true'"),
      FALSE("'false'"),
      STRING,
      NULL("'null'"),
      LE("'<='|'le'"),
      LT("'<'|'lt'"),
      GE("'>='|'ge'"),
      GT("'>'|'gt'"),
      EQ("'=='|'eq'"),
      NE("'!='|'ne'"),
      QUESTION("'?'"),
      COLON("':'"),
      TEXT,
      DOT("'.'"),
      LBRACK("'['"),
      RBRACK("']'"),
      COMMA("','"),
      START_EVAL_DEFERRED("'#{'"),
      START_EVAL_DYNAMIC("'${'"),
      END_EVAL("'}'"),
      EXTENSION;

      private final String string;

      private Symbol() {
         this((String)null);
      }

      private Symbol(String string) {
         this.string = string;
      }

      public String toString() {
         return this.string == null ? "<" + this.name() + ">" : this.string;
      }
   }

   public static class ExtensionToken extends Scanner.Token {
      public ExtensionToken(String image) {
         super(Scanner.Symbol.EXTENSION, image);
      }
   }

   public static class Token {
      private final Scanner.Symbol symbol;
      private final String image;
      private final int length;

      public Token(Scanner.Symbol symbol, String image) {
         this(symbol, image, image.length());
      }

      public Token(Scanner.Symbol symbol, String image, int length) {
         this.symbol = symbol;
         this.image = image;
         this.length = length;
      }

      public Scanner.Symbol getSymbol() {
         return this.symbol;
      }

      public String getImage() {
         return this.image;
      }

      public int getSize() {
         return this.length;
      }

      public String toString() {
         return this.symbol.toString();
      }
   }

   public static class ScanException extends Exception {
      final int position;
      final String encountered;
      final String expected;

      public ScanException(int position, String encountered, String expected) {
         super(LocalMessages.get("error.scan", position, encountered, expected));
         this.position = position;
         this.encountered = encountered;
         this.expected = expected;
      }
   }
}
