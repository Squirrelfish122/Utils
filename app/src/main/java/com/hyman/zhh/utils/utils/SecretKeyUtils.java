package com.hyman.zhh.utils.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 */
public class SecretKeyUtils {

    private static final String CIPHER = "AES/CBC/PKCS5PADDING";
    private static final String ALGORITHM_AES = "AES";
    private static final String ALGORITHM_RSA = "RSA";

    private static final String UTF_ENCODE_NAME = "UTF-8";

    private static final String KEY_CREATOR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static int maxLength = KEY_CREATOR.length();
    private static Random mRandom = new Random();

    private static final String GET_DATE_FORMAT = "yyyy-MM-dd";
    private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(GET_DATE_FORMAT, Locale.CHINA);

    public static String getTodayStr() {
        return mSimpleDateFormat.format(new Date());
    }

    public static boolean shouldUpdate(String value, int validDays) {
        try {
            Date saveDate = mSimpleDateFormat.parse(value);
            Date currentDate = mSimpleDateFormat.parse(getTodayStr());
            long interval = (currentDate.getTime() - saveDate.getTime()) / 1000;    // ms -> s
            return interval >= validDays * 24 * 60 * 60;
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }

    private static String encodeToString(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private static byte[] decode(String source) {
        return Base64.decode(source, Base64.DEFAULT);
    }

    /**
     * 生成AES秘钥,必须是16位的
     * @return
     */
    public static String getAESKey(){
        char[] chars = new char[16];
        for (int i= 0; i < chars.length; i++) {
            chars[i] = getCharKey();
        }
        return new String(chars);
    }

    private static char getCharKey() {
        return KEY_CREATOR.charAt(mRandom.nextInt(maxLength));
    }

    /**
     *
     * @param key 随机字符串，必须为16位
     * @param initVector 随机字符串，必须为16位
     * @param value 待加密的字符串
     * @return 加密后的字符串
     */
    public static String encryptWithAES(String key, String initVector, String value) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector.getBytes(UTF_ENCODE_NAME));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(UTF_ENCODE_NAME), ALGORITHM_AES);

            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(value.getBytes(UTF_ENCODE_NAME));
            return encodeToString(encrypted);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param key 必须为16位
     * @param initVector 必须为16位
     * @param value 待解密的字符串
     * @return 解密后的字符串
     */
    public static String decryptWithAES(String key, String initVector, String value) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector.getBytes(UTF_ENCODE_NAME));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(UTF_ENCODE_NAME), ALGORITHM_AES);

            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(decode(value));
            return new String(encrypted);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成RSA公钥和密钥
     * @return 返回String数组,length为2。keys[0]为RSAPublicKey的字符串,keys[1]为RSAPrivateKey的字符串.
     */
    public static String[] generatorRSASecretKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA);
            // key size必须是64的倍数
            keyPairGenerator.initialize(1024);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            String[] keys = new String[2];
            keys[0] = encodeToString(publicKey.getEncoded());
            keys[1] = encodeToString(privateKey.getEncoded());
            return keys;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用RSA公钥解密
     * @param publicKeyStr
     * @param value
     * @return
     */
    public static String decryptByRSAPublicKey(String publicKeyStr, String value) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(decode(publicKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] decryptBytes = cipher.doFinal(decode(value));
            return new String(decryptBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用RSAPrivateKey解密
     * @param privateKeyStr
     * @param value
     * @return
     */
    public static String decryptByRSAPrivateKey(String privateKeyStr, String value) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decode(privateKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptBytes = cipher.doFinal(decode(value));
            return new String(decryptBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用RSA公钥加密
     * @param publicKeyStr
     * @param value
     * @return
     */
    public static String encryptByRSAPublicKey(String publicKeyStr, String value) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(decode(publicKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] decryptBytes = cipher.doFinal(value.getBytes(UTF_ENCODE_NAME));
            return encodeToString(decryptBytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 使用RSAPrivateKey加密
     * @param privateKeyStr
     * @param value
     * @return
     */
    public static String encryptByRSAPrivateKey(String privateKeyStr, String value) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decode(privateKeyStr));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] decryptBytes = cipher.doFinal(value.getBytes(UTF_ENCODE_NAME));
            return encodeToString(decryptBytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
