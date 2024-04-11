package jinjava.de.odysseus.el.tree;

import java.io.Serializable;

public interface TreeBuilder extends Serializable {
   Tree build(String var1) throws TreeBuilderException;
}
