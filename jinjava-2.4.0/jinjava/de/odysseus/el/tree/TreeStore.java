package jinjava.de.odysseus.el.tree;

public class TreeStore {
   private final TreeCache cache;
   private final TreeBuilder builder;

   public TreeStore(TreeBuilder builder, TreeCache cache) {
      this.builder = builder;
      this.cache = cache;
   }

   public TreeBuilder getBuilder() {
      return this.builder;
   }

   public Tree get(String expression) throws TreeBuilderException {
      if (this.cache == null) {
         return this.builder.build(expression);
      } else {
         Tree tree = this.cache.get(expression);
         if (tree == null) {
            this.cache.put(expression, tree = this.builder.build(expression));
         }

         return tree;
      }
   }
}
