package com.fastdev.fastdevlib.threadpool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by sjf on 2015/8/21.
 */
public class NetConnectionReceiver extends BroadcastReceiver{

    private static NetConnectionStatus sNetStatus = NetConnectionStatus.CONNETCTED;//default
    public NetConnectionReceiver(Context context){
        getNetWorkInfo(context);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            getNetWorkInfo(context);
        }

    }

    private void getNetWorkInfo(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();

        if (activeInfo == null) {
            sNetStatus = NetConnectionStatus.DISCONNETED;
        } else {
            sNetStatus = NetConnectionStatus.CONNETCTED;
        }
    }

    public static NetConnectionStatus getNetConnectionStatus(){
        return sNetStatus;
    }
    public enum NetConnectionStatus {
        CONNETCTED,
        DISCONNETED
    }

}
