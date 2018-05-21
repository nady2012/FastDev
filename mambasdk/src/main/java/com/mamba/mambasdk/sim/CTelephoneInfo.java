package com.mamba.mambasdk.sim;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import com.mamba.mambasdk.log.LogApi;

import java.lang.reflect.Method;

public class CTelephoneInfo {
    public static final String TAG = CTelephoneInfo.class.getSimpleName();
    private String imeiSIM1;// IMEI
    private String imeiSIM2;// IMEI
    private String iNumeric1;// IMSI1
    private String iNumeric2;// IMSI2
    private boolean isSIM1Ready;// sim1
    private boolean isSIM2Ready;// sim2
    /**
     * DATA_UNKNOWN = -1 <br>
     * DATA_DISCONNECTED = 0 <br>
     * DATA_CONNECTING = 1 <br>
     * DATA_CONNECTED = 2 <br>
     * DATA_SUSPENDED = 3 <br>
     */
    private String iDataConnected1 = "0";
    private String iDataConnected2 = "0";
    private static CTelephoneInfo CTelephoneInfo;
    private static Context mContext;

    private CTelephoneInfo() {
    }

    public synchronized static CTelephoneInfo getInstance(Context context) {
        if (CTelephoneInfo == null) {
            CTelephoneInfo = new CTelephoneInfo();
        }
        mContext = context;
        return CTelephoneInfo;
    }

    public String getImeiSIM1() {
        return imeiSIM1;
    }

    public String getImeiSIM2() {
        return imeiSIM2;
    }

    public boolean isSIM1Ready() {
        return isSIM1Ready;
    }

    public boolean isSIM2Ready() {
        return isSIM2Ready;
    }

    public boolean isDualSim() {
        return imeiSIM2 != null;
    }

    public boolean isDataConnected1() {
        if (TextUtils.equals(iDataConnected1, "2") || TextUtils.equals(iDataConnected1, "1"))
            return true;
        else
            return false;
    }

    public boolean isDataConnected2() {
        if (TextUtils.equals(iDataConnected2, "2") || TextUtils.equals(iDataConnected2, "1"))
            return true;
        else
            return false;
    }

    public String getINumeric1() {
        return iNumeric1;
    }

    public String getINumeric2() {
        return iNumeric2;
    }

    public String getINumeric() {
        if (imeiSIM2 != null) {
            if (iNumeric1 != null && iNumeric1.length() > 1)
                return iNumeric1;

            if (iNumeric2 != null && iNumeric2.length() > 1)
                return iNumeric2;
        }
        return iNumeric1;
    }

