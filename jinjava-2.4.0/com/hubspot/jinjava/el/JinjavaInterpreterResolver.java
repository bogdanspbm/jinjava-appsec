package com.hubspot.jinjava.el;

import com.google.common.collect.ImmutableMap;
import com.hubspot.jinjava.el.ext.AbstractCallableMethod;
import com.hubspot.jinjava.el.ext.JinjavaBeanELResolver;
import com.hubspot.jinjava.el.ext.JinjavaListELResolver;
import com.hubspot.jinjava.el.ext.NamedParameter;
import com.hubspot.jinjava.interpret.DisabledException;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.interpret.TemplateError;
import com.hubspot.jinjava.interpret.errorcategory.BasicTemplateErrorCategory;
import com.hubspot.jinjava.objects.PyWrapper;
import com.hubspot.jinjava.objects.collections.PyList;
import com.hubspot.jinjava.objects.collections.PyMap;
import com.hubspot.jinjava.objects.date.FormattedDate;
import com.hubspot.jinjava.objects.date.PyishDate;
import com.hubspot.jinjava.objects.date.StrftimeFormatter;
import com.hubspot.jinjava.util.Logging;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import jinjava.de.odysseus.el.util.SimpleResolver;
import jinjava.javax.el.ArrayELResolver;
import jinjava.javax.el.CompositeELResolver;
import jinjava.javax.el.ELContext;
import jinjava.javax.el.ELResolver;
import jinjava.javax.el.MapELResolver;
import jinjava.javax.el.PropertyNotFoundException;
import jinjava.javax.el.ResourceBundleELResolver;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

public class JinjavaInterpreterResolver extends SimpleResolver {
   private static final ELResolver DEFAULT_RESOLVER_READ_ONLY = new CompositeELResolver() {
      {
         this.add(new ArrayELResolver(true));
         this.add(new JinjavaListELResolver(true));
         this.add(new MapELResolver(true));
         this.add(new ResourceBundleELResolver());
         this.add(new JinjavaBeanELResolver(true));
      }
   };
   private static final ELResolver DEFAULT_RESOLVER_READ_WRITE = new CompositeELResolver() {
      {
         this.add(new ArrayELResolver(false));
         this.add(new JinjavaListELResolver(false));
         this.add(new MapELResolver(false));
         this.add(new ResourceBundleELResolver());
         this.add(new JinjavaBeanELResolver(false));
      }
   };
   private final JinjavaInterpreter interpreter;

   public JinjavaInterpreterResolver(JinjavaInterpreter interpreter) {
      super(interpreter.getConfig().isReadOnlyResolver() ? DEFAULT_RESOLVER_READ_ONLY : DEFAULT_RESOLVER_READ_WRITE);
      this.interpreter = interpreter;
   }

