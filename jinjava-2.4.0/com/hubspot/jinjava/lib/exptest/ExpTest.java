package com.hubspot.jinjava.lib.exptest;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.Importable;

public interface ExpTest extends Importable {
   boolean evaluate(Object var1, JinjavaInterpreter var2, Object... var3);
}
