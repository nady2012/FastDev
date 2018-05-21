package com.mamba.mambasdk.threadpool;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;


import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * 
 * @包名：com.cmri.prcs.message.threadpool
 * @类名：ThreadPoolManager
 * @描述：线程池管理类
 * @作者：shijunfeng
 * @时间：2015年6月5日下午5:32:53
 * @版本：1.0.0
 *
 */
public class ThreadPoolManager {

    public static final String ACTION_DELETE_TASK_FAILED = "action_delete_task_failed";
    /* 线程池的大小 */
    private int mPoolSize = MAX_POOL_SIZE;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int MIN_POOL_SIZE = 1;
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    /* 线程池 */
    private ExecutorService mThreadPool;

    /* 请求队列 */
    private LinkedList<ThreadPoolTaskBase> mAsyncTasks;
    /* 缓存队列 */
    private LinkedList<ThreadPoolTaskBase> mDelayedTasks;

    /* 工作方式 */
    private int mType = TYPE_FIFO;
    public static final int TYPE_FIFO = 0;
    public static final int TYPE_LIFO = 1;

    /* 轮询线程 */
    private Thread mPoolThread;
    /* 轮询时间 */
    private static final int SLEEP_TIME = 200;

    private static ThreadPoolManager _instance;

    public static ThreadPoolManager getPoolManager() {
        if (null == _instance) {
            /* 默认工作方式&线程池大小 */
            synchronized (ThreadPoolManager.class) {
                if (_instance == null) {
                    _instance = new ThreadPoolManager();
                }
            }
        }
        return _instance;
    }

    private NetConnectionReceiver mNetConnectionReceiver;
    private boolean bRigistered = false;

    public void registerNetStatusReceiver(Context context){
        if (!bRigistered){
            mNetConnectionReceiver = new NetConnectionReceiver(context);
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            context.getApplicationContext().registerReceiver(mNetConnectionReceiver,filter);
            bRigistered = true;
        }
    }

    public void unRegisterNetStatusReceiver(Context context){
        if (null != context && bRigistered && mNetConnectionReceiver != null){
            context.getApplicationContext().unregisterReceiver(mNetConnectionReceiver);
            mNetConnectionReceiver = null;
            bRigistered = false;
        }
    }
    /* 设置线程池工作方式 */
    public void setPoolType(int type) {
        _instance.mType = (type == TYPE_FIFO) ? TYPE_FIFO : TYPE_LIFO;
    }

    /* 设置线程池大小 */
    public void setPoolSize(int poolSize) {
        if (poolSize < MIN_POOL_SIZE){
            poolSize = MIN_POOL_SIZE;
        }

        if (poolSize > MAX_POOL_SIZE){
            poolSize = MAX_POOL_SIZE;
        }
        _instance.mPoolSize = poolSize;
    }

    private ThreadPoolManager(){
        mThreadPool = Executors.newFixedThreadPool(this.mPoolSize);
        mAsyncTasks = new LinkedList<ThreadPoolTaskBase>();
        mDelayedTasks = new LinkedList<ThreadPoolTaskBase>();
    }
    public ThreadPoolManager(int type, int poolSize) {
        this.mType = (type == TYPE_FIFO) ? TYPE_FIFO : TYPE_LIFO;

        if (poolSize < MIN_POOL_SIZE)
            poolSize = MIN_POOL_SIZE;
        if (poolSize > MAX_POOL_SIZE)
            poolSize = MAX_POOL_SIZE;
        this.mPoolSize = poolSize;

        mThreadPool = Executors.newFixedThreadPool(this.mPoolSize);

        mAsyncTasks = new LinkedList<ThreadPoolTaskBase>();

        mDelayedTasks = new LinkedList<ThreadPoolTaskBase>();
    }

    /**
     *
     * @方法名：addAsyncTask
     * @描述：向任务队列中添加任务
     * @param task
     * @输出：String threadId
     * @作者：shijunfeng
     *
     */
    public String addAsyncTask(ThreadPoolTaskBase task) {
        if (null == task){
            return "";
        }

        synchronized (mAsyncTasks) {
            if (mThreadPool.isShutdown()) {
                mThreadPool = Executors.newFixedThreadPool(this.mPoolSize); 
            }
            mAsyncTasks.addLast(task);
        }
        return (String) task.getId();
    }
    /**
     *
     * @方法名：addAsyncTask
     * @描述：向任务队列中添加延时任务
     * @param task
     * @输出：String threadId
     * @作者：shijunfeng
     *
     */
    private String addDelayTask(ThreadPoolTaskBase task) {
        if (null == task){
            return "";
        }
        synchronized (mDelayedTasks) {
            mDelayedTasks.addLast(task);
        }
        return (String) task.getId();
    }

