package com.example.user.musicappdemo.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by user on 2017/4/11.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    public static final String CREATE_USER="create table User(id integer primary key autoincrement,userName text,userPwd text,sex text,nickName text,phone text,content text)";

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建用户表
        db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.equals("drop table if exits UserInfo");
        db.execSQL("drop table if exists User");
        onCreate(db);
        Log.d("onUpgrade", "onUpgrade: 升级");
    }
}
