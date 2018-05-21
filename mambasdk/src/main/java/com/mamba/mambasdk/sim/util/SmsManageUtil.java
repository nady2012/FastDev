package com.mamba.mambasdk.sim.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.List;

/**
 * 
 * @ClassName: SmsManageUtil
 * @author mWX271102
 * @date 2015年5月15日 下午2:27:52
 * @deprecated {@link #XSSmsUtil}
 */
public class SmsManageUtil {

    public static final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    public static final String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    private static SmsManageUtil instance;

    public static SmsManageUtil getInstance() {
        if (null == instance) {
            instance = new SmsManageUtil();
        }
        return instance;
    }

    public void sendSMS(Context mContext, String phoneNumber, String message) {

        // ---sends an SMS message to another device---
        SmsManager sms = SmsManager.getDefault();

        // create the sentIntent parameter
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0, sentIntent, 0);

        // create the deilverIntent parameter
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(mContext, 0, deliverIntent, 0);

        // 短信超过70个字符分开发送
        if (message.length() > 70) {
            List<String> msgs = sms.divideMessage(message);
            for (String msg : msgs) {
                sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);
            }
        } else {
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);
        }
    }
}
