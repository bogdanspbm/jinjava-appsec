package com.hubspot.jinjava.tree;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.filter.EscapeFilter;
import com.hubspot.jinjava.tree.output.OutputNode;
import com.hubspot.jinjava.tree.output.RenderedOutputNode;
import com.hubspot.jinjava.tree.parse.ExpressionToken;
import com.hubspot.jinjava.util.Logging;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class ExpressionNode extends Node {
   private static final long serialVersionUID = -6063173739682221042L;
   private final ExpressionToken master;

   public ExpressionNode(ExpressionToken token) {
      super(token, token.getLineNumber(), token.getStartPosition());
      this.master = token;
   }

   public OutputNode render(JinjavaInterpreter interpreter) {
      Object var = interpreter.resolveELExpression(this.master.getExpr(), this.getLineNumber());
      String result = Objects.toString(var, "");
      if (interpreter.getConfig().isNestedInterpretationEnabled() && !StringUtils.equals(result, this.master.getImage()) && (StringUtils.contains(result, "{{") || StringUtils.contains(result, "{%"))) {
         try {
            result = interpreter.renderFlat(result);
         } catch (Exception var5) {
            Logging.ENGINE_LOG.warn("Error rendering variable node result", var5);
         }
      }

      if (interpreter.getContext().isAutoEscape()) {
         result = EscapeFilter.escapeHtmlEntities(result);
      }

      return new RenderedOutputNode(result);
   }

   public String toString() {
      return this.master.toString();
   }

   public String getName() {
      return this.getClass().getSimpleName();
   }
}
