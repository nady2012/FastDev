package com.mamba.mambasdk.video;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

import static com.mamba.mambasdk.video.MVideoCacheConfig.DEFAULT_CONFIG;

/**
 * Created by shijunfeng on 2018/5/25.
 */
public class MVideoCacheWrapper {

    private HttpProxyCacheServer proxy;


    public static MVideoCacheWrapper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public HttpProxyCacheServer getDefaultProxy(Context context) {
        return proxy == null ? (proxy = defaultProxy(context)) : proxy;
    }

    public HttpProxyCacheServer getProxy(Context context, MVideoCacheConfig config) {
        return proxy == null ? (proxy = newProxy(context, config)) : proxy;
    }

    public HttpProxyCacheServer getProxy(Context context) {
        return proxy == null ? (proxy = newProxy(context)) : proxy;
    }

    private HttpProxyCacheServer newProxy(Context context, MVideoCacheConfig config) {
        return new HttpProxyCacheServer.Builder(context.getApplicationContext())
            .maxCacheFilesCount(config.maxCacheFileCount)
            .cacheDirectory(config.cacheRoot)
            .diskUsage(config.diskUsage)
            .fileNameGenerator(config.fileNameGenerator)
            .maxCacheSize(config.maxCacheSize)
            .build();
    }

    private HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer.Builder(context.getApplicationContext())
            .build();
    }


    private HttpProxyCacheServer defaultProxy(Context context) {
        return new HttpProxyCacheServer.Builder(context.getApplicationContext())
            .maxCacheFilesCount(DEFAULT_CONFIG.maxCacheFileCount)
            .cacheDirectory(DEFAULT_CONFIG.cacheRoot)
            .diskUsage(DEFAULT_CONFIG.diskUsage)
            .fileNameGenerator(DEFAULT_CONFIG.fileNameGenerator)
            .maxCacheSize(DEFAULT_CONFIG.maxCacheSize)
            .build();
    }

    private static class SingletonHolder {
        private static final MVideoCacheWrapper INSTANCE = new MVideoCacheWrapper();
    }

}
