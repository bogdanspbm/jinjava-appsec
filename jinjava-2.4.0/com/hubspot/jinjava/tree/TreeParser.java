package com.hubspot.jinjava.tree;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import com.hubspot.jinjava.interpret.DisabledException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.interpret.MissingEndTagException;
import com.hubspot.jinjava.interpret.TemplateError;
import com.hubspot.jinjava.interpret.TemplateSyntaxException;
import com.hubspot.jinjava.interpret.UnexpectedTokenException;
import com.hubspot.jinjava.interpret.UnknownTagException;
import com.hubspot.jinjava.lib.tag.EndTag;
import com.hubspot.jinjava.lib.tag.Tag;
import com.hubspot.jinjava.tree.parse.ExpressionToken;
import com.hubspot.jinjava.tree.parse.TagToken;
import com.hubspot.jinjava.tree.parse.TextToken;
import com.hubspot.jinjava.tree.parse.Token;
import com.hubspot.jinjava.tree.parse.TokenScanner;
import org.apache.commons.lang3.StringUtils;

public class TreeParser {
   private final PeekingIterator<Token> scanner;
   private final JinjavaInterpreter interpreter;
   private Node parent;

   public TreeParser(JinjavaInterpreter interpreter, String input) {
      this.scanner = Iterators.peekingIterator(new TokenScanner(input, interpreter.getConfig()));
      this.interpreter = interpreter;
   }

   public Node buildTree() {
      Node root = new RootNode();
      this.parent = root;

      while(this.scanner.hasNext()) {
         Node node = this.nextNode();
         if (node != null) {
            this.parent.getChildren().add(node);
         }
      }

      if (this.parent != root) {
         this.interpreter.addError(TemplateError.fromException((TemplateSyntaxException)(new MissingEndTagException(((TagNode)this.parent).getEndName(), this.parent.getMaster().getImage(), this.parent.getLineNumber(), this.parent.getStartPosition()))));
      }

      return root;
   }

   private Node nextNode() {
      Token token = (Token)this.scanner.next();
      switch(token.getType()) {
      case 0:
         return this.text((TextToken)token);
      case 37:
         return this.tag((TagToken)token);
      case 123:
         return this.expression((ExpressionToken)token);
      default:
         this.interpreter.addError(TemplateError.fromException((TemplateSyntaxException)(new UnexpectedTokenException(token.getImage(), token.getLineNumber(), token.getStartPosition()))));
      case 35:
         return null;
      }
   }

   private Node getLastSibling() {
      return this.parent != null && !this.parent.getChildren().isEmpty() ? (Node)this.parent.getChildren().getLast() : null;
   }

   private Node text(TextToken textToken) {
      if (this.interpreter.getConfig().isLstripBlocks() && this.scanner.hasNext() && ((Token)this.scanner.peek()).getType() == 37) {
         textToken = new TextToken(StringUtils.stripEnd(textToken.getImage(), "\t "), textToken.getLineNumber(), textToken.getStartPosition());
      }

      Node lastSibling = this.getLastSibling();
      if (lastSibling != null && lastSibling instanceof TagNode && lastSibling.getMaster().isRightTrimAfterEnd()) {
         textToken.setLeftTrim(true);
      }

      if (this.parent instanceof TagNode && lastSibling == null && this.parent.getMaster().isRightTrim()) {
         textToken.setLeftTrim(true);
      }

      TextNode n = new TextNode(textToken);
      n.setParent(this.parent);
      return n;
   }

   private Node expression(ExpressionToken expressionToken) {
      ExpressionNode n = new ExpressionNode(expressionToken);
      n.setParent(this.parent);
      return n;
   }

   private Node tag(TagToken tagToken) {
      Tag tag;
      try {
         tag = this.interpreter.getContext().getTag(tagToken.getTagName());
         if (tag == null) {
            this.interpreter.addError(TemplateError.fromException((TemplateSyntaxException)(new UnknownTagException(tagToken))));
            return null;
         }
      } catch (DisabledException var4) {
         this.interpreter.addError(new TemplateError(TemplateError.ErrorType.FATAL, TemplateError.ErrorReason.DISABLED, TemplateError.ErrorItem.TAG, var4.getMessage(), tagToken.getTagName(), this.interpreter.getLineNumber(), tagToken.getStartPosition(), var4));
         return null;
      }

      if (tag instanceof EndTag) {
         this.endTag(tag, tagToken);
         return null;
      } else {
         if (tagToken.isLeftTrim()) {
            Node lastSibling = this.getLastSibling();
            if (lastSibling != null && lastSibling instanceof TextNode) {
               lastSibling.getMaster().setRightTrim(true);
            }
         }

         TagNode node = new TagNode(tag, tagToken);
         node.setParent(this.parent);
         if (node.getEndName() != null) {
            this.parent.getChildren().add(node);
            this.parent = node;
            return null;
         } else {
            return node;
         }
      }
   }

   private void endTag(Tag tag, TagToken tagToken) {
      Node lastSibling = this.getLastSibling();
      if (this.parent instanceof TagNode && tagToken.isLeftTrim() && lastSibling != null && lastSibling instanceof TextNode) {
         lastSibling.getMaster().setRightTrim(true);
      }

      if (this.parent.getMaster() != null) {
         this.parent.getMaster().setRightTrimAfterEnd(tagToken.isRightTrim());
      }

      while(!(this.parent instanceof RootNode)) {
         TagNode parentTag = (TagNode)this.parent;
         this.parent = this.parent.getParent();
         if (parentTag.getEndName().equals(tag.getEndTagName())) {
            break;
         }

         this.interpreter.addError(TemplateError.fromException(new TemplateSyntaxException(tagToken.getImage(), "Mismatched end tag, expected: " + parentTag.getEndName(), tagToken.getLineNumber(), tagToken.getStartPosition())));
      }

   }
}
