package com.hubspot.jinjava.el.ext;

import com.hubspot.jinjava.objects.collections.PyList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import jinjava.de.odysseus.el.misc.LocalMessages;
import jinjava.de.odysseus.el.tree.Bindings;
import jinjava.de.odysseus.el.tree.impl.ast.AstBracket;
import jinjava.de.odysseus.el.tree.impl.ast.AstNode;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELException;
import jinjava.javax.el.PropertyNotFoundException;

public class AstRangeBracket extends AstBracket {
   protected final AstNode rangeMax;

   public AstRangeBracket(AstNode base, AstNode rangeStart, AstNode rangeMax, boolean lvalue, boolean strict, boolean ignoreReturnType) {
      super(base, rangeStart, lvalue, strict, ignoreReturnType);
      this.rangeMax = rangeMax;
   }

   public Object eval(Bindings bindings, ELContext context) {
      Object base = this.prefix.eval(bindings, context);
      if (base == null) {
         throw new PropertyNotFoundException(LocalMessages.get("error.property.base.null", this.prefix));
      } else {
         boolean baseIsString = base.getClass().equals(String.class);
         if (!Iterable.class.isAssignableFrom(base.getClass()) && !base.getClass().isArray() && !baseIsString) {
            throw new ELException("Property " + this.prefix + " is not a sequence.");
         } else if (baseIsString) {
            return this.evalString((String)base, bindings, context);
         } else {
            Object start = this.property.eval(bindings, context);
            if (start == null && this.strict) {
               return Collections.emptyList();
            } else if (!(start instanceof Number)) {
               throw new ELException("Range start is not a number");
            } else {
               Object end = this.rangeMax.eval(bindings, context);
               if (end == null && this.strict) {
                  return Collections.emptyList();
               } else if (!(end instanceof Number)) {
                  throw new ELException("Range end is not a number");
               } else {
                  int startNum = ((Number)start).intValue();
                  int endNum = ((Number)end).intValue();
                  Object baseItr;
                  if (base.getClass().isArray()) {
                     baseItr = Arrays.asList((Object[])((Object[])base));
                  } else {
                     baseItr = (Iterable)base;
                  }

                  PyList result = new PyList(new ArrayList());
                  int index = 0;

                  for(Iterator baseIterator = ((Iterable)baseItr).iterator(); baseIterator.hasNext(); ++index) {
                     Object next = baseIterator.next();
                     if (index >= startNum) {
                        if (index >= endNum) {
                           break;
                        }

                        result.add(next);
                     }
                  }

                  return result;
               }
            }
         }
      }
   }

   private String evalString(String base, Bindings bindings, ELContext context) {
      int startNum = this.intVal(this.property, 0, base.length(), bindings, context);
      int endNum = this.intVal(this.rangeMax, base.length(), base.length(), bindings, context);
      endNum = Math.min(endNum, base.length());
      return startNum > endNum ? "" : base.substring(startNum, endNum);
   }

   private int intVal(AstNode node, int defVal, int baseLength, Bindings bindings, ELContext context) {
      if (node == null) {
         return defVal;
      } else {
         Object val = node.eval(bindings, context);
         if (val == null) {
            return defVal;
         } else if (!(val instanceof Number)) {
            throw new ELException("Range start/end is not a number");
         } else {
            int result = ((Number)val).intValue();
            return result >= 0 ? result : baseLength + result;
         }
      }
   }

   public String toString() {
      return "[:]";
   }
}
