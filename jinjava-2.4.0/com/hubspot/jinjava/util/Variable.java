package com.hubspot.jinjava.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.util.Collections;
import java.util.List;

public class Variable {
   private static final Splitter DOT_SPLITTER = Splitter.on('.');
   private final JinjavaInterpreter interpreter;
   private final String name;
   private final List<String> chainList;

   public Variable(JinjavaInterpreter interpreter, String variable) {
      this.interpreter = interpreter;
      if (variable.indexOf(46) == -1) {
         this.name = variable;
         this.chainList = Collections.emptyList();
      } else {
         List<String> parts = Lists.newArrayList(DOT_SPLITTER.split(variable));
         this.name = (String)parts.get(0);
         this.chainList = parts.subList(1, parts.size());
      }

   }

   public String getName() {
      return this.name;
   }

   public Object resolve(Object value) {
      return this.interpreter.resolveProperty(value, this.chainList);
   }

   public String toString() {
      return "<Variable: " + this.name + ">";
   }
}
