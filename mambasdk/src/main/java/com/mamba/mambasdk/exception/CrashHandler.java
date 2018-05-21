package com.mamba.mambasdk.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;


import com.mamba.mambasdk.log.LogApi;
import com.mamba.mambasdk.util.FileUtil;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = CrashHandler.class.getSimpleName();

    private static CrashHandler INSTANCE = null;

    private Map<String, String> infos = new HashMap<String, String>();

    private String crashLogFileName = "crashReport.txt";

    private Context context;

    private UncaughtExceptionHandler mDefaultHandler;

    public static synchronized CrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    public void init(Context context) {
        this.context = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        LogApi.d(TAG, "uncaughtException crash = " + ex.getMessage());
        new Thread() {

            @Override
            public void run() {
                saveCrashInfo2File(context, ex);
                super.run();
            }
        }.start();
        LogApi.saveLastLogs();
        mDefaultHandler.uncaughtException(thread, ex);
    }

    public void saveCrashInfo2File(Context ctx, Throwable ex) {
        collectPackageInfo(ctx);
        collectDeviceInfo(ctx);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : this.infos.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
        Writer writer = new StringWriter();

        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();

        sb.append(result);
        try {
            String crashFileDir = FileUtil.getCurrentLogPath() + File.separator + this.crashLogFileName;

            FileUtil.deleteFile(crashFileDir);

            FileUtil.saveToFile(crashFileDir, sb);
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
    }

    private void collectPackageInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;

                String versionCode = pi.versionCode + "";
                this.infos.put("versionName", versionName);
                this.infos.put("versionCode", versionCode);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                this.infos.put("CrashTime", str);
            }
        } catch (Exception e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
    }

    private void collectDeviceInfo(Context ctx) {
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                this.infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect device info", e);
            }
        }
    }
}
