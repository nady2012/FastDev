package com.fastdev.fastdevlib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fastdev.fastdevlib.R;
import com.fastdev.fastdevlib.ui.XSWTipsBarView;

public class XSNetworkUtil {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        }
        return false;
    }

    public static boolean isNetworkAvailable(Context context, XSWTipsBarView tipsBarView) {
        if (isNetworkAvailable(context)) {
            return true;
        }
        if (tipsBarView != null) {
            tipsBarView.showFailTipsBar(context.getString(R.string.str_base_response_network_error),false);
        }
        return false;
    }

    public static boolean isWifiNetWork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifi = false;
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (null != info) {
                isWifi = info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI;
            }
        }
        return isWifi;
    }

    /**
     * WIFI是否使用网络
     * 
     * @return
     */
    public static boolean isWifiUseNetwork(Context context) {
        // return isNetworkAvailable(context) && isWiFiActive(context);
        return isWifiNetWork(context);
    }
}
