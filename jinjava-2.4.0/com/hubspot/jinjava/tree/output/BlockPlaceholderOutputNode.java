package com.hubspot.jinjava.tree.output;

import com.google.common.base.Charsets;
import java.nio.charset.Charset;

public class BlockPlaceholderOutputNode implements OutputNode {
   private final String blockName;
   private String output;

   public BlockPlaceholderOutputNode(String blockName) {
      this.blockName = blockName;
   }

   public String getBlockName() {
      return this.blockName;
   }

   public boolean isResolved() {
      return this.output != null;
   }

   public void resolve(String output) {
      this.output = output;
   }

   public String getValue() {
      if (this.output == null) {
         throw new IllegalStateException("Block placeholder not resolved: " + this.blockName);
      } else {
         return this.output;
      }
   }

   public long getSize() {
      return this.output == null ? 0L : (long)this.output.getBytes(Charset.forName(Charsets.UTF_8.name())).length;
   }

   public String toString() {
      return this.getValue();
   }
}
