/*
 * Project: yq-mod-common
 *
 * File Created at 2017年11月28日
 *
 * Copyright 2016 CMCC Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * ZYHY Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license.
 */
package com.yyb.gui.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * The Class DESEncrypt.
 *
 * @author zmcc
 * @version
 * @Type DESEncrypt.java
 * @Desc
 * @date 2016年10月21日 下午2:40:26
 */

/**
 * @author zmcc
 */
public class DESEncrypt {

    /**
     * The iv.
     */
    private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

    /**
     * The encrypt cipher.
     */
    private static Cipher encryptCipher;

    /**
     * The decrypt cipher.
     */
    private static Cipher decryptCipher;

    /**
     * Creates the key bytes.
     *
     * @return the byte[]
     */
    private static byte[] createKeyBytes() {
        byte[] keys = new byte[16];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = (byte) (8 + 2 * i);
        }
        return keys;
    }

    static {
        try {
            byte[] keys = createKeyBytes();
            Key key = getKey(keys);
            IvParameterSpec zeroIv = new IvParameterSpec(iv);

            encryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);

            decryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Byte arr 2 hex str.
     *
     * @param arrB the arr B
     * @return the string
     * @throws Exception the exception
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // add 0 at the begin of number if it is smaller than 0F
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * Hex str 2 byte arr.
     *
     * @param strIn the str in
     * @return the byte[]
     * @throws Exception the exception
     */
    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * Encrypt.
     *
     * @param arrB the arr B
     * @return the byte[]
     * @throws Exception the exception
     */
    private static byte[] encrypt(byte[] arrB) throws Exception {
        synchronized (encryptCipher) {
            return encryptCipher.doFinal(arrB);
        }
    }

    /**
     * Encrypt.
     *
     * @param strIn the str in
     * @return the string
     * @throws Exception the exception
     */
    public static String encrypt(String strIn) {
        try {
            return byteArr2HexStr(encrypt(strIn.getBytes()));
        } catch (Exception e) {
            // TODO: handle exception
        }

        return strIn;
    }

    /**
     * Decrypt.
     *
     * @param arrB the arr B
     * @return the byte[]
     * @throws Exception the exception
     */
    public static byte[] decrypt(byte[] arrB) throws Exception {
        synchronized (decryptCipher) {
            return decryptCipher.doFinal(arrB);
        }
    }

    /**
     * Decrypt.
     *
     * @param strIn the str in
     * @return the string
     * @throws Exception the exception
     */
    public static String decrypt(String strIn) {
        try {
            return new String(decrypt(hexStr2ByteArr(strIn)));
        } catch (Exception e) {
            // TODO: handle exception
        }

        return strIn;

    }

    /**
     * Gets the key.
     *
     * @param arrBTmp the arr B tmp
     * @return the key
     * @throws Exception the exception
     */
    private static Key getKey(byte[] arrBTmp) throws Exception {
        // create an empty btye array
        byte[] arrB = new byte[8];

        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }

        // create the key
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

        return key;
    }

//    public static void main(String[] args) {
//        String str = "87F61BD1738A0A35227F0021FC003B69C0A270BA1DD43989";
//        System.out.println(DESEncrypt.encrypt(str));
//        String decry = DESEncrypt.decrypt("87cc49ce82db1313acd51ad3fde3fef0c9c70361be9118da7165c14d3aef2ce1af3774db7b5f640742c981db1ab2b5825a584cc1f71da3f3");
//        System.out.println(decry.equals(str));
//    }
}

/**
 * Revision history
 * -------------------------------------------------------------------------
 * <p>
 * Date Author Note
 * -------------------------------------------------------------------------
 * 2017年11月28日 Lenovo creat
 */
