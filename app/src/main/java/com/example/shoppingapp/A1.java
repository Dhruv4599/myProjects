package com.example.shoppingapp;

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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class A1 extends AppCompatActivity {

    Context c;
    ListView a1_lv;
    Offline offline;
    int id = 0;
    ArrayList ar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a1);
        setTitle("A11");

        c = A1.this;
        a1_lv = findViewById(R.id.a1_lv);
        offline = new Offline(c);

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
            v = LayoutInflater.from(c).inflate(R.layout.single_layout2,parent,false);

            final TextView tvid= v.findViewById(R.id.tvid);
            final TextView tvname = v.findViewById(R.id.tvname);

            final ContentValues cv = (ContentValues) ar.get(i);

            tvid.setText(cv.getAsString("category_id"));
            tvname.setText(cv.getAsString("category_name"));

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(c,A2.class);
                    i.putExtra("category_id",cv.getAsString("category_id"));
                    startActivity(i);
                }
            });

            return v;
        }
    }
    void displaydata(){
        ar = new ArrayList();
        SQLiteDatabase db = offline.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from category", null);

        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                ContentValues cv = new ContentValues();
                cv.put("id",cursor.getInt(0));
                cv.put("category_id",cursor.getString(1));
                cv.put("category_name",cursor.getString(2));
                ar.add(cv);
            }
            Custom adapter = new Custom();
            a1_lv.setAdapter(adapter);
        }
    }
}
