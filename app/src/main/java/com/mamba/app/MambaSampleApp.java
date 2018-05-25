package com.mamba.app;

import android.app.Application;

import com.mamba.mambasdk.video.MVideoCacheWrapper;

/**
 * Created by shijunfeng on 2018/5/25.
 */
public class MambaSampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MVideoCacheWrapper.getInstance().getDefaultProxy(this);

    }
}
