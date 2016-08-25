package com.fastdev.fastdevlib.ui;

/**
 * TipsBar调用的相关方法，所有基类要实现此方法
 * 
 * @ClassName: TipsBarImpl
 * @author mWX271102
 * @date 2015年4月7日 上午10:26:56
 * 
 */
public interface TipsBarImpl {

    /**
     * 显示Tips<br>
     * 基类公用方法<br>
     * 
     * @param isSuc
     *            消息类型 成功/失败
     * @param messageId
     *            显示的消息Id
     */
    void showTips(boolean isSuc, int messageId);

    /**
     * 显示Tips<br>
     * 基类公用方法<br>
     * 
     * @param isSuc
     *            消息类型 成功/失败
     * @param message
     *            显示的消息
     */
    void showTips(boolean isSuc, String message);

    /**
     * 显示tipsBar左边的进度条，带默认显示消息<br>
     * 基类公用方法<br>
     */
    void showProgressTipsBar();

    /**
     * 显示tipsBar左边的进度条<br>
     * 基类公用方法<br>
     * 
     * @param messageId
     *            显示的消息Id
     */
    void showProgressTipsBar(int messageId);

    /**
     * 显示tipsBar左边的进度条<br>
     * 基类公用方法<br>
     * 
     * @param message
     *            显示的消息
     */
    void showProgressTipsBar(String message);

    /**
     * 隐藏Tips<br>
     * 基类公用方法<br>
     */
    void hideTips();
}
