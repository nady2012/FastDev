package com.fastdev.fastdevlib.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.fastdev.fastdevlib.R;

/**
 * Used to show a custom AlertDialog.
 * 
 */
public class PopupWindow {

    private Context ctx;

    private android.widget.PopupWindow popupWindow;

    public PopupWindow(Context context) {
        ctx = context;
    }

    /**
     * Dismiss this dialog, removing it from the screen.
     * 
     */
    public void dismiss() {
        if (null == popupWindow || !popupWindow.isShowing()) {
            return;
        }
        popupWindow.dismiss();
        popupWindow = null;
    }


    /**
     * button传入事件为空时 设置默认
     */
    OnClickListener defaultCancelListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
}
