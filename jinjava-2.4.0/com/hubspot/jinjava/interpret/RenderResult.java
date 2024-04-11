package com.hubspot.jinjava.interpret;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;

public class RenderResult {
   private final String output;
   private final Context context;
   private final List<TemplateError> errors;

   public RenderResult(String output, Context context, List<TemplateError> errors) {
      this.output = output;
      this.context = context;
      this.errors = errors;
   }

   public RenderResult(TemplateError fromException, Context context, List<TemplateError> errors) {
      this.output = "";
      this.context = context;
      this.errors = ImmutableList.builder().add(fromException).addAll(errors).build();
   }

   public RenderResult(String result) {
      this.output = result;
      this.context = null;
      this.errors = Collections.emptyList();
   }

   public boolean hasErrors() {
      return !this.errors.isEmpty();
   }

   public List<TemplateError> getErrors() {
      return this.errors;
   }

   public Context getContext() {
      return this.context;
   }

   public String getOutput() {
      return this.output;
   }

   public RenderResult withOutput(String newOutput) {
      return new RenderResult(newOutput, this.getContext(), this.getErrors());
   }
}
