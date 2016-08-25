package com.fastdev.fastdevlib.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;


/**
 * <b>Description:</b> XSScreenDensityUtil provides the entry of interfaces
 * about screen density expressed as dots-per-inch operation. <br>
 * <b>Purpose:</b> Invoking interfaces in this class to do some screen density
 * expressed as dots-per-inch operations.
 */
public class XSScreenDensityUtil {

    private static final String TAG = "XSScreenDensityUtil";

    private static final float DESIRED_DENSITY = 1.7f;
    private static final int STANDARD_SCREEN_DENSITY = 160;
    private static final float FONT_MAGNIFICATION = 1.0f;
    private static final float CURRENT_SCREEN_DENSITY = (240 / (float) 160.0);

    private static final int MAX_WIDTH_PIXELS = 720;
    private static final int MAX_HEIGHT_PIXELS = 1200;

    /**
     * If the screen density expressed as dots-per-inch of the device is normal
     * or not. See also {@link #adjustScreenDensityDpi(Context)}
     * 
     * @param context
     *            The context of Android operation system (OS).
     * @return true, if the screen density expressed as dots-per-inch of the
     *         device is normal. <br>
     *         false, if the screen density expressed as dots-per-inch of the
     *         device is not normal.
     */
    public static boolean isNormalScreenDensityDpi(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        if (metrics.density <= CURRENT_SCREEN_DENSITY
                && (metrics.widthPixels >= MAX_WIDTH_PIXELS && metrics.heightPixels >= MAX_HEIGHT_PIXELS || metrics.widthPixels >= MAX_HEIGHT_PIXELS
                        && metrics.heightPixels >= MAX_WIDTH_PIXELS)) {
            return false;
        }

        return true;
    }

    /**
     * Set the screen density expressed as dots-per-inch the right value. This
     * method can only be invoked when
     * {@link #isNormalScreenDensityDpi(Context)} returns true. See also
     * {@link #isNormalScreenDensityDpi(Context)}.
     * 
     * @param context
     *            The context of Android operation system (OS).
     */
    public static void adjustScreenDensityDpi(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configRes = resources.getConfiguration();
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            // configRes.densityDpi = (int) (DESIRED_DENSITY *
            // STANDARD_SCREEN_DENSITY);
        }
        metrics.density = DESIRED_DENSITY;
        metrics.xdpi = DESIRED_DENSITY * STANDARD_SCREEN_DENSITY;
        metrics.ydpi = DESIRED_DENSITY * STANDARD_SCREEN_DENSITY;
        configRes.fontScale = FONT_MAGNIFICATION;
        metrics.densityDpi = (int) (DESIRED_DENSITY * STANDARD_SCREEN_DENSITY);
        resources.updateConfiguration(configRes, metrics);
    }

}
