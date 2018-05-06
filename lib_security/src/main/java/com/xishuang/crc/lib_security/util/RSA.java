package com.xishuang.crc.lib_security.util;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class RSA {

	public static byte[] encrypt(byte []data, PrivateKey prikey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, prikey);
		return cipher.doFinal(data);
	}
	
	public static byte[] decrypt (byte []data, PublicKey pubkey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, pubkey);
		return cipher.doFinal(data);
	}
}
