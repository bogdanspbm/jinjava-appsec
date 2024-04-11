package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.util.LengthLimitingStringBuilder;
import java.util.Locale;
import java.util.Objects;

@JinjavaDoc(
   value = "Escapes strings so that they can be safely inserted into a JavaScript variable declaration",
   params = {@JinjavaParam(
   value = "s",
   desc = "String to escape"
)},
   snippets = {@JinjavaSnippet(
   code = "{% set escape_string = \"This string can safely be inserted into JavaScript\" %}\n{{ escape_string|escapejs }}"
)}
)
public class EscapeJsFilter implements Filter {
   public Object filter(Object objectToFilter, JinjavaInterpreter jinjavaInterpreter, String... strings) {
      String input = Objects.toString(objectToFilter, "");
      LengthLimitingStringBuilder builder = new LengthLimitingStringBuilder(jinjavaInterpreter.getConfig().getMaxOutputSize());

      for(int i = 0; i < input.length(); ++i) {
         char ch = input.charAt(i);
         if (ch > 4095) {
            builder.append("\\u");
            builder.append(this.toHex(ch));
         } else if (ch > 255) {
            builder.append("\\u0");
            builder.append(this.toHex(ch));
         } else if (ch > 127) {
            builder.append("\\u00");
            builder.append(this.toHex(ch));
         } else if (ch < ' ') {
            switch(ch) {
            case '\b':
               builder.append("\\b");
               break;
            case '\t':
               builder.append("\\t");
               break;
            case '\n':
               builder.append("\\n");
               break;
            case '\u000b':
            default:
               if (ch > 15) {
                  builder.append("\\u00");
                  builder.append(this.toHex(ch));
               } else {
                  builder.append("\\u000");
                  builder.append(this.toHex(ch));
               }
               break;
            case '\f':
               builder.append("\\f");
               break;
            case '\r':
               builder.append("\\r");
            }
         } else {
            switch(ch) {
            case '"':
               builder.append("\\\"");
               break;
            case '\\':
               builder.append("\\\\");
               break;
            default:
               builder.append((Object)ch);
            }
         }
      }

      return builder.toString();
   }

   public String getName() {
      return "escapejs";
   }

   private String toHex(char ch) {
      return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
   }
}
