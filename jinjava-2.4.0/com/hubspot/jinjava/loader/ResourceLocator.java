package com.hubspot.jinjava.loader;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import java.io.IOException;
import java.nio.charset.Charset;

public interface ResourceLocator {
   String getString(String var1, Charset var2, JinjavaInterpreter var3) throws IOException;
}
