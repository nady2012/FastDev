package com.mamba.mambasdk.threadpool.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import android.content.Context;

import android.text.TextUtils;

import com.mamba.mambasdk.threadpool.ThreadPoolTaskBase;

/**
 * 
 * @包名：com.cmri.prcs.base.threadpool.http
 * @类名：ThreadPoolTaskHttp
 * @描述：(描述这个类的作用)
 * @作者：sjf
 * @时间：2015年7月8日下午12:42:36
 * @版本：1.0.0
 *
 */
public class ThreadPoolTaskHttp extends ThreadPoolTaskBase {

    HttpURLConnection conn;
    BufferedReader bufferedReader;
    static String JSESSIONID = "";
    protected String mUrlString;

    public ThreadPoolTaskHttp(Context context, onResultListener listener, String requestCode, String urlString) {
        super(context, listener, requestCode);
        mUrlString = urlString;
    }

    public ThreadPoolTaskHttp(Context context, onResultListener listener, String requestCode, String urlString, TaskType taskType) {
        super(context, listener, requestCode, taskType);
        mUrlString = urlString;
    }

    @Override
    public void run() {

        conn.setReadTimeout(30000);
        conn.setConnectTimeout(30000);
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            // 将cookie设置到本地，方便下次请求
            setCookie();
            StringBuffer sb = new StringBuffer();
            String msg;
            while ((msg = bufferedReader.readLine()) != null) {
                sb.append(msg);
            }
            bufferedReader.close();
            conn.disconnect();
            if (TextUtils.isEmpty(sb.toString())){
                doResultCallback(null, TaskResult.FAILED);
                return;
            }
            doResultCallback(sb.toString(),TaskResult.SUCCEESS);
        } catch (IOException e) {
            e.printStackTrace();
            doResultCallback(null, TaskResult.ERROR);
        }

    }

    private void setCookie() {
        JSESSIONID = "JSESSIONID=" + conn.getHeaderField("jsessionid");
    }

}
