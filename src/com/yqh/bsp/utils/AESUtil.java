package com.yqh.bsp.utils;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.yqh.bsp.base.config.AppConfig;

public class AESUtil {
	
	public static SecretKeySpec generateMySQLAESKey(final String key, final String encoding) {
        try {
            final byte[] finalKey = new byte[16];
            int i = 0;
            for(byte b : key.getBytes(encoding))
                finalKey[i++%16] ^= b;          
            return new SecretKeySpec(finalKey, "AES");
        } catch(UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
	
	public static String decrypt(String enText) {
		String text="";
		Cipher decryptCipher;
		try {
			decryptCipher = Cipher.getInstance("AES");                        
		  decryptCipher.init(Cipher.DECRYPT_MODE, generateMySQLAESKey(AppConfig.ENCRYPT_SLAT, "UTF-8"));
		    text = new String(decryptCipher.doFinal(Hex.decodeHex(enText.toCharArray())));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return text;
	}
   
    
	public static String encrypt(String text) {
		String enText="";
	    Cipher encryptCipher;
		try {
			encryptCipher = Cipher.getInstance("AES");
			encryptCipher.init(Cipher.ENCRYPT_MODE, generateMySQLAESKey(AppConfig.ENCRYPT_SLAT, "UTF-8")); 
			enText = new String(Hex.encodeHex(encryptCipher.doFinal(text.getBytes("UTF-8"))));
		} catch (Exception e) {
			e.printStackTrace();
		}                        
	        
	    return enText;
//	    System.out.println(String.format("Select aes_decrypt(unhex('%s'), 'simu800');", new String(Hex.encodeHex(encryptCipher.doFinal("张龙古龙".getBytes("UTF-8")))))); 

	}
	
	public static void main(String[] args) {
		System.out.println(encrypt("890.1"));
	}
  


}
