package com.hubspot.jinjava;

import com.hubspot.jinjava.doc.JinjavaDoc;
import com.hubspot.jinjava.doc.JinjavaDocFactory;
import com.hubspot.jinjava.el.ExtendedSyntaxBuilder;
import com.hubspot.jinjava.el.TruthyTypeConverter;
import com.hubspot.jinjava.interpret.Context;
import com.hubspot.jinjava.interpret.FatalTemplateErrorsException;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.interpret.RenderResult;
import com.hubspot.jinjava.interpret.TemplateError;
import com.hubspot.jinjava.loader.ClasspathResourceLocator;
import com.hubspot.jinjava.loader.ResourceLocator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import jinjava.de.odysseus.el.ExpressionFactoryImpl;
import jinjava.de.odysseus.el.misc.TypeConverter;
import jinjava.de.odysseus.el.tree.TreeBuilder;
import jinjava.javax.el.ExpressionFactory;

public class Jinjava {
   private ExpressionFactory expressionFactory;
   private ResourceLocator resourceLocator;
   private Context globalContext;
   private JinjavaConfig globalConfig;

   public Jinjava() {
      this(new JinjavaConfig());
   }

   public Jinjava(JinjavaConfig globalConfig) {
      this.globalConfig = globalConfig;
      this.globalContext = new Context();
      Properties expConfig = new Properties();
      expConfig.setProperty(TreeBuilder.class.getName(), ExtendedSyntaxBuilder.class.getName());
      TypeConverter converter = new TruthyTypeConverter();
      this.expressionFactory = new ExpressionFactoryImpl(expConfig, converter);
      this.resourceLocator = new ClasspathResourceLocator();
   }

   public void setResourceLocator(ResourceLocator resourceLocator) {
      this.resourceLocator = resourceLocator;
   }

   public ExpressionFactory getExpressionFactory() {
      return this.expressionFactory;
   }

   public JinjavaConfig getGlobalConfig() {
      return this.globalConfig;
   }

   public Context getGlobalContext() {
      return this.globalContext;
   }

   public ResourceLocator getResourceLocator() {
      return this.resourceLocator;
   }

   public JinjavaDoc getJinjavaDoc() {
      return (new JinjavaDocFactory(this)).get();
   }

   public String render(String template, Map<String, ?> bindings) {
      RenderResult result = this.renderForResult(template, bindings);
      List<TemplateError> fatalErrors = (List)result.getErrors().stream().filter((error) -> {
         return error.getSeverity() == TemplateError.ErrorType.FATAL;
      }).collect(Collectors.toList());
      if (!fatalErrors.isEmpty()) {
         throw new FatalTemplateErrorsException(template, fatalErrors);
      } else {
         return result.getOutput();
      }
   }

   public RenderResult renderForResult(String template, Map<String, ?> bindings) {
      return this.renderForResult(template, bindings, this.globalConfig);
   }

   public RenderResult renderForResult(String template, Map<String, ?> bindings, JinjavaConfig renderConfig) {
      Context context = new Context(this.globalContext, bindings, renderConfig.getDisabled());
      JinjavaInterpreter parentInterpreter = JinjavaInterpreter.getCurrent();
      if (parentInterpreter != null) {
         renderConfig = parentInterpreter.getConfig();
      }

      JinjavaInterpreter interpreter = new JinjavaInterpreter(this, context, renderConfig);
      JinjavaInterpreter.pushCurrent(interpreter);

      RenderResult var8;
      try {
         String result = interpreter.render(template);
         var8 = new RenderResult(result, interpreter.getContext(), interpreter.getErrors());
         return var8;
      } catch (InterpretException var13) {
         var8 = new RenderResult(TemplateError.fromSyntaxError(var13), interpreter.getContext(), interpreter.getErrors());
         return var8;
      } catch (Exception var14) {
         var8 = new RenderResult(TemplateError.fromException(var14), interpreter.getContext(), interpreter.getErrors());
      } finally {
         JinjavaInterpreter.popCurrent();
      }

      return var8;
   }

   public JinjavaInterpreter newInterpreter() {
      return new JinjavaInterpreter(this, this.getGlobalContext(), this.getGlobalConfig());
   }
}
