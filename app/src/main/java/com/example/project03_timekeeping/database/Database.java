package com.example.project03_timekeeping.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context) {
        super(context, "sql.database", null, 1);
    }

    public void queryData(String sql){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(sql);
    }
    public Cursor getData(String sql){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(sql, null);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
//        db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS Info(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(100), keyName INTEGER, morning INTEGER, evening INTEGER, alls INTEGER, absent BOOLEAN, month VARCHAR(100), date VARCHAR(100), note VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Member(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(100))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
