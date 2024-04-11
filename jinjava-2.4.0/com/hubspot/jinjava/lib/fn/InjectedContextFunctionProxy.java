package com.hubspot.jinjava.lib.fn;

import com.google.common.base.Throwables;
import com.hubspot.jinjava.util.Logging;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class InjectedContextFunctionProxy {
   public static ELFunctionDefinition defineProxy(String namespace, String name, Method m, Object injectedInstance) {
      try {
         ClassPool pool = ClassPool.getDefault();
         String ccName = InjectedContextFunctionProxy.class.getSimpleName() + "$$" + namespace + "$$" + name;
         Class injectedClass = null;

         try {
            injectedClass = InjectedContextFunctionProxy.class.getClassLoader().loadClass(ccName);
         } catch (ClassNotFoundException var18) {
            CtClass cc = pool.makeClass(ccName);
            CtClass mc = pool.get(m.getDeclaringClass().getName());
            CtField injectedField = CtField.make(String.format("public static %s injectedField;", m.getDeclaringClass().getName()), cc);
            cc.addField(injectedField);
            CtField injectedMethod = CtField.make(String.format("public static %s delegate;", Method.class.getName()), cc);
            cc.addField(injectedMethod);
            CtMethod ctMethod = mc.getDeclaredMethod(m.getName());
            CtMethod invokeMethod = CtNewMethod.make(9, ctMethod.getReturnType(), "invoke", ctMethod.getParameterTypes(), ctMethod.getExceptionTypes(), (String)null, cc);
            invokeMethod.setBody("{ return $proceed($$); }", "injectedField", m.getName());
            CtClass[] var14 = ctMethod.getParameterTypes();
            int var15 = var14.length;

            for(int var16 = 0; var16 < var15; ++var16) {
               CtClass param = var14[var16];
               if (param.isArray()) {
                  invokeMethod.setModifiers(invokeMethod.getModifiers() | 128);
                  break;
               }
            }

            cc.addMethod(invokeMethod);
            injectedClass = cc.toClass();
            cc.detach();
         }

         injectedClass.getField("injectedField").set((Object)null, injectedInstance);
         injectedClass.getField("delegate").set((Object)null, m);
         Method staticMethod = null;
         Method[] var20 = injectedClass.getMethods();
         int var21 = var20.length;

         for(int var22 = 0; var22 < var21; ++var22) {
            Method m1 = var20[var22];
            if (m1.getName().equals("invoke")) {
               staticMethod = m1;
               break;
            }
         }

         return new ELFunctionDefinition(namespace, name, staticMethod);
      } catch (Throwable var19) {
         Logging.ENGINE_LOG.error("Error creating injected context function", var19);
         throw Throwables.propagate(var19);
      }
   }
}
