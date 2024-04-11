package com.hubspot.jinjava.interpret;

import java.util.Collection;

public class FatalTemplateErrorsException extends InterpretException {
   private static final long serialVersionUID = 1L;
   private final String template;
   private final Iterable<TemplateError> errors;

   public FatalTemplateErrorsException(String template, Collection<TemplateError> errors) {
      super(generateMessage(errors));
      this.template = template;
      this.errors = errors;
   }

   private static String generateMessage(Collection<TemplateError> errors) {
      if (errors.isEmpty()) {
         throw new IllegalArgumentException("FatalTemplateErrorsException should have at least one error");
      } else {
         return ((TemplateError)errors.iterator().next()).getMessage();
      }
   }

   public String getTemplate() {
      return this.template;
   }

   public Iterable<TemplateError> getErrors() {
      return this.errors;
   }
}
