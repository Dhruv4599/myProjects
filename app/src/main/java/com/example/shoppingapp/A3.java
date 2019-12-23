package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.shoppingapp.Service.IMAGE;

public class A3 extends AppCompatActivity {

    Context c;
    RecyclerView a3_lv;
    Offline offline;
    int id = 0;
    String subcategory_id;
    ArrayList ar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a3);
        setTitle("Hello I m A3");

        c = A3.this;
        a3_lv = findViewById(R.id.a3_lv);
        offline = new Offline(c);
        subcategory_id = getIntent().getStringExtra("subcategory_id");

        displaydata();
    }

    class Custom extends RecyclerView.Adapter<Custom.VH> {

        public class VH extends RecyclerView.ViewHolder {
            ImageView product_image;
            TextView product_name,product_price;
            public VH(@NonNull View v) {
                super(v);
                product_image = v.findViewById(R.id.product_image);
                product_name = v.findViewById(R.id.product_name);
                product_price = v.findViewById(R.id.product_price);
            }
        }

        @NonNull
        @Override
        public Custom.VH onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v = LayoutInflater.from(c).inflate(R.layout.single_layout_product2,parent,false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH vh, int i) {

            ContentValues cv = (ContentValues) ar.get(i);

            Picasso.with(c)
                    .load(IMAGE+cv.getAsString("productcategory_image"))
                    .placeholder(R.color.colorAccent)
                    .error(R.color.colorAccent)
                    .into(vh.product_image);
            vh.product_name.setText(cv.getAsString("productcategory_name"));
            vh.product_price.setText(cv.getAsString("productcategory_price"));
        }

        @Override
        public int getItemCount() {
            return ar.size();
        }
    }

    void displaydata(){
        ar = new ArrayList();
        SQLiteDatabase db = offline.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from productcategory where subcategory_id="+subcategory_id, null);

        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                ContentValues cv = new ContentValues();
                cv.put("id",cursor.getInt(0));
                cv.put("subcategory_id",cursor.getString(1));
                cv.put("productcategory_id",cursor.getString(2));
                cv.put("productcategory_name",cursor.getString(3));
                cv.put("productcategory_price",cursor.getString(4));
                cv.put("productcategory_image",cursor.getString(5));
                ar.add(cv);
            }
            Custom adapter = new Custom();
            RecyclerView.LayoutManager lm = new GridLayoutManager(c,2);
            a3_lv.setLayoutManager(lm);
            a3_lv.setAdapter(adapter);
        }
    }
}
