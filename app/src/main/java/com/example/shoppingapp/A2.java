package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class A2 extends AppCompatActivity {

    Context c;
    ListView a2_lv;
    Offline offline;
    int id = 0;
    String category_id;
    ArrayList ar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a2);
        setTitle("It's A2");

        c = A2.this;
        a2_lv = findViewById(R.id.a2_lv);
        offline = new Offline(c);
        category_id = getIntent().getStringExtra("category_id");

        displaydata();
    }
    class Custom extends BaseAdapter {

        @Override
        public int getCount() {
            return ar.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View v, ViewGroup parent) {
            v = LayoutInflater.from(c).inflate(R.layout.single_layout_sub2,parent,false);

            final TextView Sub_tvid= v.findViewById(R.id.Sub_tvid);
            final TextView Sub_tvcatid = v.findViewById(R.id.Sub_tvcatid);
            final TextView Sub_tvsubcatname = v.findViewById(R.id.Sub_tvsubcatname);

            final ContentValues cv = (ContentValues) ar.get(i);

            Sub_tvid.setText(cv.getAsString("category_id"));
            Sub_tvcatid.setText(cv.getAsString("subcategory_id"));
            Sub_tvsubcatname.setText(cv.getAsString("subcategory_name"));

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(c,A3.class);
                    i.putExtra("subcategory_id",cv.getAsString("subcategory_id"));
                    startActivity(i);
                }
            });

            return v;
        }
    }
    void displaydata(){
        ar = new ArrayList();
        SQLiteDatabase db = offline.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from subcategory where category_id="+category_id, null);

        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                ContentValues cv = new ContentValues();
                cv.put("id",cursor.getInt(0));
                cv.put("category_id",cursor.getString(1));
                cv.put("subcategory_id",cursor.getString(2));
                cv.put("subcategory_name",cursor.getString(3));
                ar.add(cv);
            }
            Custom adapter = new Custom();
            a2_lv.setAdapter(adapter);
        }
    }
}
