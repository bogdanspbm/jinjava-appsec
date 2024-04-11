package com.hubspot.jinjava.el.ext;

import com.google.common.base.Throwables;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import jinjava.de.odysseus.el.tree.impl.Scanner;

public class ExtendedScanner extends Scanner {
   private static final Method ADD_KEY_TOKEN_METHOD;
   private static final Field TOKEN_FIELD;
   private static final Field POSITION_FIELD;

   protected ExtendedScanner(String input) {
      super(input);
   }

   public Scanner.Token next() throws Scanner.ScanException {
      if (this.getToken() != null) {
         this.incrPosition(this.getToken().getSize());
      }

      int length = this.getInput().length();
      if (this.isEval()) {
         while(this.getPosition() < length && this.isWhitespace(this.getInput().charAt(this.getPosition()))) {
            this.incrPosition(1);
         }
      }

      Scanner.Token token = null;
      if (this.getPosition() == length) {
         token = this.fixed(Scanner.Symbol.EOF);
      } else {
         token = this.nextToken();
      }

      this.setToken(token);
      return token;
   }

   protected boolean isWhitespace(char c) {
      return Character.isWhitespace(c) || Character.isSpaceChar(c);
   }

   protected static void addKeyToken(Scanner.Token token) {
      try {
         ADD_KEY_TOKEN_METHOD.invoke((Object)null, token);
      } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var2) {
         throw Throwables.propagate(var2);
      }
   }

   protected void setToken(Scanner.Token token) {
      try {
         TOKEN_FIELD.set(this, token);
      } catch (IllegalAccessException | IllegalArgumentException var3) {
         throw Throwables.propagate(var3);
      }
   }

   protected void incrPosition(int n) {
      try {
         POSITION_FIELD.set(this, this.getPosition() + n);
      } catch (IllegalAccessException | IllegalArgumentException var3) {
         throw Throwables.propagate(var3);
      }
   }

   protected Scanner.Token nextToken() throws Scanner.ScanException {
      if (this.isEval()) {
         if (this.getInput().charAt(this.getPosition()) == '}') {
            return (Scanner.Token)(this.getPosition() < this.getInput().length() - 1 ? ExtendedParser.LITERAL_DICT_END : this.fixed(Scanner.Symbol.END_EVAL));
         } else {
            return this.nextEval();
         }
      } else {
         if (this.getPosition() + 1 < this.getInput().length() && this.getInput().charAt(this.getPosition() + 1) == '{') {
            switch(this.getInput().charAt(this.getPosition())) {
            case '#':
               return this.fixed(Scanner.Symbol.START_EVAL_DEFERRED);
            case '$':
               return this.fixed(Scanner.Symbol.START_EVAL_DYNAMIC);
            }
         }

         return this.nextText();
      }
   }

   protected Scanner.Token nextEval() throws Scanner.ScanException {
      char c1 = this.getInput().charAt(this.getPosition());
      char c2 = this.getPosition() < this.getInput().length() - 1 ? this.getInput().charAt(this.getPosition() + 1) : 0;
      if (c1 == '/' && c2 == '/') {
         return ExtendedParser.TRUNC_DIV;
      } else if (c1 == '*' && c2 == '*') {
         return ExtendedParser.POWER_OF;
      } else if (c1 == '|' && c2 != '|') {
         return ExtendedParser.PIPE;
      } else if (c1 == '+' && Character.isDigit(c2)) {
         return AbsOperator.TOKEN;
      } else if (c1 == '=' && c2 != '=') {
         return NamedParameterOperator.TOKEN;
      } else if (c1 == '{') {
         return ExtendedParser.LITERAL_DICT_START;
      } else if (c1 == '}' && c2 != 0) {
         return ExtendedParser.LITERAL_DICT_END;
      } else {
         return (Scanner.Token)(c1 == '~' ? StringConcatOperator.TOKEN : super.nextEval());
      }
   }

   protected Scanner.Token nextString() throws Scanner.ScanException {
      this.builder.setLength(0);
      char quote = this.getInput().charAt(this.getPosition());
      int i = this.getPosition() + 1;
      int l = this.getInput().length();

      while(i < l) {
         char c = this.getInput().charAt(i++);
         if (c == '\\') {
            if (i == l) {
               throw new Scanner.ScanException(this.getPosition(), "unterminated string", quote + " or \\");
            }

            c = this.getInput().charAt(i++);
            switch(c) {
            case '"':
            case '\'':
            case '\\':
               this.builder.append(c);
               break;
            case 'b':
               this.builder.append('\b');
               break;
            case 'f':
               this.builder.append('\f');
               break;
            case 'n':
               this.builder.append('\n');
               break;
            case 'r':
               this.builder.append('\r');
               break;
            case 't':
               this.builder.append('\t');
               break;
            default:
               throw new Scanner.ScanException(this.getPosition(), "invalid escape sequence \\" + c, "\\" + quote + " or \\\\");
            }
         } else {
            if (c == quote) {
               return this.token(Scanner.Symbol.STRING, this.builder.toString(), i - this.getPosition());
            }

            this.builder.append(c);
         }
      }

      throw new Scanner.ScanException(this.getPosition(), "unterminated string", String.valueOf(quote));
   }

   static {
      try {
         ADD_KEY_TOKEN_METHOD = Scanner.class.getDeclaredMethod("addKeyToken", Scanner.Token.class);
         ADD_KEY_TOKEN_METHOD.setAccessible(true);
         TOKEN_FIELD = Scanner.class.getDeclaredField("token");
         TOKEN_FIELD.setAccessible(true);
         POSITION_FIELD = Scanner.class.getDeclaredField("position");
         POSITION_FIELD.setAccessible(true);
      } catch (SecurityException | NoSuchMethodException | NoSuchFieldException var1) {
         throw Throwables.propagate(var1);
      }
   }
}
