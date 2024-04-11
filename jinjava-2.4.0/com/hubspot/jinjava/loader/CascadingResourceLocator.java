package com.hubspot.jinjava.loader;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;

public class CascadingResourceLocator implements ResourceLocator {
   private Iterable<ResourceLocator> locators;

   public CascadingResourceLocator(ResourceLocator... locators) {
      this.locators = Arrays.asList(locators);
   }

   public String getString(String fullName, Charset encoding, JinjavaInterpreter interpreter) throws IOException {
      Iterator var4 = this.locators.iterator();

      while(var4.hasNext()) {
         ResourceLocator locator = (ResourceLocator)var4.next();

         try {
            return locator.getString(fullName, encoding, interpreter);
         } catch (ResourceNotFoundException var7) {
         }
      }

      throw new ResourceNotFoundException("Couldn't find resource: " + fullName);
   }
}
