package com.example.shoppingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.shoppingapp.Service.IMAGE;

public class Bookmark extends AppCompatActivity {

    Context c;
    ListView dlv;
    DBMain objdb;
    int id = 0;
    ArrayList ar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        c = Bookmark.this;
        dlv = findViewById(R.id.dlv);
        objdb = new DBMain(c);

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
            v = LayoutInflater.from(c).inflate(R.layout.display_layout2,parent,false);

            ImageView bm_pro_img = v.findViewById(R.id.bm_pro_img);
            final TextView bm_product_name = v.findViewById(R.id.bm_product_name);
            final TextView bm_product_price = v.findViewById(R.id.bm_product_price);
            final TextView bm_product_qty = v.findViewById(R.id.bm_product_qty);
//            final TextView bm_pp_total = v.findViewById(R.id.bm_pp_total);
//            Button btn_dlt = v.findViewById(R.id.btn_dlt);
            ImageView cart_plus_qty = v.findViewById(R.id.cart_plus_qty);
            ImageView cart_minus_qty = v.findViewById(R.id.cart_minus_qty);

            final ContentValues cv = (ContentValues) ar.get(i);

//            bm_pro_img.setText(cv.getAsString("pro_img"));
            bm_product_name.setText(cv.getAsString("prodect_name"));
            bm_product_price.setText("$"+cv.getAsInteger("product_price"));
            bm_product_qty.setText(""+cv.getAsInteger("bm_qty"));
            final Integer[] qty = {cv.getAsInteger("bm_qty")};

            cart_plus_qty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    qty[0]++;
                    bm_product_qty.setText(qty[0]);
                }
            });

            cart_minus_qty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    qty[0]--;
                    bm_product_qty.setText(qty[0]);
                }
            });

            int total = cv.getAsInteger("product_price")*cv.getAsInteger("bm_qty");
//            bm_pp_total.setText("Total : "+total);
            Picasso.with(c)
                    .load(IMAGE+cv.getAsString("prodect_image"))
                    .placeholder(R.color.colorAccent)
                    .error(R.color.colorAccent)
                    .into(bm_pro_img);
/*            btn_dlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ar = new ArrayList();
                    SQLiteDatabase db = objdb.getReadableDatabase();
                    long a = db.delete("user","id="+cv.getAsInteger("id"),null);
                    if (a != -1){
                        Toast.makeText(c,"Delete Data",Toast.LENGTH_SHORT).show();
                        displaydata();
                    }
                }
            });*/

            return v;
        }
    }

    void displaydata(){
        ar = new ArrayList();
        SQLiteDatabase db = objdb.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user", null);

        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                ContentValues cv = new ContentValues();
                cv.put("id",cursor.getInt(0));
                cv.put("main_id",cursor.getString(1));
                cv.put("cat_id",cursor.getString(2));
                cv.put("subcat_id",cursor.getString(3));
                cv.put("prodect_name",cursor.getString(4));
                cv.put("product_price",cursor.getInt(5));
                cv.put("bm_qty",cursor.getInt(6));
                cv.put("prodect_image",cursor.getString(7));
                ar.add(cv);
            }
            Custom adapter = new Custom();
            dlv.setAdapter(adapter);
        }
    }
}
