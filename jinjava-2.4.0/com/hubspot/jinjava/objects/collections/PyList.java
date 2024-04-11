package com.hubspot.jinjava.objects.collections;

import com.google.common.collect.ForwardingList;
import com.hubspot.jinjava.objects.PyWrapper;
import java.util.List;

public class PyList extends ForwardingList<Object> implements PyWrapper {
   private List<Object> list;

   public PyList(List<Object> list) {
      this.list = list;
   }

   protected List<Object> delegate() {
      return this.list;
   }

   public List<Object> toList() {
      return this.list;
   }

   public boolean append(Object e) {
      return this.add(e);
   }
}
