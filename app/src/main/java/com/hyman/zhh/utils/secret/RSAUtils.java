package com.hyman.zhh.utils.secret;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 */
public class RSAUtils {

    private static final String TAG = "RSAUtils";
    private static final boolean mPrintLog = false;
    private static final boolean mPrintLogMore = false;
    private static final String CIPHER_RSA = "RSA/ECB/PKCS1Padding";
    private static final String ALGORITHM_RSA = "RSA";

    private static final String UTF_ENCODE_NAME = "UTF-8";
    private static final int RSA_KEY_SIZE = 2048;

    /**
     * 生成RSA公钥和密钥
     *
     * @return 返回String数组, length为2。keys[0]为RSAPublicKey的字符串,keys[1]为RSAPrivateKey的字符串.
     */
    @Nullable
    public static String[] generatorRSASecretKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA);
            keyPairGenerator.initialize(RSA_KEY_SIZE, new SecureRandom());

            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            String[] keys = new String[2];
            keys[0] = Converter.bytes2String(publicKey.getEncoded());
            keys[1] = Converter.bytes2String(privateKey.getEncoded());
            return keys;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用rsa public key加密
     *
     * @param key
     * @param value
     * @return
     */
    @Nullable
    public static String encryptByRSAPublicKey(String key, String value) {
        log("encryptByRSAPublicKey,  ");
        log("rsa public key = " + key);
        log("value = " + value);
        try {
            byte[] decryptBytes = encryptByRSAPublicKey(Converter.string2Bytes(key), value.getBytes(UTF_ENCODE_NAME));
            if (decryptBytes != null) {
                String result = Converter.bytes2String(decryptBytes);
                log("encrypt result string = " + result);
                return result;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用rsa public key加密
     *
     * @param key
     * @param value
     * @return
     */
    public static byte[] encryptByRSAPublicKey(byte[] key, byte[] value) {
        logMore("encryptByRSAPublicKey,  ");
        logMore("rsa public key = " + Arrays.toString(key));
        logMore("value = " + Arrays.toString(value));
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            Cipher cipher = Cipher.getInstance(CIPHER_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] result = cipher.doFinal(value);
            logMore("encrypt result = " + Arrays.toString(result));
            return result;
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
     * 使用rsa private key解密
     *
     * @param key
     * @param value
     * @return
     */
    @Nullable
    public static String decryptByRSAPrivateKey(String key, String value) {
        log("decryptByRSAPrivateKey,  ");
        log("rsa private key = " + key);
        log("value = " + value);
        byte[] decryptBytes = decryptByRSAPrivateKey(Converter.string2Bytes(key), Converter.string2Bytes(value));
        if (decryptBytes != null) {
            String result = new String(decryptBytes);
            log("decrypt result string = " + result);
            return result;
        }
        return null;
    }

    /**
     * 使用rsa private key解密
     *
     * @param key
     * @param value
     * @return
     */
    @Nullable
    public static byte[] decryptByRSAPrivateKey(byte[] key, byte[] value) {
        logMore("decryptByRSAPrivateKey,  ");
        logMore("rsa private key = " + Arrays.toString(key));
        logMore("value = " + Arrays.toString(value));
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            Cipher cipher = Cipher.getInstance(CIPHER_RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] result = cipher.doFinal(value);
            logMore("decrypt result = " + Arrays.toString(result));
            return result;
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
     * 使用rsa private key加密
     *
     * @param key
     * @param value
     * @return
     */
    @Nullable
    public static String encryptByRSAPrivateKey(String key, String value) {
        log("encryptByRSAPrivateKey,  ");
        log("rsa private key = " + key);
        log("value = " + value);
        try {
            byte[] decryptBytes = encryptByRSAPrivateKey(Converter.string2Bytes(key), value.getBytes(UTF_ENCODE_NAME));
            if (decryptBytes != null) {
                String result = Converter.bytes2String(decryptBytes);
                log("encrypt result string = " + result);
                return result;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用rsa private key加密
     *
     * @param key
     * @param value
     * @return
     */
    @Nullable
    public static byte[] encryptByRSAPrivateKey(byte[] key, byte[] value) {
        logMore("encryptByRSAPrivateKey,  ");
        logMore("rsa private key = " + Arrays.toString(key));
        logMore("value = " + Arrays.toString(value));
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            Cipher cipher = Cipher.getInstance(CIPHER_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] result = cipher.doFinal(value);
            logMore("encrypt result = " + Arrays.toString(result));
            return result;
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
     * 使用rsa public key解密
     *
     * @param key
     * @param value
     * @return
     */
    @Nullable
    public static String decryptByRSAPublicKey(String key, String value) {
        log("decryptByRSAPublicKey,  ");
        log("rsa public key = " + key);
        log("value = " + value);
        byte[] decryptBytes = decryptByRSAPublicKey(Converter.string2Bytes(key), Converter.string2Bytes(value));
        if (decryptBytes != null) {
            String result = new String(decryptBytes);
            log("decrypt result string = " + result);
            return result;
        }
        return null;
    }

    /**
     * 使用rsa public key解密
     *
     * @param key
     * @param value
     * @return
     */
    @Nullable
    public static byte[] decryptByRSAPublicKey(byte[] key, byte[] value) {
        logMore("decryptByRSAPublicKey,  ");
        logMore("rsa public key = " + Arrays.toString(key));
        logMore("value = " + Arrays.toString(value));
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            Cipher cipher = Cipher.getInstance(CIPHER_RSA);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] result = cipher.doFinal(value);
            logMore("decrypt result = " + Arrays.toString(result));
            return result;
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

    private static void log(String message) {
        if (mPrintLog) {
            Log.d(TAG, message);
        }
    }

    private static void logMore(String message) {
        if (mPrintLogMore) {
            Log.d(TAG, message);
        }
    }
}
