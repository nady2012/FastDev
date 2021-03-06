package com.mamba.mambasdk.ui;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Used to show a custom AlertDialog.
 * 
 */
public class FDPopupWindow {

    private Context ctx;

    private android.widget.PopupWindow popupWindow;

    public FDPopupWindow(Context context) {
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