    public boolean deleteTaskById(String threadId){
        if (mAsyncTasks.isEmpty() && mDelayedTasks.isEmpty()){
            return false;
        }
        synchronized (mAsyncTasks){
            for (ListIterator<ThreadPoolTaskBase> iterator = mAsyncTasks.listIterator(); iterator.hasNext(); ) {
                ThreadPoolTaskBase taskBase = iterator.next();
                if (taskBase.getId().equals(threadId)){
                    iterator.remove();
                    return true;
                }
            }
        }
        synchronized (mDelayedTasks){
            for (ListIterator<ThreadPoolTaskBase> iterator = mDelayedTasks.listIterator(); iterator.hasNext(); ) {
                ThreadPoolTaskBase taskBase = iterator.next();
                if (taskBase.getId().equals(threadId)){
                    iterator.remove();
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 
     * @方法名：getAsyncTask
     * @描述：从任务队列中提取任务
     * @return
     * @输出：ThreadPoolTaskBase
     * @作者：shijunfeng
     *
     */
    private ThreadPoolTaskBase getAsyncTask() {
        synchronized (mAsyncTasks) {
            if (mAsyncTasks.size() > 0) {
                ThreadPoolTaskBase task = (this.mType == TYPE_FIFO) ? mAsyncTasks.removeFirst() : mAsyncTasks
                        .removeLast();
                return task;
            }
        }
        return null;
    }

    /**
     * 
     * @方法名：start
     * @描述：开启线程池轮询
     * @输出：void
     * @作者：shijunfeng
     *
     */
    public void start() {
        if (mPoolThread == null) {
            mPoolThread = new Thread(new PoolRunnable());
            mPoolThread.start();
        }
    }

    /**
     * 
     * @方法名：stop
     * @描述：结束轮询，关闭线程池
     * @输出：void
     * @作者：shijunfeng
     *
     */
    public void stop() {
        if (mPoolThread != null) {
            mPoolThread.interrupt();
            mPoolThread = null;
        }
    }

    /**
     * 
     * @包名：com.cmri.prcs.message.threadpool
     * @类名：PoolRunnable
     * @描述：实现轮询的Runnable
     * @作者：shijunfeng
     * @时间：2015年6月6日上午7:58:59
     * @版本：1.0.0
     *
     */

    private class PoolRunnable implements Runnable {

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    if (NetConnectionReceiver.getNetConnectionStatus() == NetConnectionReceiver.NetConnectionStatus.CONNETCTED){
                        popDelayedTasks();
                    }

                    ThreadPoolTaskBase task = getAsyncTask();

                    if (task == null) {
                        try {
                            Thread.sleep(SLEEP_TIME);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        continue;
                    }

                    if (task.getTaskType() == ThreadPoolTaskBase.TaskType.DELAY){
                        if (NetConnectionReceiver.getNetConnectionStatus() == NetConnectionReceiver.NetConnectionStatus.DISCONNETED){
                            addDelayTask(task);
                            task.doResultCallback(null, ThreadPoolTaskBase.TaskResult.DELAYED);
                            continue;
                        }
                    }
                    if(!mThreadPool.isShutdown()){
                        try {
                            mThreadPool.execute(task);
                        }catch (RejectedExecutionException e){
                            e.printStackTrace();
                        }
                    }

                    try{
                        if (mAsyncTasks.isEmpty() && mDelayedTasks.isEmpty()){
                            Thread.sleep(SLEEP_TIME*2);
                            mThreadPool.shutdown();
                        }
                    }catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            } finally {
                mThreadPool.shutdown();
            }
        }

    }

    private void popDelayedTasks() {
        synchronized (mAsyncTasks) {
            if (mThreadPool.isShutdown()) {
                mThreadPool = Executors.newFixedThreadPool(this.mPoolSize);
            }
            if (mDelayedTasks.size() > 0){
                mAsyncTasks.addAll(mDelayedTasks);
                mDelayedTasks.clear();
            }
        }
    }

}
