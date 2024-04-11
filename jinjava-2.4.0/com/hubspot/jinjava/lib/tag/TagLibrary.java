package com.hubspot.jinjava.lib.tag;

import com.hubspot.jinjava.lib.SimpleLibrary;
import java.util.Set;

public class TagLibrary extends SimpleLibrary<Tag> {
   public TagLibrary(boolean registerDefaults, Set<String> disabled) {
      super(registerDefaults, disabled);
   }

   protected void registerDefaults() {
      this.registerClasses(new Class[]{AutoEscapeTag.class, BlockTag.class, CallTag.class, CycleTag.class, ElseTag.class, ElseIfTag.class, ExtendsTag.class, ForTag.class, FromTag.class, IfTag.class, IfchangedTag.class, ImportTag.class, IncludeTag.class, MacroTag.class, PrintTag.class, RawTag.class, SetTag.class, UnlessTag.class});
   }

   public Tag getTag(String tagName) {
      return (Tag)this.fetch(tagName);
   }

   public void addTag(Tag t) {
      this.register(t);
   }

   public void register(Tag t) {
      super.register(t);
      if (t.getEndTagName() != null) {
         this.register(t.getEndTagName(), new EndTag(t));
      }

   }
}
