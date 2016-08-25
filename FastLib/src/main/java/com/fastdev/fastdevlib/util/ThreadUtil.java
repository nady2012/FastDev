package com.fastdev.fastdevlib.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {
    private static final int MAX_POOL_SIZE = 5;
    private static ThreadUtil instance;
    private ExecutorService threadPool = Executors.newFixedThreadPool(MAX_POOL_SIZE);

    public ThreadUtil() {
        if (null == threadPool) {
            threadPool = Executors.newFixedThreadPool(MAX_POOL_SIZE);
        }
    }

    public synchronized static ThreadUtil getInstance() {
        if (null == instance) {
            instance = new ThreadUtil();
        }
        return instance;
    }

    public synchronized boolean submitTask(Runnable runnable) {
        if (null == runnable) {
            return false;
        }
        if (null == threadPool) {
            threadPool = Executors.newFixedThreadPool(MAX_POOL_SIZE);
        }
        threadPool.submit(runnable);
        return true;
    }

    public synchronized void shutdown() {
        if (null == threadPool || threadPool.isShutdown()) {
            return;
        }
        try {
            threadPool.shutdown();
            threadPool = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized Handler getHandlerThread(String threadName) {
        if (null == threadName) {

        }
        HandlerThread handlerThread = new HandlerThread(TextUtils.isEmpty(threadName) ? "uc" : threadName);
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        return handler;
    }

    public synchronized void quitHandlerThread(Looper looper) {
        if (null == looper) {
            return;
        }
        try {
            looper.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
