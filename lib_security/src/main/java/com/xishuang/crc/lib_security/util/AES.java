package com.xishuang.crc.lib_security.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private static final String AES = "AES";
    /**
     * AES算法加密文件
     */
    public static void encryptFile(byte[] rawKey, File fromFile, File toFile) throws Exception {
        if (!fromFile.exists()) {
            throw new NullPointerException("文件不存在");
        }
        if (toFile.exists()) {
            toFile.delete();
        }
        SecretKeySpec skeySpec = new SecretKeySpec(rawKey, AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        FileInputStream fis = new FileInputStream(fromFile);
        FileOutputStream fos = new FileOutputStream(toFile, true);
        byte[] buffer = new byte[512 * 1024 - 16];
        int offset;
        //使用加密流来加密
        CipherInputStream bis = new CipherInputStream(fis, cipher);
        while ((offset = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, offset);
            fos.flush();
        }
        fos.close();
        fis.close();
    }

    /**
     * AES算法解密文件
     */
    public static void decryptFile(byte[] rawKey, File fromFile, File toFile) throws Exception {
        if (!fromFile.exists()) {
            throw new NullPointerException("文件不存在");
        }
        if (toFile.exists()) {
            toFile.delete();
        }
        SecretKeySpec skeySpec = new SecretKeySpec(rawKey, AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        FileInputStream fis = new FileInputStream(fromFile);
        FileOutputStream fos = new FileOutputStream(toFile, true);
        byte[] buffer = new byte[512 * 1024 + 16];
        int offset;
        //使用解密流来解密
        CipherInputStream cipherInputStream = new CipherInputStream(fis, cipher);
        while ((offset = cipherInputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, offset);
            fos.flush();
        }
        fos.close();
        fis.close();
    }

    /**
     * 生成用AES算法来加密的密钥流，这个密钥会被应用签名{@link SignKey}的密钥进行二次加密
     */
    public static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        //192 and 256 bits may not be available
        kgen.init(128, sr);
        SecretKey skey = kgen.generateKey();
        return skey.getEncoded();
    }
}
