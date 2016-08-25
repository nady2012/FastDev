package com.fastdev.fastdevlib.util;

/**
 * 常量字段统一放在此处
 * 
 * @ClassName: XSConstant
 * @author mWX271102
 * @date 2015年4月7日 上午11:04:46
 * 
 */
public class XSConstant extends XSBaseConstant {

    /**
     * 手机号码
     */
    public static final String INTENT_PARAM_PHONENUM = "phoneNum";

    /**
     * nonce
     */
    public static final String INTENT_PARAM_NONCE = "nonce";

    /**
     * Country
     */
    public static final String INTENT_PARAM_COUNTRY = "Country";

    /**
     * 联系人ID
     */
    public static final String INTENT_PARAM_CONTACT_ID = "INTENT_PARA_CONTACT_ID";

    /**
     * 联系人名称
     */
    public static final String INTENT_PARAM_CONTACT_NAME = "INTENT_PARAM_CONTACT_NAME";

    /**
     * 联系人
     */
    public static final String INTENT_PARAM_CONTACT = "INTENT_PARAM_CONTACT";

    /**
     * 判断是否是修改已经绑定的小号
     */
    public static final String INTENT_PARAM_ISMODIFYSOCKPUPPET = "isModifySockpuppet";

    /**
     * 是否首次绑定小号
     */
    public static final String INTENT_PARAM_ISFIRSTBINDSOCKPUPPET = "isFirstBindSockpuppet";

    /**
     * 首次是否从欢迎界面进入主界面
     */
    public static final String INTENT_PARAM_ISFROMBEGINPAGE = "isFromBeginPage";

    /**
     * 从关于界面进入登录界面
     */
    public static final String INTENT_PARAM_FROMABOUT = "FromAbout";

    public static final String INTENT_PARAM_BINDINFO = "bindInfo";

    public static final String TAG_PARAM_LOG_LIST = "callLogs";

    public static final String TAG_PARAM_CONTACT_LIST = "Contacts";

    public static final String TAG_PARAM_CONTACT = "Contact";

    /**
     * Default Country Code
     */
    public static final String DEFAULT_COUNTRY_CODE = "+86";
    /**
     * Default Province Code
     */
    public static final String DEFAULT_PROVINCE = "四川";

    /**
     * SockPuppet Type
     * 绑定类型（0:虚拟号码；1:真实固话）
     */
    public static final int SOCKPUPPET_TYPE_VIRTUAL_NUM = 0;
    public static final int SOCKPUPPET_TYPE_TEL_NUM = 1;
}
