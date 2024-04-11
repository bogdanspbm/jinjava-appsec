package com.hubspot.jinjava.interpret;

import java.util.Optional;

public class TagCycleException extends TemplateStateException {
   private static final long serialVersionUID = -3058494056577268723L;
   private final String path;
   private final String tagName;

   public TagCycleException(String tagName, String path, int lineNumber, int startPosition) {
      super(tagName + " tag cycle for '" + path + "'", lineNumber, startPosition);
      this.path = path;
      this.tagName = tagName;
   }

   public String getPath() {
      return this.path;
   }

   public String getTagName() {
      return this.tagName;
   }

   public static TagCycleException create(Class<? extends TagCycleException> clazz, String path, Optional<Integer> lineNumber, Optional<Integer> startPosition) {
      Integer line = (Integer)lineNumber.orElse(-1);
      Integer position = (Integer)startPosition.orElse(-1);
      if (clazz != null) {
         if (clazz.equals(ExtendsTagCycleException.class)) {
            return new ExtendsTagCycleException(path, line, position);
         }

         if (clazz.equals(ImportTagCycleException.class)) {
            return new ImportTagCycleException(path, line, position);
         }

         if (clazz.equals(IncludeTagCycleException.class)) {
            return new IncludeTagCycleException(path, line, position);
         }

         if (clazz.equals(MacroTagCycleException.class)) {
            return new MacroTagCycleException(path, line, position);
         }
      }

      return new TagCycleException("", path, line, position);
   }
}
