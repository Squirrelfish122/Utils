package com.hyman.zhh.utils;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 *
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(5)//线程池的数量
                .threadPriority(4)
                .memoryCacheSize(10 * 1024 * 1024)//设置内存缓存区大小
                .diskCacheSize(30 * 1024 * 1024)//设置sd卡缓存区大小
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//给缓存的文件名进行md5加密处理
                .build();
        ImageLoader.getInstance().init(configuration);
    }
}
