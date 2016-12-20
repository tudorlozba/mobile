package com.android.phoneagenda;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by tudorlozba on 11/12/2016.
 */
public class LocalDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Contacts.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ContactEntry.TABLE_NAME + " (" +
                    ContactEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    ContactEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    ContactEntry.COLUMN_NUMBER + TEXT_TYPE + COMMA_SEP +
                    ContactEntry.COLUMN_EMAIL + TEXT_TYPE + COMMA_SEP +
                    ContactEntry.COLUMN_DOB + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ContactEntry.TABLE_NAME;

    public LocalDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public LocalDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public LocalDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }



    public static class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_DOB = "dob";
    }
}
