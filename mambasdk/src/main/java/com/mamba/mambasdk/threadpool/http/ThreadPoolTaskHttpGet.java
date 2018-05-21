package com.mamba.mambasdk.threadpool.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.text.TextUtils;

/**
 * 
 * @包名：com.cmri.prcs.base.threadpool.http
 * @类名：ThreadPoolTaskHttpGet
 * @描述：(描述这个类的作用)
 * @作者：sjf
 * @时间：2015年7月8日下午12:42:43
 * @版本：1.0.0
 *
 */
public class ThreadPoolTaskHttpGet extends ThreadPoolTaskHttp {

    public ThreadPoolTaskHttpGet(Context context, onResultListener listener, String requestCode, String url) {
        super(context, listener, requestCode,url);
        mUrlString = url;
    }

    public ThreadPoolTaskHttpGet(Context context, onResultListener listener, String requestCode, String url, TaskType taskType) {
        super(context, listener, requestCode,url, taskType);
        mUrlString = url;
    }
    
    @Override
    public void run() {
        if(TextUtils.isEmpty(mUrlString)){
            return ;
        }
        try {
            URL url = new URL(mUrlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setRequestProperty("Charset", "UTF-8");
            conn.addRequestProperty("Cookie", JSESSIONID);
            super.run();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            doResultCallback(null,TaskResult.ERROR);
        }

    }

}
