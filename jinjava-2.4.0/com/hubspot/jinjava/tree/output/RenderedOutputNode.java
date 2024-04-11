package com.hubspot.jinjava.tree.output;

import com.google.common.base.Charsets;
import java.nio.charset.Charset;

public class RenderedOutputNode implements OutputNode {
   private final String output;

   public RenderedOutputNode(String output) {
      this.output = output;
   }

   public String getValue() {
      return this.output;
   }

   public String toString() {
      return this.getValue();
   }

   public long getSize() {
      return this.output == null ? 0L : (long)this.output.getBytes(Charset.forName(Charsets.UTF_8.name())).length;
   }
}
