package com.fastdev.fastdevlib.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.CallLog.Calls;

import com.fastdev.fastdevlib.database.util.XSPDataBaseConstant;


public class CallsContentProvider extends ContentProvider {

    private static final UriMatcher URI_MATCHER;

    public final static String AUTHORITY = "com.huawei.cipher.common.database.CallsContentProvider";

    private DataBaseSQLiteHelper dataBaseSQLiteHelper;

    private static final int CALLS = 1;
    private static final int CALLS_ID = 2;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, XSPDataBaseConstant.DATABASE_TABLE_CALL, CALLS);
        URI_MATCHER.addURI(AUTHORITY, XSPDataBaseConstant.DATABASE_TABLE_CALL + "/#", CALLS_ID);
    }

    @Override
    public String getType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case 1:
                return "vnd.android.cursor.dir/" + XSPDataBaseConstant.DATABASE_TABLE_CALL;
            case 2:
                return "vnd.android.cursor.item/" + XSPDataBaseConstant.DATABASE_TABLE_CALL;
        }
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    @Override
    public boolean onCreate() {
        dataBaseSQLiteHelper = new DataBaseSQLiteHelper(getContext());
        return false;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = this.dataBaseSQLiteHelper.getWritableDatabase();
        ContentValues copiedValues = new ContentValues(values);

        long rowId = db.insert(XSPDataBaseConstant.DATABASE_TABLE_CALL, null, copiedValues);
        if (rowId > 0L) {
            notifyChange(uri);
            return ContentUris.withAppendedId(uri, rowId);
        }
        notifyChange(uri);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.dataBaseSQLiteHelper.getWritableDatabase();
        int matchedUriId = URI_MATCHER.match(uri);
        switch (matchedUriId) {
            case 1:
                int ret = db.delete(XSPDataBaseConstant.DATABASE_TABLE_CALL, selection, selectionArgs);
                notifyChange(uri);
                return ret;
        }
        throw new UnsupportedOperationException("Cannot delete that URL: " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String limitClause = null;

        SQLiteDatabase db = this.dataBaseSQLiteHelper.getReadableDatabase();
        Cursor c = db.query(XSPDataBaseConstant.DATABASE_TABLE_CALL, projection, selection, selectionArgs, null, null,
                sortOrder, limitClause);
        if (c != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.dataBaseSQLiteHelper.getWritableDatabase();
        int ret = db.update(XSPDataBaseConstant.DATABASE_TABLE_CALL, values, selection, selectionArgs);

        notifyChange(uri);

        return ret;
    }

    private void notifyChange(Uri uri) {
        ContentResolver cr = getContext().getContentResolver();
        cr.notifyChange(uri, null);
        cr.notifyChange(Calls.CONTENT_URI, null);
    }
}
