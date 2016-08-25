package com.fastdev.fastdevlib.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;


import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class XSSafeUtil {

    private static final String TAG = "XSSafeUtil";

    /**
     * Check the signature is modified
     * 
     * @author : sWX293372
     * @version: 1.0
     * @return boolean if the signature is modified then return true, else
     *         return false
     * @createTime : 2016-5-9
     */
    public static boolean checkSignature(Context context, String packageName, int defaultSig) {
        boolean isSignatureModified = true;
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] signatures = pi.signatures;
            int sig = signatures[0].hashCode();
            if (defaultSig == sig) {
                isSignatureModified = false;
            }
        } catch (NameNotFoundException e) {
           e.printStackTrace();
        }

        return isSignatureModified;
    }

    /**
     * Check is classes.dex file has modified
     * 
     * @author : sWX293372
     * @version: 1.0
     * @return boolean if the classes.dex is modified then return true, else
     *         return true
     * @createTime : 2016-5-9
     */
    public static boolean checkCRC(Context context, long crc) {
        boolean isModified = true;
        try {
            ZipFile zipFile = new ZipFile(context.getPackageCodePath());
            ZipEntry entry = zipFile.getEntry("classes.dex");
            if (crc == entry.getCrc()) {
                isModified = false;
            }
        } catch (IOException e) {
            isModified = true;
            e.printStackTrace();
        }
        return isModified;
    }
}
