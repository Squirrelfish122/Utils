package com.hyman.zhh.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hyman.zhh.utils.network.NetworkListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class MainActivity extends Activity {

    private ImageView mImageView;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.main_image);



    }

    @Override
    protected void onResume() {
        super.onResume();
        String testUrl = "https://game.gtimg.cn/upload/adw/image/20181113/720a0b2ba6d30dbac2639543354f4205.jpg";
//        String testUrl = "";
        downloadImage(testUrl, mImageView,true, 40, new NetworkListener<Bitmap>() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                Log.d(TAG, "on success");
//                mImageView.setImageBitmap(bitmap);
            }

            @Override
            public void onFail(String reason) {
                Log.d(TAG, "on fail, reason = " + reason);
            }
        });
    }

    public void downloadImage(String url, final ImageView imageView, final boolean rounded,
                              final int radiusPx, final NetworkListener<Bitmap> listener) {
        Log.d(TAG, "downloadImage, url = " + url);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(url, imageView, options, new ImageLoadingListener() {

            private boolean executed = false;

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Log.d(TAG, "onLoadingStarted, url = " + imageUri);
                executed = false;
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.d(TAG, "onLoadingFailed, url = " + imageUri + ", reason = " + failReason);
                if (executed) {
                    return;
                }
                executed = true;
                if (listener != null && !listener.isCancel()) {
                    listener.onFail("download failed");
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Log.d(TAG, "onLoadingComplete, url = " + imageUri);
                if (executed) {
                    return;
                }
                executed = true;
                if (loadedImage != null) {
                    Log.d(TAG, "width = " + loadedImage.getWidth() + ", height = " + loadedImage.getHeight());
                    Log.d(TAG, "view width = " + view.getWidth() + ", height = " + view.getHeight());

                    if (rounded) {
                        int width = view.getWidth();
                        loadedImage = resizeImage(loadedImage, width, view.getHeight());
                        Log.d(TAG, "width = " + loadedImage.getWidth() + ", height = " + loadedImage.getHeight());
                        loadedImage = createRoundedBitmap(loadedImage, width/ 5);
                        Log.d(TAG, "width = " + loadedImage.getWidth() + ", height = " + loadedImage.getHeight());
                        imageView.setImageBitmap(loadedImage);
                    }

                }

                if (listener != null && !listener.isCancel()) {
                    //                    Bitmap temp = loadedImage;
                    //                    if (rounded) {
                    //                        temp = createRoundedBitmap(loadedImage, radiusPx);
                    //                    }
                    listener.onSuccess(loadedImage);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Log.d(TAG, "onLoadingCancelled, url = " + imageUri);
                if (executed) {
                    return;
                }
                executed = true;
                if (listener != null && !listener.isCancel()) {
                    listener.onFail("download canceled");
                }
            }
        });

    }

    public Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    private Bitmap createRoundedBitmap(Bitmap bitmap, int radius) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Log.d(TAG, "radius = " + radius);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        RectF rectF = new RectF(0, 0, width, height);
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return target;
    }
}
