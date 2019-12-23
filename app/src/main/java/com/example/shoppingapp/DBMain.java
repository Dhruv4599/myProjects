package com.example.shoppingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBMain extends SQLiteOpenHelper {
    public DBMain(Context c) {
        super(c, "DB101", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists user (id integer primary key,main_id text,cat_id text,subcat_id text,prodect_name text,product_price integer,bm_qty integer,prodect_image text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table user";
        db.execSQL(sql);

        this.onCreate(db);
    }
}
