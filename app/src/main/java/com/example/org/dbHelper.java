package com.example.org;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {
    public static final String TB_NAME = "friends";

    public dbHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_NAME + " ( _id integer primary key autoincrement," +//
                "filename varchar," +
                "date varchar," +
                "state varchar,"+
                "priority varchar,"+
                "headline varchar,"+
                "tags varchar"+
                //"sc varchar,"+
                //"de varchar,"+
                //"re varchar"+
                //"content varchar"+
                ") ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }
}