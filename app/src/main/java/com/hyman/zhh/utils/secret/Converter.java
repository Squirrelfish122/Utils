package com.hyman.zhh.utils.secret;

import android.util.Base64;

/**
 *
 */
public class Converter {


    /**
     * 将byte[]转换为String
     *
     * @param bytes
     * @return
     */
    public static String bytes2String(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    /**
     * 将String转换为byte[]
     *
     * @param source
     * @return
     */
    public static byte[] string2Bytes(String source) {
        return Base64.decode(source, Base64.NO_WRAP);
    }

}
