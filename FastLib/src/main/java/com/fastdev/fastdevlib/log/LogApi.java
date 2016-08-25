package com.fastdev.fastdevlib.log;

import android.util.Log;

import com.fastdev.fastdevlib.util.FileUtil;
import com.fastdev.fastdevlib.util.ThreadUtil;
import com.fastdev.fastdevlib.util.TimeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LogApi {
    public static boolean ISDEBUG = false;
    private static List<String> logsBuffer = new ArrayList<String>();

    private static int TYPE_ERROR = 0;
    private static int TYPE_DEBUG = 1;
    private static int TYPE_INFO = 2;

    private static int MAX_LOGS_SIZE = 1000;

    public static void init() {
        logsBuffer.clear();
    }

    public static void e(String tag, String msg) {
        if (ISDEBUG) {
            if (null == msg) {
                msg = "";
            }
            Log.e(tag, msg);
        }
        addLogBuffer(TYPE_ERROR, tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (ISDEBUG) {
            if (null == msg) {
                msg = "";
            }
            Log.e(tag, msg, tr);
        }
        addLogBuffer(TYPE_ERROR, tag, msg + "\n" + ((null == tr) ? "" : tr.getMessage()));
    }

    public static void d(String tag, String msg) {
        if (ISDEBUG) {
            if (null == msg) {
                msg = "";
            }
            Log.d(tag, msg);
        }
        addLogBuffer(TYPE_DEBUG, tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (ISDEBUG) {
            if (null == msg) {
                msg = "";
            }
            Log.d(tag, msg, tr);
        }
        addLogBuffer(TYPE_DEBUG, tag, msg + "\n" + ((null == tr) ? "" : tr.getMessage()));
    }

    public static void i(String tag, String msg) {
        if (ISDEBUG) {
            if (null == msg) {
                msg = "";
            }
            Log.i(tag, msg);
        }
        addLogBuffer(TYPE_INFO, tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (ISDEBUG) {
            if (null == msg) {
                msg = "";
            }
            Log.i(tag, msg, tr);
        }
        addLogBuffer(TYPE_INFO, tag, msg + "\n" + ((null == tr) ? "" : tr.getMessage()));
    }

    private static void addLogBuffer(int type, String tag, String msg) {
        String time = TimeUtil.getTime();

        String typeStr = "Error";
        switch (type) {
            case 0:
                typeStr = "Error";
                break;
            case 1:
                typeStr = "Debug";
                break;
            case 2:
                typeStr = "Info";
                break;
            default:
                break;
        }

        String log = time + " " + typeStr + " > " + tag + " : " + msg;
        synchronized (logsBuffer) {
            logsBuffer.add(log);
            if (logsBuffer.size() > MAX_LOGS_SIZE) {
                List<String> newLogsBuffer = new ArrayList<String>();
                newLogsBuffer.addAll(logsBuffer);
                logsBuffer.clear();
                ThreadUtil.getInstance().submitTask(new LogsSaveRunnable(newLogsBuffer));
            }
        }
    }

    public static void saveLastLogs() {
        synchronized (logsBuffer) {
            if (logsBuffer.isEmpty()) {
                return;
            }
            List<String> newLogsBuffer = new ArrayList<String>();
            newLogsBuffer.addAll(logsBuffer);
            logsBuffer.clear();

            ThreadUtil.getInstance().submitTask(new LogsSaveRunnable(newLogsBuffer));
        }
    }

    static class LogsSaveRunnable implements Runnable {

        private List<String> logsBuffer;

        public LogsSaveRunnable(List<String> logsBuffer) {
            this.logsBuffer = logsBuffer;
        }

        @Override
        public void run() {
            if (null == logsBuffer || logsBuffer.isEmpty()) {
                return;
            }
            StringBuffer buffer = new StringBuffer();
            for (String log : logsBuffer) {
                buffer.append(log).append("\n");
            }

            if (buffer.length() == 0) {
                return;
            }

            String logFilePath = FileUtil.getCurrentLogPath();
            String filePath = logFilePath + File.separator + TimeUtil.getTime() + ".log";
            // 判断日志附件量是否超过最大值
            FileUtil.deleteFileByDate(logFilePath);
            FileUtil.saveToFile(filePath, buffer);
        }

    }

    ;

}
