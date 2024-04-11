package com.hubspot.jinjava.interpret;

import com.google.common.collect.ImmutableMap;
import com.hubspot.jinjava.interpret.errorcategory.BasicTemplateErrorCategory;
import com.hubspot.jinjava.interpret.errorcategory.TemplateErrorCategory;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class TemplateError {
   private final TemplateError.ErrorType severity;
   private final TemplateError.ErrorReason reason;
   private final TemplateError.ErrorItem item;
   private final String message;
   private final String fieldName;
   private final int lineno;
   private final int startPosition;
   private final TemplateErrorCategory category;
   private final Map<String, String> categoryErrors;
   private int scopeDepth = 1;
   private final Exception exception;
   private static final Pattern GENERIC_TOSTRING_PATTERN = Pattern.compile("@[0-9a-z]{4,}$");

   public TemplateError withScopeDepth(int scopeDepth) {
      return new TemplateError(this.getSeverity(), this.getReason(), this.getItem(), this.getMessage(), this.getFieldName(), this.getLineno(), this.getStartPosition(), this.getException(), this.getCategory(), this.getCategoryErrors(), scopeDepth);
   }

   public static TemplateError fromSyntaxError(InterpretException ex) {
      return new TemplateError(TemplateError.ErrorType.FATAL, TemplateError.ErrorReason.SYNTAX_ERROR, TemplateError.ErrorItem.OTHER, ExceptionUtils.getMessage(ex), (String)null, ex.getLineNumber(), ex.getStartPosition(), ex);
   }

   public static TemplateError fromException(TemplateSyntaxException ex) {
      String fieldName = ex instanceof UnknownTagException ? ((UnknownTagException)ex).getTag() : ex.getCode();
      return new TemplateError(TemplateError.ErrorType.FATAL, TemplateError.ErrorReason.SYNTAX_ERROR, TemplateError.ErrorItem.OTHER, ExceptionUtils.getMessage(ex), fieldName, ex.getLineNumber(), ex.getStartPosition(), ex);
   }

   public static TemplateError fromException(Exception ex) {
      int lineNumber = -1;
      int startPosition = -1;
      if (ex instanceof InterpretException) {
         lineNumber = ((InterpretException)ex).getLineNumber();
         startPosition = ((InterpretException)ex).getStartPosition();
      }

      return new TemplateError(TemplateError.ErrorType.FATAL, TemplateError.ErrorReason.EXCEPTION, TemplateError.ErrorItem.OTHER, ExceptionUtils.getMessage(ex), (String)null, lineNumber, startPosition, ex, BasicTemplateErrorCategory.UNKNOWN, ImmutableMap.of());
   }

   public static TemplateError fromException(Exception ex, int lineNumber, int startPosition) {
      return new TemplateError(TemplateError.ErrorType.FATAL, TemplateError.ErrorReason.EXCEPTION, TemplateError.ErrorItem.OTHER, ExceptionUtils.getMessage(ex), (String)null, lineNumber, startPosition, ex);
   }

   public static TemplateError fromException(Exception ex, int lineNumber) {
      return new TemplateError(TemplateError.ErrorType.FATAL, TemplateError.ErrorReason.EXCEPTION, TemplateError.ErrorItem.OTHER, ExceptionUtils.getMessage(ex), (String)null, lineNumber, -1, ex);
   }

   public static TemplateError fromUnknownProperty(Object base, String variable, int lineNumber) {
      return fromUnknownProperty(base, variable, lineNumber, -1);
   }

   public static TemplateError fromUnknownProperty(Object base, String variable, int lineNumber, int startPosition) {
      return new TemplateError(TemplateError.ErrorType.WARNING, TemplateError.ErrorReason.UNKNOWN, TemplateError.ErrorItem.PROPERTY, String.format("Cannot resolve property '%s' in '%s'", variable, friendlyObjectToString(base)), variable, lineNumber, startPosition, (Exception)null, BasicTemplateErrorCategory.UNKNOWN_PROPERTY, ImmutableMap.of("property", variable, "lineNumber", String.valueOf(lineNumber), "startPosition", String.valueOf(startPosition)));
   }

   private static String friendlyObjectToString(Object o) {
      if (o == null) {
         return "null";
      } else {
         String s = o.toString();
         if (!GENERIC_TOSTRING_PATTERN.matcher(s).find()) {
            return s;
         } else {
            Class<?> c = o.getClass();
            return c.getSimpleName();
         }
      }
   }

   public TemplateError(TemplateError.ErrorType severity, TemplateError.ErrorReason reason, TemplateError.ErrorItem item, String message, String fieldName, int lineno, Exception exception) {
      this.severity = severity;
      this.reason = reason;
      this.item = item;
      this.message = message;
      this.fieldName = fieldName;
      this.lineno = lineno;
      this.startPosition = -1;
      this.exception = exception;
      this.category = BasicTemplateErrorCategory.UNKNOWN;
      this.categoryErrors = null;
   }

   public TemplateError(TemplateError.ErrorType severity, TemplateError.ErrorReason reason, TemplateError.ErrorItem item, String message, String fieldName, int lineno, int startPosition, Exception exception) {
      this.severity = severity;
      this.reason = reason;
      this.item = item;
      this.message = message;
      this.fieldName = fieldName;
      this.lineno = lineno;
      this.startPosition = startPosition;
      this.exception = exception;
      this.category = BasicTemplateErrorCategory.UNKNOWN;
      this.categoryErrors = null;
   }

   public TemplateError(TemplateError.ErrorType severity, TemplateError.ErrorReason reason, TemplateError.ErrorItem item, String message, String fieldName, int lineno, int startPosition, Exception exception, TemplateErrorCategory category, Map<String, String> categoryErrors, int scopeDepth) {
      this.severity = severity;
      this.reason = reason;
      this.item = item;
      this.message = message;
      this.fieldName = fieldName;
      this.lineno = lineno;
      this.startPosition = startPosition;
      this.exception = exception;
      this.category = category;
      this.categoryErrors = categoryErrors;
      this.scopeDepth = scopeDepth;
   }

   public TemplateError(TemplateError.ErrorType severity, TemplateError.ErrorReason reason, TemplateError.ErrorItem item, String message, String fieldName, int lineno, Exception exception, TemplateErrorCategory category, Map<String, String> categoryErrors) {
      this.severity = severity;
      this.reason = reason;
      this.item = item;
      this.message = message;
      this.fieldName = fieldName;
      this.lineno = lineno;
      this.startPosition = -1;
      this.exception = exception;
      this.category = category;
      this.categoryErrors = categoryErrors;
   }

   public TemplateError(TemplateError.ErrorType severity, TemplateError.ErrorReason reason, TemplateError.ErrorItem item, String message, String fieldName, int lineno, int startPosition, Exception exception, TemplateErrorCategory category, Map<String, String> categoryErrors) {
      this.severity = severity;
      this.reason = reason;
      this.item = item;
      this.message = message;
      this.fieldName = fieldName;
      this.lineno = lineno;
      this.startPosition = startPosition;
      this.exception = exception;
      this.category = category;
      this.categoryErrors = categoryErrors;
   }

   public TemplateError(TemplateError.ErrorType severity, TemplateError.ErrorReason reason, String message, String fieldName, int lineno, int startPosition, Exception exception) {
      this.severity = severity;
      this.reason = reason;
      this.item = TemplateError.ErrorItem.OTHER;
      this.message = message;
      this.fieldName = fieldName;
      this.lineno = lineno;
      this.startPosition = startPosition;
      this.exception = exception;
      this.category = BasicTemplateErrorCategory.UNKNOWN;
      this.categoryErrors = null;
   }

   public TemplateError.ErrorType getSeverity() {
      return this.severity;
   }

   public TemplateError.ErrorReason getReason() {
      return this.reason;
   }

   public TemplateError.ErrorItem getItem() {
      return this.item;
   }

   public String getMessage() {
      return this.message;
   }

   public String getFieldName() {
      return this.fieldName;
   }

   public int getLineno() {
      return this.lineno;
   }

   public int getStartPosition() {
      return this.startPosition;
   }

   public Exception getException() {
      return this.exception;
   }

   public TemplateErrorCategory getCategory() {
      return this.category;
   }

   public Map<String, String> getCategoryErrors() {
      return this.categoryErrors;
   }

   public int getScopeDepth() {
      return this.scopeDepth;
   }

   public TemplateError serializable() {
      return new TemplateError(this.severity, this.reason, this.item, this.message, this.fieldName, this.lineno, this.startPosition, (Exception)null, this.category, this.categoryErrors, this.scopeDepth);
   }

   public String toString() {
      return "TemplateError{severity=" + this.severity + ", reason=" + this.reason + ", item=" + this.item + ", message='" + this.message + '\'' + ", fieldName='" + this.fieldName + '\'' + ", lineno=" + this.lineno + ", startPosition=" + this.startPosition + ", scopeDepth=" + this.scopeDepth + ", category=" + this.category + ", categoryErrors=" + this.categoryErrors + '}';
   }

   public static enum ErrorItem {
      TEMPLATE,
      TOKEN,
      TAG,
      FUNCTION,
      PROPERTY,
      FILTER,
      EXPRESSION_TEST,
      OTHER;
   }

   public static enum ErrorReason {
      SYNTAX_ERROR,
      UNKNOWN,
      BAD_URL,
      EXCEPTION,
      MISSING,
      DISABLED,
      OTHER;
   }

   public static enum ErrorType {
      FATAL,
      WARNING;
   }
}
