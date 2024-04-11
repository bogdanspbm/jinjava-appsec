package jinjava.de.odysseus.el.tree;

public interface FunctionNode extends Node {
   String getName();

   int getIndex();

   int getParamCount();

   boolean isVarArgs();
}
