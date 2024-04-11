package com.hubspot.jinjava.lib.filter;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.InterpretException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.util.ForLoop;
import com.hubspot.jinjava.util.ObjectIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@JinjavaDoc(
   value = "Group a sequence of objects by a common attribute.",
   params = {@JinjavaParam(
   value = "value",
   desc = "The dict to iterate through and group by a common attribute"
), @JinjavaParam(
   value = "attribute",
   desc = "The common attribute to group by"
)},
   snippets = {@JinjavaSnippet(
   desc = "If you have a list of dicts or objects that represent persons with gender, first_name and last_name attributes and you want to group all users by genders you can do something like this",
   code = "<ul>\n    {% for group in contents|groupby('blog_post_author') %}\n        <li>{{ group.grouper }}<ul>\n            {% for content in group.list %}\n                <li>{{ content.name }}</li>\n            {% endfor %}</ul></li>\n     {% endfor %}\n</ul>"
)}
)
public class GroupByFilter implements Filter {
   public String getName() {
      return "groupby";
   }

   public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
      if (args.length == 0) {
         throw new InterpretException(this.getName() + " requires an attr name to group on", interpreter.getLineNumber());
      } else {
         String attr = args[0];
         ForLoop loop = ObjectIterator.getLoop(var);
         LinkedListMultimap groupBuckets = LinkedListMultimap.create();

         while(loop.hasNext()) {
            Object val = loop.next();
            String grouper = Objects.toString(interpreter.resolveProperty(val, attr));
            groupBuckets.put(grouper, val);
         }

         List<GroupByFilter.Group> groups = new ArrayList();
         Iterator var12 = groupBuckets.keySet().iterator();

         while(var12.hasNext()) {
            String grouper = (String)var12.next();
            List<Object> list = Lists.newArrayList(groupBuckets.get(grouper));
            groups.add(new GroupByFilter.Group(grouper, list));
         }

         return groups;
      }
   }

   public static class Group {
      private final String grouper;
      private final List<Object> list;

      public Group(String grouper, List<Object> list) {
         this.grouper = grouper;
         this.list = list;
      }

      public String getGrouper() {
         return this.grouper;
      }

      public List<Object> getList() {
         return this.list;
      }
   }
}
