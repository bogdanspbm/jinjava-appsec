package jinjava.de.odysseus.el.tree.impl;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.EnumSet;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.de.odysseus.el.tree.NodePrinter;
import jinjava.de.odysseus.el.tree.Tree;
import jinjava.de.odysseus.el.tree.TreeBuilder;
import jinjava.de.odysseus.el.tree.TreeBuilderException;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELException;
import jinjava.javax.el.ELResolver;
import jinjava.javax.el.FunctionMapper;
import jinjava.javax.el.ValueExpression;
import jinjava.javax.el.VariableMapper;

public class Builder implements TreeBuilder {
   private static final long serialVersionUID = 1L;
   protected final EnumSet<Builder.Feature> features;

   public Builder() {
      this.features = EnumSet.noneOf(Builder.Feature.class);
   }

   public Builder(Builder.Feature... features) {
      if (features != null && features.length != 0) {
         if (features.length == 1) {
            this.features = EnumSet.of(features[0]);
         } else {
            Builder.Feature[] rest = new Builder.Feature[features.length - 1];

            for(int i = 1; i < features.length; ++i) {
               rest[i - 1] = features[i];
            }

            this.features = EnumSet.of(features[0], rest);
         }
      } else {
         this.features = EnumSet.noneOf(Builder.Feature.class);
      }

   }

   public boolean isEnabled(Builder.Feature feature) {
      return this.features.contains(feature);
   }

   public Tree build(String expression) throws TreeBuilderException {
      try {
         return this.createParser(expression).tree();
      } catch (Scanner.ScanException var3) {
         throw new TreeBuilderException(expression, var3.position, var3.encountered, var3.expected, var3.getMessage());
      } catch (Parser.ParseException var4) {
         throw new TreeBuilderException(expression, var4.position, var4.encountered, var4.expected, var4.getMessage());
      }
   }

   protected Parser createParser(String expression) {
      return new Parser(this, expression);
   }

   public boolean equals(Object obj) {
      return obj != null && obj.getClass() == this.getClass() ? this.features.equals(((Builder)obj).features) : false;
   }

   public int hashCode() {
      return this.getClass().hashCode();
   }

   public static void main(String[] args) {
      if (args.length != 1) {
         System.err.println("usage: java " + Builder.class.getName() + " <expression string>");
         System.exit(1);
      }

      PrintWriter out = new PrintWriter(System.out);
      Tree tree = null;

      try {
         tree = (new Builder(new Builder.Feature[]{Builder.Feature.METHOD_INVOCATIONS})).build(args[0]);
      } catch (TreeBuilderException var6) {
         System.out.println(var6.getMessage());
         System.exit(0);
      }

      NodePrinter.dump(out, tree.getRoot());
      if (!tree.getFunctionNodes().iterator().hasNext() && !tree.getIdentifierNodes().iterator().hasNext()) {
         ELContext context = new ELContext() {
            public VariableMapper getVariableMapper() {
               return null;
            }

            public FunctionMapper getFunctionMapper() {
               return null;
            }

            public ELResolver getELResolver() {
               return null;
            }
         };
         out.print(">> ");

         try {
            out.println(tree.getRoot().getValue(new Bindings((Method[])null, (ValueExpression[])null), context, (Class)null));
         } catch (ELException var5) {
            out.println(var5.getMessage());
         }
      }

      out.flush();
   }

   public static enum Feature {
      METHOD_INVOCATIONS,
      NULL_PROPERTIES,
      VARARGS,
      IGNORE_RETURN_TYPE;
   }
}
