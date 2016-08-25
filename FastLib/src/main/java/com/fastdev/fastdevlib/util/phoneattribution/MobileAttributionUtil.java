package com.fastdev.fastdevlib.util.phoneattribution;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @包名：com.cmri.prcs.util.phoneattribution
 * @类名：MobileAttributionUtil
 * @描述：电话号码归属地信息查询
 * @作者：mss
 * @时间：2015-2-11上午11:19:35
 * @版本：1.0.0
 * 
 */
public class MobileAttributionUtil {
    /* 最短座机查询长度 */
    private final static int MIN_LANDLINE_QUERY_LEN = 3;
    /* 最短手机查询长度 */
    private final static int MIN_MOBILE_PHONE_QUERY_LEN = 7;

    private static HashMap<String, MobileAttribution> sMobileAttribution = new HashMap<String, MobileAttribution>();

    /**
     * 
     * @方法名：getNumberAttribution
     * @描述：查询号码归属地信息
     * @param phoneNumber
     * @return
     * @输出：void
     * @作者：mss
     * 
     */
    public static MobileAttribution getNumberAttribution(Context context, String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() == 0) {
            return null;
        }

        String number = getPhoneNum(phoneNumber);
        if (isZeroStarted(number) && getNumLength(number) >= MIN_LANDLINE_QUERY_LEN) {
            ArrayList<String> queryKey = new ArrayList<String>();
            queryKey.add(number.substring(0, MIN_LANDLINE_QUERY_LEN));
            if (number.length() > MIN_LANDLINE_QUERY_LEN) {
                queryKey.add(number.substring(0, MIN_LANDLINE_QUERY_LEN + 1));
            }
            for (String key : queryKey) {
                if (sMobileAttribution.containsKey(key)) {
                    return sMobileAttribution.get(key);
                }
            }

            MobileAttribution numberAttr = MobileAttributionDB.getInstance(context).queryLandlineNumber(queryKey);
            if (numberAttr != null) {
                for (String key : queryKey) {
                    sMobileAttribution.put(key, numberAttr);
                }
                return numberAttr;
            }
        } else if (isOneStarted(number) && getNumLength(number) >= MIN_MOBILE_PHONE_QUERY_LEN) {
            String queryKey = number.substring(0, MIN_MOBILE_PHONE_QUERY_LEN);
            if (sMobileAttribution.containsKey(queryKey)) {
                return sMobileAttribution.get(queryKey);
            }
            MobileAttribution numberAttr = MobileAttributionDB.getInstance(context).queryMobileNumber(queryKey);
            if (numberAttr != null) {
                sMobileAttribution.put(queryKey, numberAttr);
                return numberAttr;
            }
        }
        return null;
    }

    /** 判断号码是否以零开头 */
    public static boolean isZeroStarted(String number) {
        if (number == null || number.isEmpty()) {
            return false;
        }
        return number.charAt(0) == '0';
    }

    /** 判断号码是否以1开头 */
    public static boolean isOneStarted(String number) {
        if (number == null || number.isEmpty()) {
            return false;
        }
        return number.charAt(0) == '1';
    }

    /** 得到号码的长度 */
    public static int getNumLength(String number) {
        if (number == null || number.isEmpty()) {
            return 0;
        }
        return number.length();
    }

    public static String getPhoneNum(String formatedPhoneNum) {
        if (TextUtils.isEmpty(formatedPhoneNum)) {
            return "";
        } else {
            if (formatedPhoneNum.startsWith("+86")) {
                formatedPhoneNum = formatedPhoneNum.substring(3, formatedPhoneNum.length());

            }else if (formatedPhoneNum.startsWith("0086")) {
                formatedPhoneNum = formatedPhoneNum.substring(4, formatedPhoneNum.length());

            }else if(formatedPhoneNum.startsWith("+0086")) {
                formatedPhoneNum = formatedPhoneNum.substring(5, formatedPhoneNum.length());
            }

            return formatedPhoneNum.replaceAll(" ", "").replaceAll(" \t", "");
        }
    }
}
