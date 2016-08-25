package com.fastdev.fastdevlib.sim;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fastdev.fastdevlib.R;


public class XSSimUtil {

    public CTelephoneInfo cTelephoneInfo;

    public static XSSimUtil instance = null;

    private Context context;

    public static XSSimUtil getInstance() {
        if (null == instance) {
            instance = new XSSimUtil();
        }
        return instance;
    }

    /**
     * 判断是否是双卡手机
     * 
     * @param context
     * @return
     */
    public boolean isDualSim(Context context) {
        if (null == cTelephoneInfo) {
            cTelephoneInfo = CTelephoneInfo.getInstance(context);
            cTelephoneInfo.setCTelephoneInfo();
        }
        return cTelephoneInfo.isDualSim();
    }

    /**
     * 获取SIM卡运行商
     * 
     * @param context
     * @param index
     *            SIM卡位置
     * @return 运行商
     */
    public String getSimOperators(Context context, int index) {
        if (null == cTelephoneInfo) {
            cTelephoneInfo = CTelephoneInfo.getInstance(context);
            cTelephoneInfo.setCTelephoneInfo();
        }

        String operator = cTelephoneInfo.getINumeric();

        if (cTelephoneInfo.isDualSim()) {
            if (index == 1) {
                operator = cTelephoneInfo.getINumeric1();
            } else {
                operator = cTelephoneInfo.getINumeric2();
            }
        }
        return getSimOperatorsStr(context, operator);
    }

    /**
     * 根据operator判断运营商
     * 
     * @param context
     * @param operator
     * @return
     */
    private String getSimOperatorsStr(Context context, String operator) {
        if (TextUtils.isEmpty(operator)) {
            return "Unknown";
        } else if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
            // 中国移动
            return "China Mobile";
        } else if (operator.equals("46001")) {
            // 中国联通
            return "China Unicom";
        } else if (operator.equals("46003")) {
            // 中国电信
            return "China Telecom";
        } else {
            return "Unknown";
        }
    }

    /**
     * 判断是否插有SIM卡
     * 
     * @return true SIM卡正常 <br>
     *         false SIM卡异常
     */
    public boolean isSimAbsent() {
        int absent = TelephonyManager.SIM_STATE_ABSENT;
        if (1 == absent) {
            return false;
        }
        return true;
    }

    public interface OnSimChangedListener {
        void onSimChanged(int index);
    }
}
