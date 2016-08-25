package com.fastdev.fastdevlib.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fastdev.fastdevlib.database.util.XSPDataBaseConstant;


public class DataBaseSQLiteHelper extends SQLiteOpenHelper {

    public DataBaseSQLiteHelper(Context context) {
        super(context, XSPDataBaseConstant.DATABASE_NAME, null, XSPDataBaseConstant.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(XSPDataBaseConstant.DATABASE_TABLE_CALL_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table IF EXISTS " + XSPDataBaseConstant.DATABASE_TABLE_CALL);
        onCreate(db);
    }

}
