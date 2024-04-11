package com.hubspot.jinjava.loader;

import com.google.common.io.Resources;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.io.IOException;
import java.nio.charset.Charset;

public class ClasspathResourceLocator implements ResourceLocator {
   public String getString(String fullName, Charset encoding, JinjavaInterpreter interpreter) throws IOException {
      try {
         return Resources.toString(Resources.getResource(fullName), encoding);
      } catch (IllegalArgumentException var5) {
         throw new ResourceNotFoundException("Couldn't find resource: " + fullName);
      }
   }
}
