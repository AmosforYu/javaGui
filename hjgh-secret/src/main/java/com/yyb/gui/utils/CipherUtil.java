package com.yyb.gui.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * The Class CipherUtil.
 */
public class CipherUtil {

    /**
     * method is "AES/ECB/PKCSSPadding".
     *
     * @param key  the key
     * @param data the data
     * @return the string
     * @throws Exception the exception
     */
    public static String deCiper(String key, String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] raw = key.getBytes("utf-8");
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] original = cipher.doFinal(new Base64().decode(data));
        return new String(original, StandardCharsets.UTF_8);
    }

    /**
     * En ciper.
     *
     * @param key  the key
     * @param data the data
     * @return the string
     * @throws Exception the exception
     */
    public static String enCiper(String key, String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] raw = key.getBytes("utf-8");
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] original = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return new Base64().encodeAsString(original);
    }


}
