package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.fn.Functions;
import com.hubspot.jinjava.util.Logging;
import java.util.Objects;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

@JinjavaDoc(
   value = "Truncates a given string, respecting html markup (i.e. will properly close all nested tags)",
   params = {@JinjavaParam(
   value = "html",
   desc = "HTML to truncate"
), @JinjavaParam(
   value = "length",
   type = "number",
   defaultValue = "255",
   desc = "Length at which to truncate text (HTML characters not included)"
), @JinjavaParam(
   value = "end",
   defaultValue = "...",
   desc = "The characters that will be added to indicate where the text was truncated"
), @JinjavaParam(
   value = "breakword",
   type = "boolean",
   defaultValue = "false",
   desc = "If set to true, text will be truncated in the middle of words"
)},
   snippets = {@JinjavaSnippet(
   code = "{{ \"<p>I want to truncate this text without breaking my HTML<p>\"|truncatehtml(28, '..', false) }}",
   output = "<p>I want to truncate this text without breaking my HTML</p>"
)}
)
public class TruncateHtmlFilter implements Filter {
   private static final int DEFAULT_TRUNCATE_LENGTH = 255;
   private static final String DEFAULT_END = "...";

   public String getName() {
      return "truncatehtml";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      if (var instanceof String) {
         int length = 255;
         String ends = "...";
         if (args.length > 0) {
            try {
               length = Integer.parseInt(Objects.toString(args[0]));
            } catch (Exception var9) {
               Logging.ENGINE_LOG.warn("truncatehtml(): error setting length for {}, using default {}", args[0], 255);
            }
         }

         if (args.length > 1) {
            ends = Objects.toString(args[1]);
         }

         boolean killwords = false;
         if (args.length > 2) {
            killwords = BooleanUtils.toBoolean(args[2]);
         }

         Document dom = Jsoup.parseBodyFragment((String)var);
         TruncateHtmlFilter.ContentTruncatingNodeVisitor visitor = new TruncateHtmlFilter.ContentTruncatingNodeVisitor(length, ends, killwords);
         dom.select("body").traverse(visitor);
         dom.select(".__deleteme").remove();
         return dom.select("body").html();
      } else {
         return var;
      }
   }

   private static class ContentTruncatingNodeVisitor implements NodeVisitor {
      private int maxTextLen;
      private int textLen;
      private String ending;
      private boolean killwords;

      public ContentTruncatingNodeVisitor(int maxTextLen, String ending, boolean killwords) {
         this.maxTextLen = maxTextLen;
         this.ending = ending;
         this.killwords = killwords;
      }

      public void head(Node node, int depth) {
         if (node instanceof TextNode) {
            TextNode text = (TextNode)node;
            String textContent = text.text();
            if (this.textLen >= this.maxTextLen) {
               text.text("");
            } else if (this.textLen + textContent.length() > this.maxTextLen) {
               int ptr = this.maxTextLen - this.textLen;
               if (!this.killwords) {
                  ptr = Functions.movePointerToJustBeforeLastWord(ptr, textContent) - 1;
               }

               text.text(textContent.substring(0, ptr) + this.ending);
               this.textLen = this.maxTextLen;
            } else {
               this.textLen += textContent.length();
            }
         }

      }

      public void tail(Node node, int depth) {
         if (node instanceof Element) {
            Element el = (Element)node;
            if (StringUtils.isBlank(el.text())) {
               el.addClass("__deleteme");
            }
         }

      }
   }
}
