package com.hyman.zhh.utils.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Process;

import com.hyman.zhh.utils.log.PrintLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BitmapUtils {

    private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINA);

    private static final String CAPTURE_FILE_NAME = "capture";
    private static final String SUFFIX_NAME = ".png";
    private static final String TAG = "BitmapUtils";
    private static final int MAX_FILE_COUNT = 100;
    private static final int MIN_USABLE_SIZE = 1024 * 1024 * 10;    // 10M

    public static boolean saveBitmap2File(Context context, Bitmap bitmap) {
        PrintLog.d(TAG, "saveBitmap2File");
        if (context == null || bitmap == null) {
            PrintLog.d(TAG, "context is null or bitmap is null");
            return false;
        }
        // data/data/package_name/cache/capture;
        File rootPath = getSavePath(context);
        if (!canSavePng(rootPath)) {
            PrintLog.d(TAG, "can not save png");
            return false;
        }
        // pid_yyyy-MM-dd_HH-mm-ss.png
        String fileName = getFileName();
        File file = new File(rootPath, fileName);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            PrintLog.d(TAG, "save success, path = " + file.getAbsolutePath() + ", length = " + file.length());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static String getFileName() {
        int pid = Process.myPid();
        return pid + "_" + mSimpleDateFormat.format(new Date()) + SUFFIX_NAME;
    }

    private static File getSavePath(Context context) {
        File rootFile = new File(context.getCacheDir(), CAPTURE_FILE_NAME);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        return rootFile;
    }

    private static boolean canSavePng(File saveRoot) {
        if (saveRoot.length() <= 0) {
            PrintLog.d(TAG, "save root is empty");
            return true;
        }

        long totalSpace = saveRoot.getTotalSpace();
        long usableSpace = saveRoot.getUsableSpace();
        PrintLog.d(TAG, "total = " + totalSpace + ", usable = " + usableSpace);

        String[] list = saveRoot.list();
        PrintLog.d(TAG, "list length = " + list.length);

        // 不得小于10M并且最多保存100个文件
        return usableSpace >= MIN_USABLE_SIZE && list.length <= MAX_FILE_COUNT;
    }

    public static Bitmap decodeABGR8888ToBitmap(byte[] rawAbgr, int srcWidth, int srcHeight) {
        int[] colors = convertABGR8888ByteToARGB8888Int(rawAbgr);
        if (colors == null) {
            PrintLog.d(TAG, "colors == null");
            return null;
        }
        return Bitmap.createBitmap(colors, 0, srcWidth, srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
    }

    private static int[] convertABGR8888ByteToARGB8888Int(byte[] data) {
        int size = data.length;
        PrintLog.d(TAG, "convertABGR8888ToARGB8888Int...data.length = " + size);
        if (size == 0) {
            return null;
        }

        int pixelCount = size / 4;
        int[] color = new int[pixelCount];
        byte alpha, red, green, blue;

        // ABGR_8888格式的byte数组，每4个byte代表一个像素点，其中的顺序为: [B][G][R][A]
        for (int i = 0; i < pixelCount; ++i) {
            blue = data[i * 4];
            green = data[i * 4 + 1];
            red = data[i * 4 + 2];
            alpha = data[i * 4 + 3];

            // ARGB_8888格式的byte数组，每4个byte代表一个像素点，其中的顺序为: [R][G][B][A]
            // int color = (A & 0xff) << 24 | (B & 0xff) << 16 | (G & 0xff) << 8 | (R & 0xff);
            color[i] = (alpha & 0xff) << 24 | (blue & 0xff) << 16 | (green & 0xff) << 8 | (red & 0xff);
        }
        return color;
    }

}
