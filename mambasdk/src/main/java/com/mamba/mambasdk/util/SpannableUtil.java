package com.mamba.mambasdk.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.List;

/**
 * Created by Kaven on 2016/3/31.
 */
public class SpannableUtil {
    public static SpannableStringBuilder setHighLight(Context context , String source, List<String> keyList, int color){
        if (TextUtils.isEmpty(source)){
            return  new SpannableStringBuilder("");
        }
        SpannableStringBuilder styled = new SpannableStringBuilder(source);
        if (TextUtils.isEmpty(source) || keyList == null || keyList.isEmpty()){
            return styled;
        }
        for (String key : keyList) {
            if (key.length() > source.length()){
                continue;
            }
            int start = source.indexOf(key);
            if (start < 0 ){
                continue;
            }
            int end = start+key.length();
            styled.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return styled;
    }
}
