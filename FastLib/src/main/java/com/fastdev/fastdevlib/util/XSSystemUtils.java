package com.fastdev.fastdevlib.util;

import android.content.Context;
import android.content.Intent;


/**
 * 
 * @author : sWX293372
 * @version: 1.0
 *           <p>
 *           时间 : 2015-7-16
 *           </p>
 *           <p>
 *           描述 : TODO
 *           </p>
 *           <p>
 *           Copyright 1988-2005, Huawei Tech. Co., Ltd.
 *           </p>
 */
public class XSSystemUtils {

    private static final String TAG = "XSSystemUtils";

    /**
     * @author : sWX293372
     * @version: 1.0
     *           <p>
     *           时间 : 2015-7-16
     *           </p>
     *           <p>
     *           描述 : 使用该方法需要在androidManifest中配置相关权限
     *           </p>
     *           <p>
     *           实现方法：TODO
     *           </p>
     *           <p>
     *           Copyright 1988-2005, Huawei Tech. Co., Ltd.
     *           </p>
     * @param context
     * @param appName
     * @param iconId
     */
    public static void createShortcut(Context context, String appName, int iconId) {
        try {
            // 点击图标启动的Intent
            Intent launchIntent = new Intent(context, context.getClass());
            launchIntent.setAction(Intent.ACTION_MAIN);
            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 广播用的Intent
            Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            shortcutIntent.putExtra("duplicate", false);
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(context, iconId));
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
            context.sendBroadcast(shortcutIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
