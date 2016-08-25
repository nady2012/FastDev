package com.fastdev.fastdevlib.util.phoneattribution;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MobileAttributionDB {

    private final static String DATA_BASE_NAME = "NumberLocation.zip";
    private final Context mContext;
    private SQLiteDatabase mSqliteDB;

    private static MobileAttributionDB sSafRCSFrameworkDB = null;

    private Object obj = new Object();

    public static synchronized MobileAttributionDB getInstance(Context context) {

        if (context == null) {
            // invalid condition, database must be related to a context
            return null;
        }

        if (sSafRCSFrameworkDB == null) {
            synchronized (MobileAttributionDB.class) {
                if (sSafRCSFrameworkDB == null) {
                    sSafRCSFrameworkDB = new MobileAttributionDB(context);
                }
            }
        }

        return sSafRCSFrameworkDB;
    }

    /**
     * Constructor of SafRCSFrameworkDB
     * 
     * @param context
     *            context to construct SafRCSFrameworkDB object
     */
    private MobileAttributionDB(Context context) {
        mContext = context;
        synchronized (obj) {

            if (context != null) {
                mSqliteDB = initDB(context);
            }
        }
    }

    // ALL TABLE NAME
    public final static String TABLE_NAME_NUMBER0 = "number0";
    // 13
    public final static String TABLE_NAME_NUMBER130 = "number130";
    public final static String TABLE_NAME_NUMBER131 = "number131";
    public final static String TABLE_NAME_NUMBER132 = "number132";
    public final static String TABLE_NAME_NUMBER133 = "number133";
    public final static String TABLE_NAME_NUMBER134 = "number134";
    public final static String TABLE_NAME_NUMBER135 = "number135";
    public final static String TABLE_NAME_NUMBER136 = "number136";
    public final static String TABLE_NAME_NUMBER137 = "number137";
    public final static String TABLE_NAME_NUMBER138 = "number138";
    public final static String TABLE_NAME_NUMBER139 = "number139";
    // 14
    public final static String TABLE_NAME_NUMBER140 = "number140";
    public final static String TABLE_NAME_NUMBER141 = "number141";
    public final static String TABLE_NAME_NUMBER142 = "number142";
    public final static String TABLE_NAME_NUMBER143 = "number143";
    public final static String TABLE_NAME_NUMBER144 = "number144";
    public final static String TABLE_NAME_NUMBER145 = "number145";
    public final static String TABLE_NAME_NUMBER146 = "number146";
    public final static String TABLE_NAME_NUMBER147 = "number147";
    public final static String TABLE_NAME_NUMBER148 = "number148";
    public final static String TABLE_NAME_NUMBER149 = "number149";
    // 15
    public final static String TABLE_NAME_NUMBER150 = "number150";
    public final static String TABLE_NAME_NUMBER151 = "number151";
    public final static String TABLE_NAME_NUMBER152 = "number152";
    public final static String TABLE_NAME_NUMBER153 = "number153";
    public final static String TABLE_NAME_NUMBER154 = "number154";
    public final static String TABLE_NAME_NUMBER155 = "number155";
    public final static String TABLE_NAME_NUMBER156 = "number156";
    public final static String TABLE_NAME_NUMBER157 = "number157";
    public final static String TABLE_NAME_NUMBER158 = "number158";
    public final static String TABLE_NAME_NUMBER159 = "number159";

    // 18
    public final static String TABLE_NAME_NUMBER180 = "number180";
    public final static String TABLE_NAME_NUMBER181 = "number181";
    public final static String TABLE_NAME_NUMBER182 = "number182";
    public final static String TABLE_NAME_NUMBER183 = "number183";
    public final static String TABLE_NAME_NUMBER184 = "number184";
    public final static String TABLE_NAME_NUMBER185 = "number185";
    public final static String TABLE_NAME_NUMBER186 = "number186";
    public final static String TABLE_NAME_NUMBER187 = "number187";
    public final static String TABLE_NAME_NUMBER188 = "number188";
    public final static String TABLE_NAME_NUMBER189 = "number189";

    public final static String[] ALL_TABLE_NAME = { TABLE_NAME_NUMBER130, TABLE_NAME_NUMBER131, TABLE_NAME_NUMBER132,
            TABLE_NAME_NUMBER133, TABLE_NAME_NUMBER134, TABLE_NAME_NUMBER135, TABLE_NAME_NUMBER136,
            TABLE_NAME_NUMBER137, TABLE_NAME_NUMBER138, TABLE_NAME_NUMBER139, TABLE_NAME_NUMBER140,
            TABLE_NAME_NUMBER141, TABLE_NAME_NUMBER142, TABLE_NAME_NUMBER143, TABLE_NAME_NUMBER144,
            TABLE_NAME_NUMBER145, TABLE_NAME_NUMBER146, TABLE_NAME_NUMBER147, TABLE_NAME_NUMBER148,
            TABLE_NAME_NUMBER149, TABLE_NAME_NUMBER150, TABLE_NAME_NUMBER151, TABLE_NAME_NUMBER152,
            TABLE_NAME_NUMBER153, TABLE_NAME_NUMBER154, TABLE_NAME_NUMBER155, TABLE_NAME_NUMBER156,
            TABLE_NAME_NUMBER157, TABLE_NAME_NUMBER158, TABLE_NAME_NUMBER159, TABLE_NAME_NUMBER180,
            TABLE_NAME_NUMBER181, TABLE_NAME_NUMBER182, TABLE_NAME_NUMBER183, TABLE_NAME_NUMBER184,
            TABLE_NAME_NUMBER185, TABLE_NAME_NUMBER186, TABLE_NAME_NUMBER187, TABLE_NAME_NUMBER188,
            TABLE_NAME_NUMBER189 };

    public final static List<String> TABLE_NAME_LIST = Arrays.asList(ALL_TABLE_NAME);

    public final static String COLOMN_PARAM_ID = "id";
    public final static String COLOMN_PARAM_MOBILE = "mobile";
    public final static String COLOMN_PARAM_PROVINCE = "province";
    public final static String COLOMN_PARAM_CITY = "city";
    public final static String COLOMN_PARAM_CORP = "corp";
    public final static String COLOMN_PARAM_AREACODE = "areacode";
    public final static String COLOMN_PARAM_POSTCODE = "postcode";

    public List<String> getAllNumberTablename() {
        List<String> list = new ArrayList<String>();
        for (int i = 30; i <= 89; i++) {
            if (i >= 60 && i < 70)
                continue;
            list.add("number1" + i);
        }
        return list;
    }

    public String getCity(String mobile) {
        if (mSqliteDB == null) {
            mSqliteDB = initDB(mContext);
        }
        String city = "";
        String where = "mobile=?";
        Cursor cursor = mSqliteDB.query(getTable(mobile), new String[] { COLOMN_PARAM_CITY }, where,
                    new String[] { mobile }, null, null, null);
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                city = cursor.getString(0);
            }
            cursor.close();
        }
        return city;
    }

    /**
     * 
     * @方法名：getMobileNumber
     * @描述：查询手机号码
     * @param mobile
     * @return
     * @输出：MobileNumber
     * 
     */
    public MobileAttribution queryMobileNumber(String mobile) {
        if (mSqliteDB == null) {
            mSqliteDB = initDB(mContext);
        }

        if (mobile == null || mobile.length() == 0) {
            return null;
        }
        String tableName = getTable(mobile);
        if (!isTabExist(tableName)) {
            // 没有对应的table
            return null;
        }
        MobileAttribution number = new MobileAttribution();
        String where = COLOMN_PARAM_MOBILE + "=?";
        Cursor cursor = mSqliteDB.query(tableName, null, where, new String[] { mobile }, null, null, null);
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                number.setCorp(cursor.getString(cursor.getColumnIndex(COLOMN_PARAM_CORP)));
                number.setId(cursor.getString(cursor.getColumnIndex(COLOMN_PARAM_ID)));
                number.setMobile(cursor.getString(cursor.getColumnIndex(COLOMN_PARAM_MOBILE)));
                number.setProvince(cursor.getString(cursor.getColumnIndex(COLOMN_PARAM_PROVINCE)));
                number.setCity(cursor.getString(cursor.getColumnIndex(COLOMN_PARAM_CITY)));
                number.setAreacode(cursor.getString(cursor.getColumnIndex(COLOMN_PARAM_AREACODE)));
                number.setPostcode(cursor.getString(cursor.getColumnIndex(COLOMN_PARAM_POSTCODE)));
            }
            cursor.close();
        }
        return number;
    }

    private boolean isTabExist(String tableName) {
        if (TextUtils.isEmpty(tableName)) {
            // 没有对应的table
            return false;
        }
        for (String tabList : TABLE_NAME_LIST) {
            if (tabList.contains(tableName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @方法名：queryLandlineNumber
     * @描述：查询座机归属地
     * @param mobileList
     * @return
     * @输出：MobileNumber
     * 
     */
    public MobileAttribution queryLandlineNumber(ArrayList<String> mobileList) {
        if (mSqliteDB == null) {
            mSqliteDB = initDB(mContext);
        }

        if (mobileList == null || mobileList.size() == 0) {
            return null;
        }
        MobileAttribution number = new MobileAttribution();
        StringBuffer where = new StringBuffer();
        where.append(COLOMN_PARAM_AREACODE);
        where.append("=?");
        for (int i = 1; i < mobileList.size(); i++) {
            where.append(" or ");
            where.append(COLOMN_PARAM_AREACODE);
            where.append("=?");
        }
        String[] areaCode = new String[mobileList.size()];
        mobileList.toArray(areaCode);
        Cursor cursor = mSqliteDB.query(TABLE_NAME_NUMBER0, null, where.toString(), areaCode, null, null, null);
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                number.setId(cursor.getString(cursor.getColumnIndex(COLOMN_PARAM_ID)));
                number.setMobile(cursor.getString(cursor.getColumnIndex(COLOMN_PARAM_MOBILE)));
                number.setProvince(cursor.getString(cursor.getColumnIndex(COLOMN_PARAM_PROVINCE)));
                number.setCity(cursor.getString(cursor.getColumnIndex(COLOMN_PARAM_CITY)));
                number.setAreacode(cursor.getString(cursor.getColumnIndex(COLOMN_PARAM_AREACODE)));
                number.setPostcode(cursor.getString(cursor.getColumnIndex(COLOMN_PARAM_POSTCODE)));
            }
            cursor.close();
        }
        return number;
    }

    public long saveOrUpdate(MobileAttribution number) {
        // check if same plugin exists
        if (mSqliteDB == null) {
            mSqliteDB = initDB(mContext);
        }
        if (numberHasExist(number)) {
            return updateNumbers(number);
        }
        try {

            final ContentValues cv = new ContentValues();

            cv.put(COLOMN_PARAM_ID, number.getId());
            cv.put(COLOMN_PARAM_AREACODE, number.getAreacode());
            cv.put(COLOMN_PARAM_CITY, number.getCity());
            cv.put(COLOMN_PARAM_CORP, number.getCorp());
            cv.put(COLOMN_PARAM_POSTCODE, number.getPostcode());
            cv.put(COLOMN_PARAM_PROVINCE, number.getProvince());
            final long result = mSqliteDB.insert(getTable(number.getMobile()), "", cv);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long insertNumber(MobileAttribution number) {

        // check if same plugin exists
        if (mSqliteDB == null) {
            mSqliteDB = initDB(mContext);
        }

        try {

            final ContentValues cv = new ContentValues();

            cv.put(COLOMN_PARAM_MOBILE, number.getMobile());
            cv.put(COLOMN_PARAM_ID, number.getId());
            cv.put(COLOMN_PARAM_AREACODE, number.getAreacode());
            cv.put(COLOMN_PARAM_CITY, number.getCity());
            cv.put(COLOMN_PARAM_CORP, number.getCorp());
            cv.put(COLOMN_PARAM_POSTCODE, number.getPostcode());
            cv.put(COLOMN_PARAM_PROVINCE, number.getProvince());
            final long result = mSqliteDB.insert(getTable(number.getMobile()), "", cv);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long updateNumbers(MobileAttribution number) {
        long updateResult = 0;
        ContentValues cv = new ContentValues();
        cv.put(COLOMN_PARAM_ID, number.getId());
        cv.put(COLOMN_PARAM_AREACODE, number.getAreacode());
        cv.put(COLOMN_PARAM_CITY, number.getCity());
        cv.put(COLOMN_PARAM_CORP, number.getCorp());
        cv.put(COLOMN_PARAM_POSTCODE, number.getPostcode());
        cv.put(COLOMN_PARAM_PROVINCE, number.getProvince());
        if (cv.size() > 0) {
            String where = COLOMN_PARAM_MOBILE + "='" + number.getMobile() + "'";
            try {
                updateResult = mSqliteDB.update(getTable(number.getMobile()), cv, where, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return updateResult;
    }

    public boolean numberHasExist(MobileAttribution number) {
        if (mSqliteDB == null) {
            mSqliteDB = initDB(mContext);
        }
        boolean hasExist = false;
        Cursor cursor = mSqliteDB.query(getTable(number.getMobile()), new String[] { COLOMN_PARAM_MOBILE },
                    COLOMN_PARAM_MOBILE + " = '" + number.getMobile() + "'", null, null, null, null);
        if (null != cursor) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                hasExist = true;
            }
            cursor.close();
        }
        return hasExist;
    }

    public String getTable(String mobile) {
        if (mobile == null || "".equals(mobile)) {
            return "";
        } else {
            return "number" + mobile.substring(0, 3);
        }
    }

    /**
     * 
     * @方法名：initDB
     * @描述：初始化数据库
     * @输出：void
     * @作者：mss
     * 
     */
    private SQLiteDatabase initDB(Context context) {
        AssetsDatabaseManager.initManager(context);
        AssetsDatabaseManager mg = AssetsDatabaseManager.getAssetsDatabaseManager();
        return mg.getDatabase(DATA_BASE_NAME);
    }

    /**
     * 
     * @方法名：closeDB
     * @描述：关闭数据库连接
     * @输出：void
     * @作者：mss
     *
     */
    public void closeDB() {
        if (mSqliteDB != null && mSqliteDB.isOpen()) {
            mSqliteDB.close();
        }
    }
}
