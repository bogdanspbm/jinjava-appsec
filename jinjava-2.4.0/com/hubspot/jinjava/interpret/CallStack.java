package com.hubspot.jinjava.interpret;

import java.util.Optional;
import java.util.Stack;

public class CallStack {
   private final CallStack parent;
   private final Class<? extends TagCycleException> exceptionClass;
   private final Stack<String> stack = new Stack();

   public CallStack(CallStack parent, Class<? extends TagCycleException> exceptionClass) {
      this.parent = parent;
      this.exceptionClass = exceptionClass;
   }

   public boolean contains(String path) {
      if (this.stack.contains(path)) {
         return true;
      } else {
         return this.parent != null ? this.parent.contains(path) : false;
      }
   }

   public void pushWithoutCycleCheck(String path) {
      this.stack.push(path);
   }

   public void push(String path, int lineNumber, int startPosition) {
      if (this.contains(path)) {
         throw TagCycleException.create(this.exceptionClass, path, Optional.of(lineNumber), Optional.of(startPosition));
      } else {
         this.stack.push(path);
      }
   }

   public Optional<String> pop() {
      if (this.stack.isEmpty()) {
         return this.parent != null ? this.parent.pop() : Optional.empty();
      } else {
         return Optional.of(this.stack.pop());
      }
   }
}
