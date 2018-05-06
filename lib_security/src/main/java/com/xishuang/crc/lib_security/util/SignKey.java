package com.xishuang.crc.lib_security.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

/**
 * Author:xishuang
 * Date:2018.05.06
 * Des:根据导入的应用签名，读取其中的密钥对和证书
 */
public class SignKey {
    //应用签名
    private static final String keystoreName = "testkey.keystore";
    private static final String keystorePassword = "123456";
    //应用签名的别名
    private static final String alias = "key0";
    private static final String aliasPassword = "123456";

    /**
     * 获取签名的密钥对，用来给密钥加密
     */
    public static KeyPair getSignKeyPair() {
        try {
            File storeFile = new File(keystoreName);
            if (!storeFile.exists()) {
                throw new IllegalArgumentException("还没设置签名文件!");
            }
            String keyStoreType = "JKS";
            char[] keystorepasswd = keystorePassword.toCharArray();
            char[] keyaliaspasswd = aliasPassword.toCharArray();
            KeyStore keystore = KeyStore.getInstance(keyStoreType);
            keystore.load(new FileInputStream(storeFile), keystorepasswd);

            Key key = keystore.getKey(alias, keyaliaspasswd);
            if (key instanceof PrivateKey) {
                Certificate cert = keystore.getCertificate(alias);
                PublicKey publicKey = cert.getPublicKey();
                return new KeyPair(publicKey, (PrivateKey) key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
