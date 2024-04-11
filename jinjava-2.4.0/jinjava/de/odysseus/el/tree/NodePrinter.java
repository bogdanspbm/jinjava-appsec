package jinjava.de.odysseus.el.tree;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Stack;

public class NodePrinter {
   private static boolean isLastSibling(Node node, Node parent) {
      if (parent != null) {
         return node == parent.getChild(parent.getCardinality() - 1);
      } else {
         return true;
      }
   }

   private static void dump(PrintWriter writer, Node node, Stack<Node> predecessors) {
      Node parent;
      Iterator i$;
      Node predecessor;
      if (!predecessors.isEmpty()) {
         parent = null;

         for(i$ = predecessors.iterator(); i$.hasNext(); parent = predecessor) {
            predecessor = (Node)i$.next();
            if (isLastSibling(predecessor, parent)) {
               writer.print("   ");
            } else {
               writer.print("|  ");
            }
         }

         writer.println("|");
      }

      parent = null;

      for(i$ = predecessors.iterator(); i$.hasNext(); parent = predecessor) {
         predecessor = (Node)i$.next();
         if (isLastSibling(predecessor, parent)) {
            writer.print("   ");
         } else {
            writer.print("|  ");
         }
      }

      writer.print("+- ");
      writer.println(node.toString());
      predecessors.push(node);

      for(int i = 0; i < node.getCardinality(); ++i) {
         dump(writer, node.getChild(i), predecessors);
      }

      predecessors.pop();
   }

   public static void dump(PrintWriter writer, Node node) {
      dump(writer, node, new Stack());
   }
}
