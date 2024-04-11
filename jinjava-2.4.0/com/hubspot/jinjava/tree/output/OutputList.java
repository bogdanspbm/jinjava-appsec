package com.hubspot.jinjava.tree.output;

import com.hubspot.jinjava.interpret.OutputTooBigException;
import com.hubspot.jinjava.util.LengthLimitingStringBuilder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OutputList {
   private final List<OutputNode> nodes = new LinkedList();
   private final List<BlockPlaceholderOutputNode> blocks = new LinkedList();
   private final long maxOutputSize;
   private long currentSize;

   public OutputList(long maxOutputSize) {
      this.maxOutputSize = maxOutputSize;
   }

   public void addNode(OutputNode node) {
      if (this.maxOutputSize > 0L && this.currentSize + node.getSize() > this.maxOutputSize) {
         throw new OutputTooBigException(this.maxOutputSize, this.currentSize + node.getSize());
      } else {
         this.currentSize += node.getSize();
         this.nodes.add(node);
         if (node instanceof BlockPlaceholderOutputNode) {
            BlockPlaceholderOutputNode blockNode = (BlockPlaceholderOutputNode)node;
            if (this.maxOutputSize > 0L && this.currentSize + blockNode.getSize() > this.maxOutputSize) {
               throw new OutputTooBigException(this.maxOutputSize, this.currentSize + blockNode.getSize());
            }

            this.currentSize += blockNode.getSize();
            this.blocks.add(blockNode);
         }

      }
   }

   public List<BlockPlaceholderOutputNode> getBlocks() {
      return this.blocks;
   }

   public String getValue() {
      LengthLimitingStringBuilder val = new LengthLimitingStringBuilder(this.maxOutputSize);
      Iterator var2 = this.nodes.iterator();

      while(var2.hasNext()) {
         OutputNode node = (OutputNode)var2.next();
         val.append(node.getValue());
      }

      return val.toString();
   }

   public String toString() {
      return this.getValue();
   }
}
