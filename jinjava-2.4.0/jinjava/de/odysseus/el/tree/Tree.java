package jinjava.de.odysseus.el.tree;

import java.lang.reflect.Method;
import java.util.List;
import jinjava.de.odysseus.el.misc.LocalMessages;
import jinjava.de.odysseus.el.misc.TypeConverter;
import jinjava.javax.el.ELException;
import jinjava.javax.el.FunctionMapper;
import jinjava.javax.el.ValueExpression;
import jinjava.javax.el.VariableMapper;

public class Tree {
   private final ExpressionNode root;
   private final List<FunctionNode> functions;
   private final List<IdentifierNode> identifiers;
   private final boolean deferred;

   public Tree(ExpressionNode root, List<FunctionNode> functions, List<IdentifierNode> identifiers, boolean deferred) {
      this.root = root;
      this.functions = functions;
      this.identifiers = identifiers;
      this.deferred = deferred;
   }

   public Iterable<FunctionNode> getFunctionNodes() {
      return this.functions;
   }

   public Iterable<IdentifierNode> getIdentifierNodes() {
      return this.identifiers;
   }

   public ExpressionNode getRoot() {
      return this.root;
   }

   public boolean isDeferred() {
      return this.deferred;
   }

   public String toString() {
      return this.getRoot().getStructuralId((Bindings)null);
   }

   public Bindings bind(FunctionMapper fnMapper, VariableMapper varMapper) {
      return this.bind(fnMapper, varMapper, (TypeConverter)null);
   }

   public Bindings bind(FunctionMapper fnMapper, VariableMapper varMapper, TypeConverter converter) {
      Method[] methods = null;
      if (!this.functions.isEmpty()) {
         if (fnMapper == null) {
            throw new ELException(LocalMessages.get("error.function.nomapper"));
         }

         methods = new Method[this.functions.size()];

         for(int i = 0; i < this.functions.size(); ++i) {
            FunctionNode node = (FunctionNode)this.functions.get(i);
            String image = node.getName();
            Method method = null;
            int colon = image.indexOf(58);
            if (colon < 0) {
               method = fnMapper.resolveFunction("", image);
            } else {
               method = fnMapper.resolveFunction(image.substring(0, colon), image.substring(colon + 1));
            }

            if (method == null) {
               throw new ELException(LocalMessages.get("error.function.notfound", image));
            }

            if (node.isVarArgs() && method.isVarArgs()) {
               if (method.getParameterTypes().length > node.getParamCount() + 1) {
                  throw new ELException(LocalMessages.get("error.function.params", image));
               }
            } else if (method.getParameterTypes().length != node.getParamCount()) {
               throw new ELException(LocalMessages.get("error.function.params", image));
            }

            methods[node.getIndex()] = method;
         }
      }

      ValueExpression[] expressions = null;
      if (this.identifiers.size() > 0) {
         expressions = new ValueExpression[this.identifiers.size()];

         for(int i = 0; i < this.identifiers.size(); ++i) {
            IdentifierNode node = (IdentifierNode)this.identifiers.get(i);
            ValueExpression expression = null;
            if (varMapper != null) {
               expression = varMapper.resolveVariable(node.getName());
            }

            expressions[node.getIndex()] = expression;
         }
      }

      return new Bindings(methods, expressions, converter);
   }
}
