package com.fastdev.fastdevlib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <b>Description:</b> PreferenceUtil provides the entry of interfaces for
 * accessing and modifying preference data. <br>
 * <b>Purpose:</b> Invoking interfaces in this class to access and modify
 * preference data.
 */
public class PreferenceUtil {

    private static HashMap<String, Preference> mPreferences = new HashMap<String, Preference>();

    private PreferenceUtil() {
    };

    /**
     * Constructor.
     * 
     * @param context
     *            The context of the Android operating system (OS).
     * @param name
     *            Desired preferences file. If a preferences file by this name
     *            does not exist, it will be created when you retrieve an editor
     *            (SharedPreferences.edit()) and then commit changes
     *            (Editor.commit()).
     * @return An instance of Preference.
     */
    @SuppressLint("CommitPrefEdits")
    public static Preference getInstance(Context context, String name) {
        if (mPreferences.get(name) != null) {
            return mPreferences.get(name);
        } else {
            SharedPreferences preference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            Editor editor = preference.edit();
            mPreferences.put(name, new Preference(preference, editor));
            return mPreferences.get(name);
        }
    }

    public static class Preference {
        private SharedPreferences mPreference;
        private Editor mEditor;
        private List<Object> mObjectlist;

        /**
         * 
         * @param preference
         * @param editor
         */
        public Preference(SharedPreferences preference, Editor editor) {
            this.mPreference = preference;
            this.mEditor = editor;
            this.mObjectlist = new ArrayList<Object>();
        };

        public Editor getEditor() {
            return mEditor;
        }

        /**
         * Set a boolean value in the preferences editor, and commit.
         * 
         * @param key
         *            The name of the preference to modify.
         * @param value
         *            The new value for the preference.
         */
        public void commitBoolean(String key, boolean value) {
            if (mEditor == null)
                return;
            mEditor.putBoolean(key, value);
            mEditor.commit();
        }

        /**
         * Set a float value in the preferences editor, and commit.
         * 
         * @param key
         *            The name of the preference to modify.
         * @param value
         *            The new value for the preference.
         */
        public void commitFloat(String key, Float value) {
            if (mEditor == null)
                return;
            mEditor.putFloat(key, value);
            mEditor.commit();
        }

        /**
         * Set an int value in the preferences editor, and commit.
         * 
         * @param key
         *            The name of the preference to modify.
         * @param value
         *            The new value for the preference.
         */
        public void commitInt(String key, int value) {
            if (mEditor == null)
                return;
            mEditor.putInt(key, value);
            mEditor.commit();
        }

        /**
         * Set a long value in the preferences editor, and commit.
         * 
         * @param key
         *            The name of the preference to modify.
         * @param value
         *            The new value for the preference.
         */
        public void commitLong(String key, Long value) {
            if (mEditor == null)
                return;
            mEditor.putLong(key, value);
            mEditor.commit();
        }

        /**
         * Set a String value in the preferences editor, and commit.
         * 
         * @param key
         *            The name of the preference to modify.
         * @param value
         *            The new value for the preference.
         */
        public void commitString(String key, String value) {
            if (mEditor == null)
                return;
            mEditor.putString(key, value);
            mEditor.commit();
        }

        /**
         * Set an Image value in the preferences editor, and commit.
         * 
         * @param key
         *            The name of the preference to modify.
         * @param value
         *            The new value for the preference.
         */
        public void commitImage(String key, Bitmap image) {
            String string = null;

            ByteArrayOutputStream bStream = new ByteArrayOutputStream();

            image.compress(CompressFormat.PNG, 100, bStream);

            byte[] bytes = bStream.toByteArray();

            string = Base64.encodeToString(bytes, Base64.DEFAULT);

            mEditor.putString(key, string);

            mEditor.commit();
        }

