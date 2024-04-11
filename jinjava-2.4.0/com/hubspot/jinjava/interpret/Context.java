package com.hubspot.jinjava.interpret;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import com.hubspot.jinjava.lib.Importable;
import com.hubspot.jinjava.lib.exptest.ExpTest;
import com.hubspot.jinjava.lib.exptest.ExpTestLibrary;
import com.hubspot.jinjava.lib.filter.Filter;
import com.hubspot.jinjava.lib.filter.FilterLibrary;
import com.hubspot.jinjava.lib.fn.ELFunctionDefinition;
import com.hubspot.jinjava.lib.fn.FunctionLibrary;
import com.hubspot.jinjava.lib.fn.MacroFunction;
import com.hubspot.jinjava.lib.tag.Tag;
import com.hubspot.jinjava.lib.tag.TagLibrary;
import com.hubspot.jinjava.tree.Node;
import com.hubspot.jinjava.util.ScopeMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class Context extends ScopeMap<String, Object> {
   public static final String GLOBAL_MACROS_SCOPE_KEY = "__macros__";
   private final SetMultimap<String, String> dependencies;
   private Map<Context.Library, Set<String>> disabled;
   private final CallStack extendPathStack;
   private final CallStack importPathStack;
   private final CallStack includePathStack;
   private final CallStack macroStack;
   private final Set<String> resolvedExpressions;
   private final Set<String> resolvedValues;
   private final Set<String> resolvedFunctions;
   private final ExpTestLibrary expTestLibrary;
   private final FilterLibrary filterLibrary;
   private final FunctionLibrary functionLibrary;
   private final TagLibrary tagLibrary;
   private final Context parent;
   private int renderDepth;
   private Boolean autoEscape;
   private List<? extends Node> superBlock;
   private final Stack<String> renderStack;

   public Context() {
      this((Context)null, (Map)null, (Map)null);
   }

   public Context(Context parent) {
      this(parent, (Map)null, (Map)null);
   }

   public Context(Context parent, Map<String, ?> bindings) {
      this(parent, bindings, (Map)null);
   }

   public Context(Context parent, Map<String, ?> bindings, Map<Context.Library, Set<String>> disabled) {
      super(parent);
      this.dependencies = HashMultimap.create();
      this.resolvedExpressions = new HashSet();
      this.resolvedValues = new HashSet();
      this.resolvedFunctions = new HashSet();
      this.renderDepth = -1;
      this.renderStack = new Stack();
      this.disabled = (Map)disabled;
      if (bindings != null) {
         this.putAll(bindings);
      }

      this.parent = parent;
      this.extendPathStack = new CallStack(parent == null ? null : parent.getExtendPathStack(), ExtendsTagCycleException.class);
      this.importPathStack = new CallStack(parent == null ? null : parent.getImportPathStack(), ImportTagCycleException.class);
      this.includePathStack = new CallStack(parent == null ? null : parent.getIncludePathStack(), IncludeTagCycleException.class);
      this.macroStack = new CallStack(parent == null ? null : parent.getMacroStack(), MacroTagCycleException.class);
      if (disabled == null) {
         disabled = new HashMap();
      }

      this.expTestLibrary = new ExpTestLibrary(parent == null, (Set)((Map)disabled).get(Context.Library.EXP_TEST));
      this.filterLibrary = new FilterLibrary(parent == null, (Set)((Map)disabled).get(Context.Library.FILTER));
      this.tagLibrary = new TagLibrary(parent == null, (Set)((Map)disabled).get(Context.Library.TAG));
      this.functionLibrary = new FunctionLibrary(parent == null, (Set)((Map)disabled).get(Context.Library.FUNCTION));
   }

   public Context getParent() {
      return this.parent;
   }

   public Map<String, Object> getSessionBindings() {
      return this.getScope();
   }

   public Map<String, MacroFunction> getGlobalMacros() {
      Map<String, MacroFunction> macros = (Map)this.getScope().get("__macros__");
      if (macros == null) {
         macros = new HashMap();
         this.getScope().put("__macros__", macros);
      }

      return (Map)macros;
   }

   public void addGlobalMacro(MacroFunction macro) {
      this.getGlobalMacros().put(macro.getName(), macro);
   }

   public MacroFunction getGlobalMacro(String identifier) {
      MacroFunction fn = (MacroFunction)this.getGlobalMacros().get(identifier);
      if (fn == null && this.parent != null) {
         fn = this.parent.getGlobalMacro(identifier);
      }

      return fn;
   }

   public boolean isGlobalMacro(String identifier) {
      return this.getGlobalMacro(identifier) != null;
   }

   public boolean isAutoEscape() {
      if (this.autoEscape != null) {
         return this.autoEscape;
      } else {
         return this.parent != null ? this.parent.isAutoEscape() : false;
      }
   }

   public void setAutoEscape(Boolean autoEscape) {
      this.autoEscape = autoEscape;
   }

   public void addResolvedExpression(String expression) {
      this.resolvedExpressions.add(expression);
      if (this.getParent() != null) {
         this.getParent().addResolvedExpression(expression);
      }

   }

   public Set<String> getResolvedExpressions() {
      return ImmutableSet.copyOf(this.resolvedExpressions);
   }

   public boolean wasExpressionResolved(String expression) {
      return this.resolvedExpressions.contains(expression);
   }

   public void addResolvedValue(String value) {
      this.resolvedValues.add(value);
      if (this.getParent() != null) {
         this.getParent().addResolvedValue(value);
      }

   }

   public Set<String> getResolvedValues() {
      return ImmutableSet.copyOf(this.resolvedValues);
   }

   public boolean wasValueResolved(String value) {
      return this.resolvedValues.contains(value);
   }

   public Set<String> getResolvedFunctions() {
      return ImmutableSet.copyOf(this.resolvedFunctions);
   }

   public void addResolvedFunction(String function) {
      this.resolvedFunctions.add(function);
      if (this.getParent() != null) {
         this.getParent().addResolvedFunction(function);
      }

   }

   public List<? extends Node> getSuperBlock() {
      if (this.superBlock != null) {
         return this.superBlock;
      } else {
         return this.parent != null ? this.parent.getSuperBlock() : null;
      }
   }

   public void setSuperBlock(List<? extends Node> superBlock) {
      this.superBlock = superBlock;
   }

   public void removeSuperBlock() {
      this.superBlock = null;
   }

   public void addResolvedFrom(Context context) {
      context.getResolvedExpressions().forEach(this::addResolvedExpression);
      context.getResolvedFunctions().forEach(this::addResolvedFunction);
      context.getResolvedValues().forEach(this::addResolvedValue);
   }

   @SafeVarargs
   public final void registerClasses(Class<? extends Importable>... classes) {
      Class[] var2 = classes;
      int var3 = classes.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class<? extends Importable> c = var2[var4];
         if (ExpTest.class.isAssignableFrom(c)) {
            this.expTestLibrary.registerClasses(new Class[]{c});
         } else if (Filter.class.isAssignableFrom(c)) {
            this.filterLibrary.registerClasses(new Class[]{c});
         } else if (Tag.class.isAssignableFrom(c)) {
            this.tagLibrary.registerClasses(new Class[]{c});
         }
      }

   }

   public Collection<ExpTest> getAllExpTests() {
      List<ExpTest> expTests = new ArrayList(this.expTestLibrary.entries());
      if (this.parent != null) {
         expTests.addAll(this.parent.getAllExpTests());
      }

      return expTests;
   }

   public ExpTest getExpTest(String name) {
      ExpTest t = this.expTestLibrary.getExpTest(name);
      if (t != null) {
         return t;
      } else {
         return this.parent != null ? this.parent.getExpTest(name) : null;
      }
   }

   public void registerExpTest(ExpTest t) {
      this.expTestLibrary.addExpTest(t);
   }

   public Collection<Filter> getAllFilters() {
      List<Filter> filters = new ArrayList(this.filterLibrary.entries());
      if (this.parent != null) {
         filters.addAll(this.parent.getAllFilters());
      }

      return filters;
   }

   public Filter getFilter(String name) {
      Filter f = this.filterLibrary.getFilter(name);
      if (f != null) {
         return f;
      } else {
         return this.parent != null ? this.parent.getFilter(name) : null;
      }
   }

   public void registerFilter(Filter f) {
      this.filterLibrary.addFilter(f);
   }

   public boolean isFunctionDisabled(String name) {
      return this.disabled != null && ((Set)this.disabled.getOrDefault(Context.Library.FUNCTION, Collections.emptySet())).contains(name);
   }

   public ELFunctionDefinition getFunction(String name) {
      ELFunctionDefinition f = this.functionLibrary.getFunction(name);
      if (f != null) {
         return f;
      } else {
         return this.parent != null ? this.parent.getFunction(name) : null;
      }
   }

   public Collection<ELFunctionDefinition> getAllFunctions() {
      List<ELFunctionDefinition> fns = new ArrayList(this.functionLibrary.entries());
      if (this.parent != null) {
         fns.addAll(this.parent.getAllFunctions());
      }

      Set<String> disabledFunctions = this.disabled == null ? new HashSet() : (Set)this.disabled.getOrDefault(Context.Library.FUNCTION, new HashSet());
      return (Collection)fns.stream().filter((f) -> {
         return !disabledFunctions.contains(f.getName());
      }).collect(Collectors.toList());
   }

   public void registerFunction(ELFunctionDefinition f) {
      this.functionLibrary.addFunction(f);
   }

   public Collection<Tag> getAllTags() {
      List<Tag> tags = new ArrayList(this.tagLibrary.entries());
      if (this.parent != null) {
         tags.addAll(this.parent.getAllTags());
      }

      return tags;
   }

   public Tag getTag(String name) {
      Tag t = this.tagLibrary.getTag(name);
      if (t != null) {
         return t;
      } else {
         return this.parent != null ? this.parent.getTag(name) : null;
      }
   }

   public void registerTag(Tag t) {
      this.tagLibrary.addTag(t);
   }

   public CallStack getExtendPathStack() {
      return this.extendPathStack;
   }

   public CallStack getImportPathStack() {
      return this.importPathStack;
   }

   public CallStack getIncludePathStack() {
      return this.includePathStack;
   }

   public CallStack getMacroStack() {
      return this.macroStack;
   }

   public int getRenderDepth() {
      if (this.renderDepth != -1) {
         return this.renderDepth;
      } else {
         return this.parent != null ? this.parent.getRenderDepth() : 0;
      }
   }

   public void setRenderDepth(int renderDepth) {
      this.renderDepth = renderDepth;
   }

   public void pushRenderStack(String template) {
      this.renderStack.push(template);
   }

   public String popRenderStack() {
      return (String)this.renderStack.pop();
   }

   public boolean doesRenderStackContain(String template) {
      return this.renderStack.contains(template);
   }

   public void addDependency(String type, String identification) {
      this.dependencies.get(type).add(identification);
   }

   public void addDependencies(SetMultimap<String, String> dependencies) {
      this.dependencies.putAll(dependencies);
   }

   public SetMultimap<String, String> getDependencies() {
      return this.dependencies;
   }

   public static enum Library {
      EXP_TEST,
      FILTER,
      FUNCTION,
      TAG;
   }
}
