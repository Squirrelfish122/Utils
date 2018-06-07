package com.hyman.zhh.utils.secret;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
public class AESUtils {


    private static final String TAG = "AESUtils";
    private static final boolean mPrintLog = false;
    private static final boolean mPrintLogMore = false;
    private static final String CIPHER_AES = "AES/CBC/PKCS5PADDING";
    private static final String ALGORITHM_AES = "AES";

    private static final String UTF_ENCODE_NAME = "UTF-8";

    private static final String KEY_CREATOR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static int maxLength = KEY_CREATOR.length();
    private static Random mRandom = new Random();

    /**
     * 生成16位的AES秘钥
     *
     * @return
     */
    public static String getAESKey() {
        char[] chars = new char[16];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = getCharKey();
        }
        return new String(chars);
    }

    private static char getCharKey() {
        return KEY_CREATOR.charAt(mRandom.nextInt(maxLength));
    }

    @Nullable
    public static String encryptWithAES(String key, String value) {
        return encryptWithAES(key, null, value);
    }

    /**
     * 使用aes进行加密
     *
     * @param key        随机字符串，必须为16位
     * @param initVector 随机字符串，必须为16位
     * @param value      待加密的字符串
     * @return 加密后的字符串
     */
    @Nullable
    public static String encryptWithAES(String key, String initVector, String value) {
        try {
            byte[] initVectorBytes = initVector != null ? initVector.getBytes(UTF_ENCODE_NAME) : null;
            byte[] encrypted = encryptWithAES(key.getBytes(UTF_ENCODE_NAME),
                    initVectorBytes, value.getBytes(UTF_ENCODE_NAME));
            if (encrypted != null) {
                return Converter.bytes2String(encrypted);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static byte[] encryptWithAES(byte[] key, byte[] initVector, byte[] value) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM_AES);
            Cipher cipher = Cipher.getInstance(CIPHER_AES);
            if (initVector != null) {
                IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector);
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            }
            return cipher.doFinal(value);
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

    @Nullable
    public static String decryptWithAES(String key, String value) {
        return decryptWithAES(key, null, value);
    }

    /**
     * @param key        必须为16位
     * @param initVector 必须为16位
     * @param value      待解密的字符串
     * @return 解密后的字符串
     */
    @Nullable
    public static String decryptWithAES(String key, String initVector, String value) {
        try {
            byte[] initVectorBytes = initVector != null ? initVector.getBytes(UTF_ENCODE_NAME) : null;
            byte[] encrypted = decryptWithAES(key.getBytes(UTF_ENCODE_NAME),
                    initVectorBytes, Converter.string2Bytes(value));
            if (encrypted != null) {
                return new String(encrypted);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static byte[] decryptWithAES(byte[] key, byte[] initVector, byte[] value) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM_AES);
            Cipher cipher = Cipher.getInstance(CIPHER_AES);
            if (initVector != null) {
                IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector);
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            }
            return cipher.doFinal(value);
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
