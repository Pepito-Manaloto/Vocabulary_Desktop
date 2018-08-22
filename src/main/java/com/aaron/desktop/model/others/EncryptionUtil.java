/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.model.others;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Aaron
 */
public final class EncryptionUtil
{
    private static final String KEY = "Bar12345Bar12345"; // 128 bit key
    private static final String INIT_VECTOR = "RandomInitVector"; // 16 bytes IV

    public static String encrypt(String plainText) throws Exception
    {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        String encryptedString = Base64.getEncoder().encodeToString(encrypted);

        return encryptedString;
    }

    public static String decrypt(String encrypted) throws Exception
    {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

        return new String(original);
    }

    public static void main(String[] args) throws Exception
    {
        String text = "root";

        String encrypted = encrypt(text);
        System.out.println("Encrypted: " + encrypted);

        System.out.println("Decrypted: " + decrypt(encrypted));
    }
}
