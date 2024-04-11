package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.lib.SimpleLibrary;
import java.util.Set;

public class FilterLibrary extends SimpleLibrary<Filter> {
   public FilterLibrary(boolean registerDefaults, Set<String> disabled) {
      super(registerDefaults, disabled);
   }

   protected void registerDefaults() {
      this.registerClasses(new Class[]{AttrFilter.class, PrettyPrintFilter.class, DefaultFilter.class, DAliasedDefaultFilter.class, FileSizeFormatFilter.class, UrlizeFilter.class, BatchFilter.class, CountFilter.class, DictSortFilter.class, FirstFilter.class, GroupByFilter.class, JoinFilter.class, LastFilter.class, LengthFilter.class, ListFilter.class, MapFilter.class, RejectAttrFilter.class, RejectFilter.class, SelectFilter.class, SelectAttrFilter.class, SliceFilter.class, ShuffleFilter.class, SortFilter.class, SplitFilter.class, UniqueFilter.class, DatetimeFilter.class, DateTimeFormatFilter.class, UnixTimestampFilter.class, AbsFilter.class, AddFilter.class, BoolFilter.class, CutFilter.class, DivideFilter.class, DivisibleFilter.class, FloatFilter.class, IntFilter.class, Md5Filter.class, MultiplyFilter.class, RandomFilter.class, ReverseFilter.class, RoundFilter.class, SumFilter.class, EscapeFilter.class, EAliasedEscapeFilter.class, EscapeJsFilter.class, ForceEscapeFilter.class, StripTagsFilter.class, UrlEncodeFilter.class, XmlAttrFilter.class, EscapeJsonFilter.class, EscapeJinjavaFilter.class, CapitalizeFilter.class, CenterFilter.class, FormatFilter.class, IndentFilter.class, LowerFilter.class, TruncateFilter.class, TruncateHtmlFilter.class, UpperFilter.class, ReplaceFilter.class, StringFilter.class, SafeFilter.class, TitleFilter.class, TrimFilter.class, WordCountFilter.class, WordWrapFilter.class});
   }

   public Filter getFilter(String filterName) {
      return (Filter)this.fetch(filterName);
   }

   public void addFilter(Filter filter) {
      this.register(filter);
   }
}
