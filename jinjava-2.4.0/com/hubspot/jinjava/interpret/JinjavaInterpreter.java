package com.hubspot.jinjava.interpret;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.JinjavaConfig;
import com.hubspot.jinjava.el.ExpressionResolver;
import com.hubspot.jinjava.interpret.errorcategory.BasicTemplateErrorCategory;
import com.hubspot.jinjava.random.ConstantZeroRandomNumberGenerator;
import com.hubspot.jinjava.random.RandomNumberGeneratorStrategy;
import com.hubspot.jinjava.tree.Node;
import com.hubspot.jinjava.tree.TreeParser;
import com.hubspot.jinjava.tree.output.BlockPlaceholderOutputNode;
import com.hubspot.jinjava.tree.output.OutputList;
import com.hubspot.jinjava.tree.output.OutputNode;
import com.hubspot.jinjava.tree.output.RenderedOutputNode;
import com.hubspot.jinjava.util.Logging;
import com.hubspot.jinjava.util.Variable;
import com.hubspot.jinjava.util.WhitespaceUtils;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.StringUtils;

public class JinjavaInterpreter {
   private final Multimap<String, List<? extends Node>> blocks;
   private final LinkedList<Node> extendParentRoots;
   private Context context;
   private final JinjavaConfig config;
   private final ExpressionResolver expressionResolver;
   private final Jinjava application;
   private final Random random;
   private int lineNumber;
   private int scopeDepth;
   private final List<TemplateError> errors;
   private static final ThreadLocal<Stack<JinjavaInterpreter>> CURRENT_INTERPRETER = ThreadLocal.withInitial(Stack::new);

   public JinjavaInterpreter(Jinjava application, Context context, JinjavaConfig renderConfig) {
      this.blocks = ArrayListMultimap.create();
      this.extendParentRoots = new LinkedList();
      this.lineNumber = -1;
      this.scopeDepth = 1;
      this.errors = new LinkedList();
      this.context = context;
      this.config = renderConfig;
      this.application = application;
      if (this.config.getRandomNumberGeneratorStrategy() == RandomNumberGeneratorStrategy.THREAD_LOCAL) {
         this.random = ThreadLocalRandom.current();
      } else {
         if (this.config.getRandomNumberGeneratorStrategy() != RandomNumberGeneratorStrategy.CONSTANT_ZERO) {
            throw new IllegalStateException("No random number generator with strategy " + this.config.getRandomNumberGeneratorStrategy());
         }

         this.random = new ConstantZeroRandomNumberGenerator();
      }

      this.expressionResolver = new ExpressionResolver(this, application.getExpressionFactory());
   }

   public JinjavaInterpreter(JinjavaInterpreter orig) {
      this(orig.application, new Context(orig.context), orig.config);
      this.scopeDepth = orig.getScopeDepth() + 1;
   }

   /** @deprecated */
   @Deprecated
   public JinjavaConfig getConfiguration() {
      return this.config;
   }

   public void addExtendParentRoot(Node root) {
      this.extendParentRoots.add(root);
   }

   public void addBlock(String name, LinkedList<? extends Node> value) {
      this.blocks.put(name, value);
   }

   public JinjavaInterpreter.InterpreterScopeClosable enterScope() {
      return this.enterScope((Map)null);
   }

   public JinjavaInterpreter.InterpreterScopeClosable enterScope(Map<Context.Library, Set<String>> disabled) {
      this.context = new Context(this.context, (Map)null, disabled);
      ++this.scopeDepth;
      return new JinjavaInterpreter.InterpreterScopeClosable();
   }

   public void leaveScope() {
      Context parent = this.context.getParent();
      --this.scopeDepth;
      if (parent != null) {
         parent.addDependencies(this.context.getDependencies());
         this.context = parent;
      }

   }

   public Random getRandom() {
      return this.random;
   }

   public Node parse(String template) {
      return (new TreeParser(this, template)).buildTree();
   }

   public String renderFlat(String template) {
      int depth = this.context.getRenderDepth();

      String var3;
      try {
         if (depth <= this.config.getMaxRenderDepth()) {
            this.context.setRenderDepth(depth + 1);
            var3 = this.render(this.parse(template), false);
            return var3;
         }

         Logging.ENGINE_LOG.warn("Max render depth exceeded: {}", Integer.toString(depth));
         var3 = template;
      } finally {
         this.context.setRenderDepth(depth);
      }

      return var3;
   }

   public String render(String template) {
      Logging.ENGINE_LOG.debug(template);
      return this.render(this.parse(template), true);
   }

   public String render(Node root) {
      return this.render(root, true);
   }

   public String render(Node root, boolean processExtendRoots) {
      OutputList output = new OutputList(this.config.getMaxOutputSize());
      Iterator var4 = root.getChildren().iterator();

      OutputNode out;
      while(var4.hasNext()) {
         Node node = (Node)var4.next();
         this.lineNumber = node.getLineNumber();
         String renderStr = node.getMaster().getImage();
         if (this.context.doesRenderStackContain(renderStr)) {
            this.addError(new TemplateError(TemplateError.ErrorType.WARNING, TemplateError.ErrorReason.EXCEPTION, TemplateError.ErrorItem.TAG, "Rendering cycle detected: '" + renderStr + "'", (String)null, this.getLineNumber(), node.getStartPosition(), (Exception)null, BasicTemplateErrorCategory.IMPORT_CYCLE_DETECTED, ImmutableMap.of("string", renderStr)));
            output.addNode(new RenderedOutputNode(renderStr));
         } else {
            this.context.pushRenderStack(renderStr);
            out = node.render(this);
            this.context.popRenderStack();
            output.addNode(out);
         }
      }

      if (processExtendRoots) {
         while(!this.extendParentRoots.isEmpty()) {
            Node parentRoot = (Node)this.extendParentRoots.removeFirst();
            output = new OutputList(this.config.getMaxOutputSize());
            Iterator var9 = parentRoot.getChildren().iterator();

            while(var9.hasNext()) {
               Node node = (Node)var9.next();
               out = node.render(this);
               output.addNode(out);
            }

            this.context.getExtendPathStack().pop();
         }
      }

      this.resolveBlockStubs(output);
      return output.getValue();
   }

