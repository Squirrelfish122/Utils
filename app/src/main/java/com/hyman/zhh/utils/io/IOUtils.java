package com.hyman.zhh.utils.io;

import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

    private IOUtils() {
        throw new AssertionError();
    }


    /**
     * Close closable object and wrap {@link IOException} with {@link
     * RuntimeException}
     *
     * @param closeable closeable object
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            }
        }
    }

    public static void closeAll(Closeable... closeables) {
        for (Closeable c : closeables) {
            close(c);
        }
    }

    public static void closeAllQuietly(Closeable... closeables) {
        for (Closeable c : closeables) {
            closeQuietly(c);
        }
    }


    /**
     * Close closable and hide possible {@link IOException}
     *
     * @param closeable closeable object
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // Ignored
            }
        }
    }


    /**
     * 保存文本
     *
     * @param fileName 文件名字
     * @param content  内容
     * @param append   是否累加
     * @return 是否成功
     */
    public static boolean saveTextValue(String fileName, String content, boolean append) {

        try {
            File textFile = new File(fileName);
            if (!append && textFile.exists()) textFile.delete();

            FileOutputStream os = new FileOutputStream(textFile);
            os.write(content.getBytes("UTF-8"));
            os.close();
        } catch (Exception ee) {
            return false;
        }

        return true;
    }


    /**
     * 删除目录下所有文件
     *
     * @param Path 路径
     */
    public static void deleteAllFile(String Path) {

        // 删除目录下所有文件
        File path = new File(Path);
        File files[] = path.listFiles();
        if (files != null) {
            for (File tfi : files) {
                if (tfi.isDirectory()) {
                    System.out.println(tfi.getName());
                } else {
                    tfi.delete();
                }
            }
        }
    }


    private static final int DOWNLOAD_FILE_BUFFER_SIZE = 8192;  // 8KB
    public static boolean writeFile(InputStream inputStream, String path) {
        if (inputStream == null || TextUtils.isEmpty(path)) {
            return false;
        }

        File file = new File(path);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }

        BufferedOutputStream bufferedOutputStream = null;
        byte[] buffer = new byte[DOWNLOAD_FILE_BUFFER_SIZE];
        int len;
        try {
            bufferedOutputStream = new BufferedOutputStream((new FileOutputStream(file)));
            while ((len = inputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, len);
            }
            bufferedOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(inputStream);
            close(bufferedOutputStream);
        }
        return true;
    }

}
