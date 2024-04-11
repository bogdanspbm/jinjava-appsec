package com.hubspot.jinjava.loader;

import com.google.common.io.Files;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileLocator implements ResourceLocator {
   private File baseDir;

   public FileLocator() {
      this.baseDir = new File(".");
   }

   public FileLocator(File baseDir) throws FileNotFoundException {
      if (!baseDir.exists()) {
         throw new FileNotFoundException(String.format("Specified baseDir [%s] does not exist", baseDir.getAbsolutePath()));
      } else {
         this.baseDir = baseDir;
      }
   }

   private File resolveFileName(String name) {
      File f = new File(name);
      return f.isAbsolute() ? f : new File(this.baseDir, name);
   }

   public String getString(String name, Charset encoding, JinjavaInterpreter interpreter) throws IOException {
      File file = this.resolveFileName(name);
      if (file.exists() && file.isFile()) {
         return Files.toString(file, encoding);
      } else {
         throw new ResourceNotFoundException("Couldn't find resource: " + file);
      }
   }
}