   public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params) {
      try {
         Object methodProperty = this.getValue(context, base, method, false);
         if (methodProperty instanceof AbstractCallableMethod) {
            context.setPropertyResolved(true);
            return ((AbstractCallableMethod)methodProperty).evaluate(params);
         }
      } catch (IllegalArgumentException var7) {
      }

      return super.invoke(context, base, method, paramTypes, this.generateMethodParams(method, params));
   }

   public Object getValue(ELContext context, Object base, Object property) {
      return this.getValue(context, base, property, true);
   }

   private Object[] generateMethodParams(Object method, Object[] astParams) {
      if (!"filter".equals(method)) {
         return astParams;
      } else {
         List<Object> args = new ArrayList();
         Map<String, Object> kwargs = new LinkedHashMap();
         Iterator var5 = Arrays.asList(astParams).subList(2, astParams.length).iterator();

         while(var5.hasNext()) {
            Object param = var5.next();
            if (param instanceof NamedParameter) {
               NamedParameter namedParameter = (NamedParameter)param;
               kwargs.put(namedParameter.getName(), namedParameter.getValue());
            } else {
               args.add(param);
            }
         }

         return new Object[]{astParams[0], astParams[1], args.toArray(), kwargs};
      }
   }

   private Object getValue(ELContext context, Object base, Object property, boolean errOnUnknownProp) {
      String propertyName = Objects.toString(property, "");
      Object value = null;
      this.interpreter.getContext().addResolvedValue(propertyName);
      TemplateError.ErrorItem item = TemplateError.ErrorItem.PROPERTY;

      try {
         if ("____int3rpr3t3r____".equals(property)) {
            value = this.interpreter;
         } else if (propertyName.startsWith("filter:")) {
            item = TemplateError.ErrorItem.FILTER;
            value = this.interpreter.getContext().getFilter(StringUtils.substringAfter(propertyName, "filter:"));
         } else if (propertyName.startsWith("exptest:")) {
            item = TemplateError.ErrorItem.EXPRESSION_TEST;
            value = this.interpreter.getContext().getExpTest(StringUtils.substringAfter(propertyName, "exptest:"));
         } else if (base == null) {
            value = this.interpreter.retraceVariable((String)property, this.interpreter.getLineNumber(), -1);
         } else {
            try {
               Optional optValue;
               if (base instanceof Optional) {
                  optValue = (Optional)base;
                  if (!optValue.isPresent()) {
                     return null;
                  }

                  base = optValue.get();
               }

               value = super.getValue(context, base, propertyName);
               if (value instanceof Optional) {
                  optValue = (Optional)value;
                  if (!optValue.isPresent()) {
                     return null;
                  }

                  value = optValue.get();
               }
            } catch (PropertyNotFoundException var9) {
               if (errOnUnknownProp) {
                  this.interpreter.addError(TemplateError.fromUnknownProperty(base, propertyName, this.interpreter.getLineNumber(), -1));
               }
            }
         }
      } catch (DisabledException var10) {
         this.interpreter.addError(new TemplateError(TemplateError.ErrorType.FATAL, TemplateError.ErrorReason.DISABLED, item, var10.getMessage(), propertyName, this.interpreter.getLineNumber(), -1, var10));
      }

      context.setPropertyResolved(true);
      return this.wrap(value);
   }

   Object wrap(Object value) {
      if (value == null) {
         return null;
      } else if (value instanceof PyWrapper) {
         return value;
      } else if (List.class.isAssignableFrom(value.getClass())) {
         return new PyList((List)value);
      } else if (Map.class.isAssignableFrom(value.getClass())) {
         return new PyMap((Map)value);
      } else if (Date.class.isAssignableFrom(value.getClass())) {
         return new PyishDate(localizeDateTime(this.interpreter, ZonedDateTime.ofInstant(Instant.ofEpochMilli(((Date)value).getTime()), ZoneOffset.UTC)));
      } else if (ZonedDateTime.class.isAssignableFrom(value.getClass())) {
         return new PyishDate(localizeDateTime(this.interpreter, (ZonedDateTime)value));
      } else {
         return FormattedDate.class.isAssignableFrom(value.getClass()) ? formattedDateToString(this.interpreter, (FormattedDate)value) : value;
      }
   }

   private static ZonedDateTime localizeDateTime(JinjavaInterpreter interpreter, ZonedDateTime dt) {
      Logging.ENGINE_LOG.debug("Using timezone: {} to localize datetime: {}", interpreter.getConfig().getTimeZone(), dt);
      return dt.withZoneSameInstant(interpreter.getConfig().getTimeZone());
   }

   private static String formattedDateToString(JinjavaInterpreter interpreter, FormattedDate d) {
      DateTimeFormatter formatter = getFormatter(interpreter, d).withLocale(getLocale(interpreter, d));
      return formatter.format(localizeDateTime(interpreter, d.getDate()));
   }

   private static DateTimeFormatter getFormatter(JinjavaInterpreter interpreter, FormattedDate d) {
      if (!StringUtils.isBlank(d.getFormat())) {
         try {
            return StrftimeFormatter.formatter(d.getFormat(), interpreter.getConfig().getLocale());
         } catch (IllegalArgumentException var3) {
            interpreter.addError(new TemplateError(TemplateError.ErrorType.WARNING, TemplateError.ErrorReason.SYNTAX_ERROR, TemplateError.ErrorItem.OTHER, var3.getMessage(), (String)null, interpreter.getLineNumber(), -1, (Exception)null, BasicTemplateErrorCategory.UNKNOWN_DATE, ImmutableMap.of("date", d.getDate().toString(), "exception", var3.getMessage(), "lineNumber", String.valueOf(interpreter.getLineNumber()))));
         }
      }

      return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
   }

   private static Locale getLocale(JinjavaInterpreter interpreter, FormattedDate d) {
      if (!StringUtils.isBlank(d.getLanguage())) {
         try {
            return LocaleUtils.toLocale(d.getLanguage());
         } catch (IllegalArgumentException var3) {
            interpreter.addError(new TemplateError(TemplateError.ErrorType.WARNING, TemplateError.ErrorReason.SYNTAX_ERROR, TemplateError.ErrorItem.OTHER, var3.getMessage(), (String)null, interpreter.getLineNumber(), -1, (Exception)null, BasicTemplateErrorCategory.UNKNOWN_LOCALE, ImmutableMap.of("date", d.getDate().toString(), "exception", var3.getMessage(), "lineNumber", String.valueOf(interpreter.getLineNumber()))));
         }
      }

      return Locale.US;
   }
}
