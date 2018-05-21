package com.mamba.mambasdk.util;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

public class PinYinUtil {

    private static final String TAG = PinYinUtil.class.getSimpleName();

    /**
     * 输出格式
     * 
     * @return
     */
    private static HanyuPinyinOutputFormat getDefaultOutputFormat() {
        final HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 大写
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 没有音调数字
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);// u显示
        return format;
    }

    /**
     * 将中文转换成拼音
     * 
     *            -汉字
     * @return
     */
    public static String getPinYin(String zhongwen) throws BadHanyuPinyinOutputFormatCombination {

        String zhongWenPinYin = "";
        final char[] chars = zhongwen.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            final String[] pinYin = PinyinHelper.toHanyuPinyinStringArray(chars[i], getDefaultOutputFormat());
            // 当转换不是中文字符时,返回null
            if (pinYin != null) {
                zhongWenPinYin += pinYin[0];
            } else {
                zhongWenPinYin += chars[i];
            }
        }
        return zhongWenPinYin;
    }

    /**
     * 将传递的汉字list转换成拼音List
     * 
     * @param list
     */
    public static List<String> getPinyinList(List<String> list) {
        List<String> pinyinList = new ArrayList<String>();
        String pinyin = null;
        for (Iterator<String> i = list.iterator(); i.hasNext();) {
            try {
                pinyin = getPinYin(i.next());
                pinyinList.add(pinyin);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            } catch (ConcurrentModificationException e) {
                e.printStackTrace();
            }
        }
        return pinyinList;
    }
}
