/**
 * Copyright (C) 2014-2015 CMCC All rights reserved
 */
package com.fastdev.fastdevlib.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.File;

/**
 * 图片管理
 * Date: 2015-10-16
 *
 * @author mashanshan
 */
public class ImageLoaderManager {

    public static String URL_PATH_SYMBOL = "http";
    public static String LOCAL_URL_PATH_SYMBOL = "file://";

    /**
     * 本地/网络图片的实际地址
     *
     * @param url 本地/网络图片地址
     * @return
     */
    public static String getImageLoadPath(String url) {
        String newUrl = url;
        if (url != null && url.toLowerCase().startsWith(LOCAL_URL_PATH_SYMBOL)) {
            newUrl = url.substring(LOCAL_URL_PATH_SYMBOL.length());
        }
        return newUrl;
    }

    /**
     * 同步获取图片
     *
     * @param url 图片地址
     * @return
     */
    public static Bitmap getSyncImage(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String newUrl = url;
        if (!TextUtils.isEmpty(url) && new File(url).exists()) {
            newUrl = LOCAL_URL_PATH_SYMBOL + url;
        }
        return ImageLoader.getInstance().loadImageSync(newUrl);
    }

    /**
     * 根据原图和变长绘制圆形图片
     *
     * @param source
     * @return
     */
    public static Bitmap createCircleImage(Bitmap source) {
        if (source == null) {
            return null;
        }
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        int min = source.getWidth();
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        // 绘制圆形
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        //使用SRC_IN
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //绘制图片
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 缓存下载图片
     *
     * @param url 图片地址
     */
    public static void loadImage(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ImageLoader.getInstance().loadImage(url, null);
    }

    /**
     * 带回调的缓存图片
     */
    public static void loadImage(String url, ImageLoadingListener imageLoadingListener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        //本地图片
        if (!url.toLowerCase().startsWith(URL_PATH_SYMBOL)) {
            String newUrl = LOCAL_URL_PATH_SYMBOL + url;
            ImageLoader.getInstance().loadImage(newUrl, imageLoadingListener);
        } else {//网络图片
            ImageLoader.getInstance().loadImage(url, imageLoadingListener);
        }
    }

    /**
     * 删除缓存的图片
     *
     * @param url 缓存的图片的地址
     */
    public static void removeMemoryCache(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        DiskCacheUtils.removeFromCache(url, ImageLoader.getInstance().getDiskCache());
        MemoryCacheUtils.removeFromCache(url, ImageLoader.getInstance().getMemoryCache());
    }

    /**
     * 显示url地址的图片到targetView
     *
     * @param url            本地/网络图片地址
     * @param targetView     目标View
     * @param displayOptions 图片配置
     */
    public static void displayImage(String url, ImageView targetView, DisplayImageOptions displayOptions) {
        String newUrl = url;
        if (!TextUtils.isEmpty(url) && new File(url).exists()) {
            newUrl = LOCAL_URL_PATH_SYMBOL + url;
        }
        if (displayOptions == null) {
            ImageLoader.getInstance().displayImage(newUrl, targetView);
        } else {
            ImageLoader.getInstance().displayImage(newUrl, targetView, displayOptions);
        }
    }


    /**
     * 设置回调参数
     *
     * @param url            本地/网络图片地址
     * @param targetView     目标View
     * @param displayOptions 图片配置
     * @param listener       监听器
     */
    public static void displayImage(String url, ImageView targetView, DisplayImageOptions displayOptions, ImageLoadingListener listener) {
        String newUrl = url;
        if (!TextUtils.isEmpty(url) && new File(url).exists()) {
            newUrl = LOCAL_URL_PATH_SYMBOL + url;
        }
        if (displayOptions == null) {
            ImageLoader.getInstance().displayImage(newUrl, targetView, listener);
        } else {
            ImageLoader.getInstance().displayImage(newUrl, targetView, displayOptions, listener);
        }
    }

    public static void init(Context context, int cacheSize) {
        try {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context.getApplicationContext())
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .diskCacheSize(cacheSize)
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .defaultDisplayImageOptions(new DisplayImageOptions.Builder()
                                    .cacheInMemory(true)
                                    .cacheOnDisk(true)
                                    .considerExifParams(true)
                                    .bitmapConfig(Bitmap.Config.RGB_565)
                                    .build())
                    .build();

            ImageLoader.getInstance().init(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