    public void setCTelephoneInfo() {
        TelephonyManager telephonyManager = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE));
        CTelephoneInfo.imeiSIM1 = telephonyManager.getDeviceId();
        ;
        CTelephoneInfo.imeiSIM2 = null;
        try {
            CTelephoneInfo.imeiSIM1 = getOperatorBySlot(mContext, "getDeviceIdGemini", 0);
            CTelephoneInfo.imeiSIM2 = getOperatorBySlot(mContext, "getDeviceIdGemini", 1);
            CTelephoneInfo.iNumeric1 = getOperatorBySlot(mContext, "getSimOperatorGemini", 0);
            CTelephoneInfo.iNumeric2 = getOperatorBySlot(mContext, "getSimOperatorGemini", 1);
            CTelephoneInfo.iDataConnected1 = getOperatorBySlot(mContext, "getDataStateGemini", 0);
            CTelephoneInfo.iDataConnected2 = getOperatorBySlot(mContext, "getDataStateGemini", 1);
        } catch (GeminiMethodNotFoundException e) {
            e.printStackTrace();
            LogApi.d(TAG, e.getMessage());
            try {
                CTelephoneInfo.imeiSIM1 = (String) getResultByGaoTong(mContext, "getDeviceId", 0);
                CTelephoneInfo.imeiSIM2 = (String) getResultByGaoTong(mContext, "getDeviceId", 1);
                CTelephoneInfo.iNumeric1 = (String) getResultByGaoTong(mContext, "getSimOperator", 0);
                CTelephoneInfo.iNumeric2 = (String) getResultByGaoTong(mContext, "getSimOperator", 1);
                // CTelephoneInfo.iDataConnected1 = getOperatorBySlot(mContext,
                // "getDataState", 0);
                // CTelephoneInfo.iDataConnected2 = getOperatorBySlot(mContext,
                // "getDataState", 1);
            } catch (Exception e1) {
                LogApi.d(TAG, e1.getMessage());
                try {
                    CTelephoneInfo.imeiSIM1 = getOperatorBySlot(mContext, "getDeviceId", 0);
                    CTelephoneInfo.imeiSIM2 = getOperatorBySlot(mContext, "getDeviceId", 1);
                    CTelephoneInfo.iNumeric1 = getOperatorBySlot(mContext, "getSimOperator", 0);
                    CTelephoneInfo.iNumeric2 = getOperatorBySlot(mContext, "getSimOperator", 1);
                    CTelephoneInfo.iDataConnected1 = getOperatorBySlot(mContext, "getDataState", 0);
                    CTelephoneInfo.iDataConnected2 = getOperatorBySlot(mContext, "getDataState", 1);
                } catch (GeminiMethodNotFoundException e2) {
                    // Call here for hy_icon_arrow_right manufacturer's predicted method name
                    // if you wish
                    LogApi.d(TAG, e2.getMessage());
                }
            }

        }
        CTelephoneInfo.isSIM1Ready = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
        CTelephoneInfo.isSIM2Ready = false;

        try {
            CTelephoneInfo.isSIM1Ready = getSIMStateBySlot(mContext, "getSimStateGemini", 0);
            CTelephoneInfo.isSIM2Ready = getSIMStateBySlot(mContext, "getSimStateGemini", 1);
        } catch (GeminiMethodNotFoundException e) {
            e.printStackTrace();
            LogApi.d(TAG, e.getMessage());
            try {
                int mainSimState = (Integer) getResultByGaoTong(mContext, "getSimState", 0);
                int otherSimState = (Integer) getResultByGaoTong(mContext, "getSimState", 1);
                CTelephoneInfo.isSIM1Ready = TelephonyManager.SIM_STATE_READY == mainSimState;
                CTelephoneInfo.isSIM2Ready = TelephonyManager.SIM_STATE_READY == otherSimState;
            } catch (Exception e1) {
                e1.printStackTrace();
                LogApi.d(TAG, e1.getMessage());
                try {
                    CTelephoneInfo.isSIM1Ready = getSIMStateBySlot(mContext, "getSimState", 0);
                    CTelephoneInfo.isSIM2Ready = getSIMStateBySlot(mContext, "getSimState", 1);
                } catch (GeminiMethodNotFoundException e2) {
                    // Call here for hy_icon_arrow_right manufacturer's predicted method name
                    // if you wish
                    e2.printStackTrace();
                    LogApi.d(TAG, e2.getMessage());
                }
            }

        }
    }

    private static String getOperatorBySlot(Context context, String predictedMethodName, int slotID)
            throws GeminiMethodNotFoundException {
        String inumeric = null;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);
            if (ob_phone != null) {
                inumeric = ob_phone.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }
        return inumeric;
    }

    private static boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID)
            throws GeminiMethodNotFoundException {

        boolean isReady = false;

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimStateGemini = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);

            if (ob_phone != null) {
                int simState = Integer.parseInt(ob_phone.toString());
                if (simState == TelephonyManager.SIM_STATE_READY) {
                    isReady = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return isReady;
    }

    /**
     * <p>
     * 通过反射，获取高通芯片对应SIM卡槽的SIM相关信息
     * </p>
     * 
     * @param mContext
     *            当前上下文
     * @param methodName
     *            需要查询信息的方法名
     * @param slot
     *            卡槽位置
     * @return
     */
    public static Object getResultByGaoTong(Context mContext, String methodName, int slot)
            throws GeminiMethodNotFoundException {
        Object result = null;
        try {
            Object obj = mContext.getSystemService("phone_msim");
            Class<?> clz = Class.forName("android.telephony.MSimTelephonyManager");

            Method imsiMethod = clz.getMethod(methodName, int.class);
            result = imsiMethod.invoke(obj, slot);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeminiMethodNotFoundException(methodName);
        }
    }
}
