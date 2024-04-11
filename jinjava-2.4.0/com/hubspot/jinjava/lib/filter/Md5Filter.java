package com.hubspot.jinjava.lib.filter;

import com.hubspot.jinjava.doc.annotations.JinjavaDoc;
import com.hubspot.jinjava.doc.annotations.JinjavaParam;
import com.hubspot.jinjava.doc.annotations.JinjavaSnippet;
import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.util.Logging;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@JinjavaDoc(
   value = "Calculates the md5 hash of the given object",
   params = {@JinjavaParam(
   value = "value",
   desc = "Value to get MD5 hash of"
)},
   snippets = {@JinjavaSnippet(
   code = "{{ content.absolute_url|md5 }}"
)}
)
public class Md5Filter implements Filter {
   private static final String[] NOSTR = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
   private static final String MD5 = "MD5";

   private String byteToArrayString(byte bByte) {
      int temp = bByte;
      if (bByte < 0) {
         temp = bByte + 256;
      }

      int iD1 = temp / 16;
      int iD2 = temp % 16;
      return NOSTR[iD1] + NOSTR[iD2];
   }

   private String byteToString(byte[] bByte) {
      StringBuilder sBuffer = new StringBuilder();

      for(int i = 0; i < bByte.length; ++i) {
         sBuffer.append(this.byteToArrayString(bByte[i]));
      }

      return sBuffer.toString();
   }

   private String md5(String str, Charset encoding) {
      String result = null;

      try {
         MessageDigest md = MessageDigest.getInstance("MD5");
         result = this.byteToString(md.digest(str.getBytes(encoding)));
      } catch (NoSuchAlgorithmException var6) {
         Logging.ENGINE_LOG.error(var6.getMessage());
      }

      return result;
   }

   public Object filter(Object object, JinjavaInterpreter interpreter, String... arg) {
      return object instanceof String ? this.md5((String)object, interpreter.getConfig().getCharset()) : object;
   }

   public String getName() {
      return "md5";
   }
}
