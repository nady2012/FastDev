package com.mamba.mambasdk.util;

import android.text.TextUtils;


public class CommonUtil {

    private static final String TAG = CommonUtil.class.getSimpleName();

    public static String formatPhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return null;
        }
        return phoneNum.replace(" ", "");
    }

    public static int formatStringToInt(String data) {
        try {
            return Integer.parseInt(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * String安全转换Double
     * 
     * @param data
     *            String
     * @return Double
     */
    public static double formatStringToDouble(String data) {
        try {
            return Double.parseDouble(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static long formatStringToLong(String data) {
        try {
            return Long.parseLong(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String formatByteToString(byte[] buffer) {
        if (null == buffer) {
            return null;
        }
        try {
            return new String(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断号码是否符合规则 1.号码必须不为空 2.号码的长度超过10位 3.号码必须以1开头 4.号码必须全是数字（在输入的时候控制）
     *
     * @param phoneNumber 手机号码
     * @return if available then true, else false
     */
    public static boolean isPhoneNumberAvailable(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        }
        if (phoneNumber.length() != 11 || !phoneNumber.substring(0, 1).equals("1") || phoneNumber.contains("*")
                || phoneNumber.contains("#")) {
            return false;
        } else {
            return true;
        }

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
