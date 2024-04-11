package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JinjavaDoc(
   value = "Counts the words in the given string",
   snippets = {@JinjavaSnippet(
   code = "{%  set count_words = \"Count the number of words in this variable\" %}\n{{ count_words|wordcount }}"
)}
)
public class WordCountFilter implements Filter {
   private static final Pattern WORD_RE = Pattern.compile("\\w+", 264);

   public String getName() {
      return "wordcount";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      Matcher matcher = WORD_RE.matcher(Objects.toString(var, ""));

      int count;
      for(count = 0; matcher.find(); ++count) {
      }

      return count;
   }
}
