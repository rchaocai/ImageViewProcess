package com.xishuang.imageviewprocess.security;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;

public class DecryptUtil {
    private static final String SIMPLE_KEY_DATA = "testkey.dat";

    /**
     * 获取解密之后的文件流
     */
    public static InputStream onObtainInputStream(Context context) {
        try {
            AssetManager assetmanager = context.getAssets();
            InputStream is = assetmanager.open("encrypt_测试.txt");
            byte[] rawkey = getRawKey(context);

            //使用解密流，数据写出到基础OutputStream之前先对该会先对数据进行解密
            SecretKeySpec skeySpec = new SecretKeySpec(rawkey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            return new CipherInputStream(is, cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取解密之后的文件加密密钥
     */
    private static byte[] getRawKey(Context context) throws Exception {
        //获取应用的签名密钥
        byte[] sign = SignKey.getSign(context);
        PublicKey pubKey = SignKey.getPublicKey(sign);
        //获取加密文件的密钥
        InputStream keyis = context.getAssets().open(SIMPLE_KEY_DATA);
        byte[] key = getData(keyis);
        //解密密钥
        return RSA.decrypt(key, pubKey);
    }


    private static byte[] getData(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int offset;
        while ((offset = is.read(buffer)) != -1) {
            baos.write(buffer, 0, offset);
        }
        return baos.toByteArray();
    }
}
