package com.hubspot.jinjava.el;

import com.google.common.collect.ImmutableMap;
import com.hubspot.jinjava.el.ext.NamedParameter;
import com.hubspot.jinjava.interpret.DisabledException;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.interpret.TemplateError;
import com.hubspot.jinjava.interpret.TemplateSyntaxException;
import com.hubspot.jinjava.interpret.UnknownTokenException;
import com.hubspot.jinjava.interpret.errorcategory.BasicTemplateErrorCategory;
import com.hubspot.jinjava.lib.fn.ELFunctionDefinition;
import java.util.Iterator;
import java.util.List;
import jinjava.de.odysseus.el.tree.TreeBuilderException;
import jinjava.javax.el.ELException;
import jinjava.javax.el.ExpressionFactory;
import jinjava.javax.el.PropertyNotFoundException;
import jinjava.javax.el.ValueExpression;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class ExpressionResolver {
   private final JinjavaInterpreter interpreter;
   private final ExpressionFactory expressionFactory;
   private final JinjavaInterpreterResolver resolver;
   private final JinjavaELContext elContext;

   public ExpressionResolver(JinjavaInterpreter interpreter, ExpressionFactory expressionFactory) {
      this.interpreter = interpreter;
      this.expressionFactory = expressionFactory;
      this.resolver = new JinjavaInterpreterResolver(interpreter);
      this.elContext = new JinjavaELContext(this.resolver);
      Iterator var3 = interpreter.getContext().getAllFunctions().iterator();

      while(var3.hasNext()) {
         ELFunctionDefinition fn = (ELFunctionDefinition)var3.next();
         this.elContext.setFunction(fn.getNamespace(), fn.getLocalName(), fn.getMethod());
      }

   }

   public Object resolveExpression(String expression) {
      if (StringUtils.isBlank(expression)) {
         return "";
      } else {
         this.interpreter.getContext().addResolvedExpression(expression.trim());

         try {
            String elExpression = "#{" + expression.trim() + "}";
            ValueExpression valueExp = this.expressionFactory.createValueExpression(this.elContext, elExpression, Object.class);
            Object result = valueExp.getValue(this.elContext);
            this.validateResult(result);
            return result;
         } catch (PropertyNotFoundException var5) {
            this.interpreter.addError(new TemplateError(TemplateError.ErrorType.WARNING, TemplateError.ErrorReason.UNKNOWN, TemplateError.ErrorItem.PROPERTY, var5.getMessage(), "", this.interpreter.getLineNumber(), -1, var5, BasicTemplateErrorCategory.UNKNOWN, ImmutableMap.of("exception", var5.getMessage())));
         } catch (TreeBuilderException var6) {
            this.interpreter.addError(TemplateError.fromException(new TemplateSyntaxException(expression, "Error parsing '" + expression + "': " + StringUtils.substringAfter(var6.getMessage(), "': "), this.interpreter.getLineNumber(), var6.getPosition(), var6)));
         } catch (ELException var7) {
            this.interpreter.addError(TemplateError.fromException(new TemplateSyntaxException(expression, var7.getMessage(), this.interpreter.getLineNumber(), var7)));
         } catch (DisabledException var8) {
            this.interpreter.addError(new TemplateError(TemplateError.ErrorType.FATAL, TemplateError.ErrorReason.DISABLED, TemplateError.ErrorItem.FUNCTION, var8.getMessage(), expression, this.interpreter.getLineNumber(), -1, var8));
         } catch (UnknownTokenException var9) {
            throw var9;
         } catch (Exception var10) {
            this.interpreter.addError(TemplateError.fromException((Exception)(new InterpretException(String.format("Error resolving expression [%s]: " + ExceptionUtils.getRootCauseMessage(var10), expression), var10, this.interpreter.getLineNumber()))));
         }

         return "";
      }
   }

   private void validateResult(Object result) {
      if (result instanceof NamedParameter) {
         throw new ELException("Unexpected '=' operator (use {% set %} tag for variable assignment)");
      }
   }

   public Object resolveProperty(Object object, List<String> propertyNames) {
      Object value = this.resolver.wrap(object);

      String propertyName;
      for(Iterator var4 = propertyNames.iterator(); var4.hasNext(); value = this.resolver.getValue(this.elContext, value, propertyName)) {
         propertyName = (String)var4.next();
         if (value == null) {
            return null;
         }
      }

      return value;
   }
}
