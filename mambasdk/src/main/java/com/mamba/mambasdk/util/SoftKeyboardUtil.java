package com.mamba.mambasdk.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * <b>Description:</b> SoftKeyboardUtil provides the entry of interfaces to
 * show and hide system soft keyboard. <br>
 * <b>Purpose:</b> Invoking interfaces in this class to to show or hide system
 * soft keyboard.
 */
public class SoftKeyboardUtil {

    /**
     * Explicitly request that the current input method's soft input area be
     * shown to the user, if needed.
     * 
     * @param view
     *            The currently focused view, which would like to receive soft
     *            keyboard input.
     */
    public static void showKeyboard(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /**
     * Request to hide the soft input window from the context of the window that
     * is currently accepting input.
     * 
     * @param activity
     *            The currently activity, which shows the view receives soft
     *            keyboard input
     */
    public static void showKeyboard(Activity activity) {
        if (null == activity) {
            return;
        }
        InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v != null) {
            im.showSoftInput(v, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    /**
     * Request to hide the soft input window from the context of the window that
     * is currently accepting input.
     * 
     * @param activity
     *            The currently activity, which shows the view receives soft
     *            keyboard input.
     */
    public static void hideKeyboard(Activity activity) {
        if (null == activity) {
            return;
        }
        InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v != null) {
            im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Request to hide the soft input window from the context of the window that
     * is currently accepting input.
     * 
     * @param view
     *            he currently focused view, which would like to receive soft
     *            keyboard input.
     */
    public static void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
