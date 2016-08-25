package com.fastdev.fastdevlib.database.util;

public class XSPDataBaseConstant {

    public static final String DATABASE_NAME = "cipher_calls_db";

    public static final int VERSION = 1;

    public static final String DATABASE_TABLE_CALL = "calls";

    public static final String DATABASE_TABLE_CALL_SQL = "create table " + DATABASE_TABLE_CALL
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "call_id INTEGER," + "number TEXT," + "name TEXT,"
            + "date INTEGER," + "duration INTEGER," + "type INTEGER," + "new INTEGER,"
            + "contact_id TEXT DEFAULT '-1')";
}
