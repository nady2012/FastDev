package com.fastdev.fastdevlib.util;

import android.graphics.Bitmap;

/**
 * <b>Description:</b> A cache that holds strong references to a limited number
 * of values.
 * 
 * <br>
 * <b>Purpose:</b> LruCache providers a cache to store bitmap, contact name
 * and contact id.
 */
public class LruCache {
    private android.support.v4.util.LruCache contactNumberNameCache;
    private android.support.v4.util.LruCache contactNumberIdCache;
    private android.support.v4.util.LruCache bitmapCache;

    public static LruCache getInstance() {
        return XSLruCacheHolder.instance;
    }

    private static class XSLruCacheHolder {
        protected static LruCache instance = new LruCache();
    }

    private LruCache() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        contactNumberNameCache = new android.support.v4.util.LruCache(maxMemory / 1024);
        contactNumberIdCache = new android.support.v4.util.LruCache(maxMemory / 1024);
        bitmapCache = new android.support.v4.util.LruCache(maxMemory / 16) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    /**
     * Get stored contact name.
     * 
     * @param number
     *            The key of the name.
     * @return The name you had been stored.
     */
    public String getContactName(String number) {
        if (null == number) {
            return null;
        }
        return contactNumberNameCache.get(number);
    }

    /**
     * Remove stored contact name.
     * 
     * @param number
     *            The key of the name.
     * @return The name you removed.
     */
    public String removeContactName(String number) {
        if (null == number) {
            return null;
        }
        return contactNumberNameCache.remove(number);
    }

    /**
     * Store a contact name.
     * 
     * @param number
     *            The key.
     * @param name
     *            The value.
     */
    public void storeContactName(String number, String name) {
        if (null == number || null == name) {
            return;
        }
        contactNumberNameCache.put(number, name);
    }

    /**
     * Get stored contactId.
     * 
     * @param number
     *            The key of the contactId.
     * @return The contactId you had been stored.
     */
    public String getContactId(String number) {
        if (null == number) {
            return null;
        }
        return contactNumberIdCache.get(number);
    }

    /**
     * Remove stored contactId.
     * 
     * @param number
     *            The key of the contactId.
     * @return The contactId you removed.
     */
    public String removeContactId(String number) {
        if (null == number) {
            return null;
        }
        return contactNumberIdCache.remove(number);
    }

    /**
     * Store a contactId.
     * 
     * @param number
     *            The key.
     * @param contactId
     *            The value.
     */
    public void storeContactId(String number, String contactId) {
        if (null == number || null == contactId) {
            return;
        }
        contactNumberIdCache.put(number, contactId);
    }

    /**
     * Get stored bitmap.
     * 
     * @param key
     *            The key of the bitmap.
     * @return The bitmap you had been stored.
     */
    public Bitmap getBitmap(String key) {
        if (null == key) {
            return null;
        }
        return bitmapCache.get(key);
    }

    /**
     * Remove stored bitmap.
     * 
     * @param key
     *            The key of the bitmap.
     * @return Removed bitmap.
     */
    public Bitmap removeBitmap(String key) {
        if (null == key) {
            return null;
        }
        return bitmapCache.remove(key);
    }

    /**
     * Store a bitmap.
     * 
     * @param key
     *            The key.
     * @param value
     *            The value.
     */
    public void storeBitmap(String key, Bitmap value) {
        if (null == key || null == value) {
            return;
        }
        bitmapCache.put(key, value);
    }

    /**
     * Clean all cache.
     */
    public void clearMemoryCache() {
        contactNumberNameCache.evictAll();
        contactNumberIdCache.evictAll();
        bitmapCache.evictAll();
    }
}