        /**
         * Set an Object value in the preferences editor, and commit.
         * 
         * @param key
         *            The name of the preference to modify.
         * @param value
         *            The new value for the preference.
         */
        public void commitObject(String key, Object object) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                mObjectlist.clear();
                mObjectlist.add(object);
                oos.writeObject(mObjectlist);
                String strList = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
                mEditor.putString(key, strList);
                mEditor.commit();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Retrieve a boolean value from the preferences.
         * 
         * @param key
         *            The name of the preference to retrieve.
         * @param defValue
         *            Value to return if this preference does not exist.
         * @return Returns the preference value if it exists, or defValue.
         *         Throws ClassCastException if there is a preference with this
         *         name that is not a boolean.
         */
        public boolean getBoolean(String key, boolean defValue) {
            if (mPreference == null)
                return defValue;
            return mPreference.getBoolean(key, defValue);
        }

        /**
         * Retrieve a float value from the preferences.
         * 
         * @param key
         *            The name of the preference to retrieve.
         * @param defValue
         *            Value to return if this preference does not exist.
         * @return Returns the preference value if it exists, or defValue.
         *         Throws ClassCastException if there is a preference with this
         *         name that is not a float.
         */
        public float getFloat(String key, Float defValue) {
            if (mPreference == null)
                return defValue;
            return mPreference.getFloat(key, defValue);
        }

        /**
         * Retrieve an int value from the preferences.
         * 
         * @param key
         *            The name of the preference to retrieve.
         * @param defValue
         *            Value to return if this preference does not exist.
         * @return Returns the preference value if it exists, or defValue.
         *         Throws ClassCastException if there is a preference with this
         *         name that is not an int.
         */
        public int getInt(String key, int defValue) {
            if (mPreference == null)
                return defValue;
            return mPreference.getInt(key, defValue);
        }

        /**
         * Retrieve a long value from the preferences.
         * 
         * @param key
         *            The name of the preference to retrieve.
         * @param defValue
         *            Value to return if this preference does not exist.
         * @return Returns the preference value if it exists, or defValue.
         *         Throws ClassCastException if there is a preference with this
         *         name that is not a long.
         */
        public long getLong(String key, Long defValue) {
            if (mPreference == null)
                return defValue;
            return mPreference.getLong(key, defValue);
        }

        /**
         * Retrieve a String value from the preferences.
         * 
         * @param key
         *            The name of the preference to retrieve.
         * @param defValue
         *            Value to return if this preference does not exist.
         * @return Returns the preference value if it exists, or defValue.
         *         Throws ClassCastException if there is a preference with this
         *         name that is not a String.
         */
        public String getString(String key, String defValue) {
            if (mPreference == null)
                return defValue;
            return mPreference.getString(key, defValue);
        }

        /**
         * Retrieve an Image value from the preferences.
         * 
         * @param key
         *            The name of the preference to retrieve.
         * @return Returns the preference value if it exists, or null. Throws
         *         ClassCastException if there is a preference with this name
         *         that is not an Image.
         */
        public Bitmap getImage(String key) {

            Bitmap bitmap = null;
            String string = mPreference.getString(key, "");
            if (string == null) {
                return null;
            }
            try {
                byte[] bitmapArray;
                bitmapArray = Base64.decode(string, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        /**
         * Retrieve an Object value from the preferences.
         * 
         * @param key
         *            The name of the preference to retrieve.
         * @return Returns the preference value if it exists, or null. Throws
         *         ClassCastException if there is a preference with this name
         *         that is not an Object.
         */
        @SuppressWarnings("unchecked")
        public Object getObject(String key, Object defValue) {
            String strObject = mPreference.getString(key, "");
            if (TextUtils.isEmpty(strObject)) {
                return null;
            }
            byte[] buffer = Base64.decode(strObject.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            try {
                ObjectInputStream oIstream = new ObjectInputStream(bais);
                mObjectlist = (List<Object>) oIstream.readObject();
                oIstream.close();
                return mObjectlist.get(0);
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return defValue;
        }

        /**
         * Mark in the editor that a preference value should be removed, and
         * commit
         * 
         * @param key
         *            The name of the preference to remove.
         */
        public void remove(String key) {
            if (mEditor == null)
                return;
            mEditor.remove(key);
            mEditor.commit();
        }

        /**
         * Get the SharedPreferences instance of this class
         * 
         * @return The SharedPreferences instance
         */
        public SharedPreferences getSharedPreferences() {
            return mPreference;
        }
    }
}
