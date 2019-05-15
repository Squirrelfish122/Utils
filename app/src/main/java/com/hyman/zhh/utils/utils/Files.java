package com.hyman.zhh.utils.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 *
 */
public class Files {
    
    private static final String TAG = "Files";

    public static void compress(File sourceFile, File desFile, boolean delete) throws IOException {
        LogUtils.d(TAG, "source file length = " + sourceFile.length());
        FileInputStream fileInputStream = new FileInputStream(sourceFile);
        GZIPOutputStream gzipOutputStream  = new GZIPOutputStream(new FileOutputStream(desFile));

        int length;
        byte[] buffer = new byte[1024 * 8];
        while ((length = fileInputStream.read(buffer)) != -1) {
            gzipOutputStream.write(buffer, 0, length);
        }

        // finish()、flush()同时调用，在api<=19版本会抛异常DataFormatException，只能调用finish().
        gzipOutputStream.finish();
        //gzipOutputStream.flush();
        gzipOutputStream.close();
        fileInputStream.close();

        LogUtils.d(TAG, "des file length = " + desFile.length());

        LogUtils.d(TAG, "gzip scale = " + sourceFile.length() * 1.0f / desFile.length());

        if (delete) {
            sourceFile.delete();
        }
    }
}
