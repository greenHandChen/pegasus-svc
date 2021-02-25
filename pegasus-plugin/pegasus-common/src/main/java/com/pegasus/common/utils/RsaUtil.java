package com.pegasus.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

/**
 * Created by enHui.Chen on 2020/4/30.
 */
public class RsaUtil {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String RSA = "RSA";

    /**
     * @Author: enHui.Chen
     * @Description: 生成公钥和私钥
     * @Data 2020/4/30
     */
    public static HashMap<String, Object> getKeys() throws NoSuchAlgorithmException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA);
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey rsaPublicKey= (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKey = new String(Base64.encodeBase64(rsaPublicKey.getEncoded()));
        String privateKey = new String(Base64.encodeBase64(rsaPrivateKey.getEncoded()));
        map.put("public", publicKey);
        map.put("private", privateKey);
        return map;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 公钥加密
     * @Data 2020/4/30
     */
    public static String encryptByPublicKey(String target, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(RSA).generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(target.getBytes(DEFAULT_CHARSET)));
    }

    /**
     * @Autho   r: enHui.Chen
     * @Description: 私钥解密
     * @Data 2020/4/30
     */
    public static String decryptByPrivateKey(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes(DEFAULT_CHARSET));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(RSA).generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }
}
