package com.example.shoppingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Offline extends SQLiteOpenHelper {
    Offline(Context c) {
        super(c, "offline", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String category = "create table if not exists category (id integer primary key , category_id text , category_name text)";
        String subcategory = "create table if not exists subcategory (id integer primary key , category_id text , subcategory_id text , subcategory_name text)";
        String productcategory = "create table if not exists productcategory (id integer primary key , subcategory_id text , productcategory_id text , productcategory_name text , productcategory_price integer , productcategory_image text)";
        db.execSQL(category);
        db.execSQL(subcategory);
        db.execSQL(productcategory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String category = "drop table category";
        String subcategory = "drop table subcategory";
        String productcategory = "drop table productcategory";
        db.execSQL(category);
        db.execSQL(subcategory);
        db.execSQL(productcategory);

        this.onCreate(db);
    }
}