   private void resolveBlockStubs(OutputList output) {
      this.resolveBlockStubs(output, new Stack());
   }

   private void resolveBlockStubs(OutputList output, Stack<String> blockNames) {
      Iterator var3 = output.getBlocks().iterator();

      while(var3.hasNext()) {
         BlockPlaceholderOutputNode blockPlaceholder = (BlockPlaceholderOutputNode)var3.next();
         if (!blockNames.contains(blockPlaceholder.getBlockName())) {
            Collection<List<? extends Node>> blockChain = this.blocks.get(blockPlaceholder.getBlockName());
            List<? extends Node> block = (List)Iterables.getFirst(blockChain, (Object)null);
            if (block != null) {
               List<? extends Node> superBlock = (List)Iterables.get(blockChain, 1, (Object)null);
               this.context.setSuperBlock(superBlock);
               OutputList blockValueBuilder = new OutputList(this.config.getMaxOutputSize());
               Iterator var9 = block.iterator();

               while(var9.hasNext()) {
                  Node child = (Node)var9.next();
                  blockValueBuilder.addNode(child.render(this));
               }

               blockNames.push(blockPlaceholder.getBlockName());
               this.resolveBlockStubs(blockValueBuilder, blockNames);
               blockNames.pop();
               this.context.removeSuperBlock();
               blockPlaceholder.resolve(blockValueBuilder.getValue());
            }
         }

         if (!blockPlaceholder.isResolved()) {
            blockPlaceholder.resolve("");
         }
      }

   }

   public Object retraceVariable(String variable, int lineNumber, int startPosition) {
      if (StringUtils.isBlank(variable)) {
         return "";
      } else {
         Variable var = new Variable(this, variable);
         String varName = var.getName();
         Object obj = this.context.get(varName);
         if (obj != null) {
            obj = var.resolve(obj);
         } else if (this.getConfig().isFailOnUnknownTokens()) {
            throw new UnknownTokenException(variable, lineNumber, startPosition);
         }

         return obj;
      }
   }

   public Object retraceVariable(String variable, int lineNumber) {
      return this.retraceVariable(variable, lineNumber, -1);
   }

   public Object resolveObject(String variable, int lineNumber, int startPosition) {
      if (StringUtils.isBlank(variable)) {
         return "";
      } else if (WhitespaceUtils.isQuoted(variable)) {
         return WhitespaceUtils.unquote(variable);
      } else {
         Object val = this.retraceVariable(variable, lineNumber, startPosition);
         return val == null ? variable : val;
      }
   }

   public Object resolveObject(String variable, int lineNumber) {
      return this.resolveObject(variable, lineNumber, -1);
   }

   public String resolveString(String variable, int lineNumber, int startPosition) {
      return Objects.toString(this.resolveObject(variable, lineNumber, startPosition), "");
   }

   public String resolveString(String variable, int lineNumber) {
      return this.resolveString(variable, lineNumber, -1);
   }

   public Context getContext() {
      return this.context;
   }

   public String getResource(String resource) throws IOException {
      return this.application.getResourceLocator().getString(resource, this.config.getCharset(), this);
   }

   public JinjavaConfig getConfig() {
      return this.config;
   }

   public Object resolveELExpression(String expression, int lineNumber) {
      this.lineNumber = lineNumber;
      return this.expressionResolver.resolveExpression(expression);
   }

   public Object resolveProperty(Object object, String propertyName) {
      return this.resolveProperty(object, Collections.singletonList(propertyName));
   }

   public Object resolveProperty(Object object, List<String> propertyNames) {
      return this.expressionResolver.resolveProperty(object, propertyNames);
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public void addError(TemplateError templateError) {
      this.errors.add(templateError.withScopeDepth(this.scopeDepth));
   }

   public int getScopeDepth() {
      return this.scopeDepth;
   }

   public List<TemplateError> getErrors() {
      return this.errors;
   }

   public static JinjavaInterpreter getCurrent() {
      return ((Stack)CURRENT_INTERPRETER.get()).isEmpty() ? null : (JinjavaInterpreter)((Stack)CURRENT_INTERPRETER.get()).peek();
   }

   public static Optional<JinjavaInterpreter> getCurrentMaybe() {
      return Optional.ofNullable(getCurrent());
   }

   public static void pushCurrent(JinjavaInterpreter interpreter) {
      ((Stack)CURRENT_INTERPRETER.get()).push(interpreter);
   }

   public static void popCurrent() {
      if (!((Stack)CURRENT_INTERPRETER.get()).isEmpty()) {
         ((Stack)CURRENT_INTERPRETER.get()).pop();
      }

   }

   public class InterpreterScopeClosable implements AutoCloseable {
      public void close() {
         JinjavaInterpreter.this.leaveScope();
      }
   }
}
