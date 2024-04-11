package com.hubspot.jinjava.tree;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.tree.output.OutputNode;
import com.hubspot.jinjava.tree.parse.Token;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.commons.lang3.StringUtils;

public abstract class Node implements Serializable {
   private static final long serialVersionUID = -6194634312533310816L;
   private final Token master;
   private final int lineNumber;
   private final int startPosition;
   private Node parent;
   private LinkedList<Node> children;

   public Node(Token master, int lineNumber) {
      this(master, lineNumber, -1);
   }

   public Node(Token master, int lineNumber, int startPosition) {
      this.parent = null;
      this.children = new LinkedList();
      this.master = master;
      this.lineNumber = lineNumber;
      this.startPosition = startPosition;
   }

   public Node getParent() {
      return this.parent;
   }

   public void setParent(Node parent) {
      this.parent = parent;
   }

   public Token getMaster() {
      return this.master;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public int getStartPosition() {
      return this.startPosition;
   }

   public LinkedList<Node> getChildren() {
      return this.children;
   }

   public void setChildren(LinkedList<Node> children) {
      this.children = children;
   }

   public abstract OutputNode render(JinjavaInterpreter var1);

   public abstract String getName();

   public String toTreeString() {
      return this.toTreeString(0);
   }

   public String toTreeString(int level) {
      String prefix = StringUtils.repeat(" ", level * 4) + " ";
      StringBuilder t = (new StringBuilder(prefix)).append(this.toString()).append('\n');
      Iterator var4 = this.getChildren().iterator();

      while(var4.hasNext()) {
         Node n = (Node)var4.next();
         t.append(n.toTreeString(level + 1));
      }

      if (this.getChildren().size() > 0) {
         t.append(prefix).append("end :: " + this.toString()).append('\n');
      }

      return t.toString();
   }
}
