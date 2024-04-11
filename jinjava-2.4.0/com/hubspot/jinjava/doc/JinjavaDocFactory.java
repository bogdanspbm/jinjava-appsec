package com.hubspot.jinjava.doc;

import com.google.common.base.Throwables;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.doc.annotations.JinjavaMetaValue;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.lib.exptest.ExpTest;
import com.hubspot.jinjava.lib.filter.Filter;
import com.hubspot.jinjava.lib.fn.ELFunctionDefinition;
import com.hubspot.jinjava.lib.fn.InjectedContextFunctionProxy;
import com.hubspot.jinjava.lib.tag.EndTag;
import com.hubspot.jinjava.lib.tag.Tag;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JinjavaDocFactory {
   private static final Logger LOG = LoggerFactory.getLogger(JinjavaDocFactory.class);
   private final Jinjava jinjava;

   public JinjavaDocFactory(Jinjava jinjava) {
      this.jinjava = jinjava;
   }

   public JinjavaDoc get() {
      JinjavaDoc doc = new JinjavaDoc();
      this.addExpTests(doc);
      this.addFilterDocs(doc);
      this.addFnDocs(doc);
      this.addTagDocs(doc);
      return doc;
   }

   private void addExpTests(JinjavaDoc doc) {
      Iterator var2 = this.jinjava.getGlobalContext().getAllExpTests().iterator();

      while(var2.hasNext()) {
         ExpTest t = (ExpTest)var2.next();
         com.hubspot.jinjava.doc.annotations.JinjavaDoc docAnnotation = (com.hubspot.jinjava.doc.annotations.JinjavaDoc)t.getClass().getAnnotation(com.hubspot.jinjava.doc.annotations.JinjavaDoc.class);
         if (docAnnotation == null) {
            LOG.warn("Expression Test {} doesn't have a @{} annotation", t.getName(), com.hubspot.jinjava.doc.annotations.JinjavaDoc.class.getName());
            doc.addExpTest(new JinjavaDocExpTest(t.getName(), "", "", false, new JinjavaDocParam[0], new JinjavaDocSnippet[0], Collections.emptyMap()));
         } else if (!docAnnotation.hidden()) {
            doc.addExpTest(new JinjavaDocExpTest(t.getName(), docAnnotation.value(), docAnnotation.aliasOf(), docAnnotation.deprecated(), this.extractParams(docAnnotation.params()), this.extractSnippets(docAnnotation.snippets()), this.extractMeta(docAnnotation.meta())));
         }
      }

   }

   private void addFilterDocs(JinjavaDoc doc) {
      Iterator var2 = this.jinjava.getGlobalContext().getAllFilters().iterator();

      while(var2.hasNext()) {
         Filter f = (Filter)var2.next();
         com.hubspot.jinjava.doc.annotations.JinjavaDoc docAnnotation = (com.hubspot.jinjava.doc.annotations.JinjavaDoc)f.getClass().getAnnotation(com.hubspot.jinjava.doc.annotations.JinjavaDoc.class);
         if (docAnnotation == null) {
            LOG.warn("Filter {} doesn't have a @{} annotation", f.getClass(), com.hubspot.jinjava.doc.annotations.JinjavaDoc.class.getName());
            doc.addFilter(new JinjavaDocFilter(f.getName(), "", "", false, new JinjavaDocParam[0], new JinjavaDocSnippet[0], Collections.emptyMap()));
         } else if (!docAnnotation.hidden()) {
            doc.addFilter(new JinjavaDocFilter(f.getName(), docAnnotation.value(), docAnnotation.aliasOf(), docAnnotation.deprecated(), this.extractParams(docAnnotation.params()), this.extractSnippets(docAnnotation.snippets()), this.extractMeta(docAnnotation.meta())));
         }
      }

   }

   private void addFnDocs(JinjavaDoc doc) {
      Iterator var2 = this.jinjava.getGlobalContext().getAllFunctions().iterator();

      while(var2.hasNext()) {
         ELFunctionDefinition fn = (ELFunctionDefinition)var2.next();
         if (StringUtils.isBlank(fn.getNamespace())) {
            Method realMethod = fn.getMethod();
            if (realMethod.getDeclaringClass().getName().contains(InjectedContextFunctionProxy.class.getSimpleName())) {
               try {
                  realMethod = (Method)realMethod.getDeclaringClass().getField("delegate").get((Object)null);
               } catch (Exception var6) {
                  throw Throwables.propagate(var6);
               }
            }

            com.hubspot.jinjava.doc.annotations.JinjavaDoc docAnnotation = (com.hubspot.jinjava.doc.annotations.JinjavaDoc)realMethod.getAnnotation(com.hubspot.jinjava.doc.annotations.JinjavaDoc.class);
            if (docAnnotation == null) {
               LOG.warn("Function {} doesn't have a @{} annotation", fn.getName(), com.hubspot.jinjava.doc.annotations.JinjavaDoc.class.getName());
               doc.addFunction(new JinjavaDocFunction(fn.getLocalName(), "", "", false, new JinjavaDocParam[0], new JinjavaDocSnippet[0], Collections.emptyMap()));
            } else if (!docAnnotation.hidden()) {
               doc.addFunction(new JinjavaDocFunction(fn.getLocalName(), docAnnotation.value(), docAnnotation.aliasOf(), docAnnotation.deprecated(), this.extractParams(docAnnotation.params()), this.extractSnippets(docAnnotation.snippets()), this.extractMeta(docAnnotation.meta())));
            }
         }
      }

   }

   private void addTagDocs(JinjavaDoc doc) {
      Iterator var2 = this.jinjava.getGlobalContext().getAllTags().iterator();

      while(var2.hasNext()) {
         Tag t = (Tag)var2.next();
         if (!(t instanceof EndTag)) {
            com.hubspot.jinjava.doc.annotations.JinjavaDoc docAnnotation = (com.hubspot.jinjava.doc.annotations.JinjavaDoc)t.getClass().getAnnotation(com.hubspot.jinjava.doc.annotations.JinjavaDoc.class);
            if (docAnnotation == null) {
               LOG.warn("Tag {} doesn't have a @{} annotation", t.getName(), com.hubspot.jinjava.doc.annotations.JinjavaDoc.class.getName());
               doc.addTag(new JinjavaDocTag(t.getName(), StringUtils.isBlank(t.getEndTagName()), "", "", false, new JinjavaDocParam[0], new JinjavaDocSnippet[0], Collections.emptyMap()));
            } else if (!docAnnotation.hidden()) {
               doc.addTag(new JinjavaDocTag(t.getName(), StringUtils.isBlank(t.getEndTagName()), docAnnotation.value(), docAnnotation.aliasOf(), docAnnotation.deprecated(), this.extractParams(docAnnotation.params()), this.extractSnippets(docAnnotation.snippets()), this.extractMeta(docAnnotation.meta())));
            }
         }
      }

   }

   private JinjavaDocParam[] extractParams(JinjavaParam[] params) {
      JinjavaDocParam[] result = new JinjavaDocParam[params.length];

      for(int i = 0; i < params.length; ++i) {
         JinjavaParam p = params[i];
         result[i] = new JinjavaDocParam(p.value(), p.type(), p.desc(), p.defaultValue());
      }

      return result;
   }

   private JinjavaDocSnippet[] extractSnippets(JinjavaSnippet[] snippets) {
      JinjavaDocSnippet[] result = new JinjavaDocSnippet[snippets.length];

      for(int i = 0; i < snippets.length; ++i) {
         JinjavaSnippet s = snippets[i];
         result[i] = new JinjavaDocSnippet(s.desc(), s.code(), s.output());
      }

      return result;
   }

   private Map<String, String> extractMeta(JinjavaMetaValue[] metaValues) {
      Map<String, String> meta = new LinkedHashMap();
      JinjavaMetaValue[] var3 = metaValues;
      int var4 = metaValues.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         JinjavaMetaValue metaValue = var3[var5];
         meta.put(metaValue.name(), metaValue.value());
      }

      return meta;
   }
}
