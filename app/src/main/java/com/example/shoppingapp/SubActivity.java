package com.example.shoppingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Model.ProductCategory;
import com.example.shoppingapp.Model.SubCategory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubActivity extends AppCompatActivity {

    Context c;
    TextView Subloader,SubForError;
    ListView Sublv;
    Offline offline;
    Main main;
    Service service;
    String TAG = "==MSG==";
    String catid,catname;
    List<SubCategory.information> categorydata = new ArrayList<>();
    List<ProductCategory.information> categorydata2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        c = SubActivity.this;
        offline = new Offline(c);
        main = new Main(c);
        service = main.service;
        Subloader = findViewById(R.id.Subloader);
        SubForError = findViewById(R.id.SubForError);
        Sublv = findViewById(R.id.Sublv);

        catid = getIntent().getStringExtra("catid");
        catname = getIntent().getStringExtra("catname");
        if (catid != null){
            setTitle(catname);
            fatchdata(catid);
        }
    }
    void fatchdata(String catid){
        Call<SubCategory> Subdata = service.getSubCategory(Service.APIKEY,catid);

        try {
            Subdata.enqueue(new Callback<SubCategory>() {
                @Override
                public void onResponse(Call<SubCategory> call, Response<SubCategory> response) {
                    if (response != null){
                        Subloader.setVisibility(View.GONE);
                        categorydata = response.body().data;
                        Custom adapter = new Custom(categorydata);
                        Sublv.setAdapter(adapter);

                    }else{
                        Toast.makeText(c,"Sorry, Response Return Null",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SubCategory> call, Throwable t) {
                    Toast.makeText(c,"Sorry data not found", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            SubForError.setText("Error = "+e);
        }
    }

    class Custom extends BaseAdapter {

        List<SubCategory.information> data;

        public Custom(List<SubCategory.information> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View v, ViewGroup parent) {
            v = LayoutInflater.from(c).inflate(R.layout.single_layout_sub,parent,false);

            TextView Sub_tvid = v.findViewById(R.id.Sub_tvid);
            TextView Sub_tvcatid = v.findViewById(R.id.Sub_tvcatid);
            TextView Sub_tvsubcatname = v.findViewById(R.id.Sub_tvsubcatname);

            Sub_tvid.setText(""+data.get(position).id);
            Sub_tvcatid.setText(""+data.get(position).categoryid);
            Sub_tvsubcatname.setText(""+data.get(position).subcategoryName);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(c,""+data.get(position).id,Toast.LENGTH_SHORT).show();
                    if (data.get(position).id != null){
                        Intent i = new Intent(c,ProductActivity.class);
                        i.putExtra("subcatid",data.get(position).id);
                        i.putExtra("subcatname",data.get(position).subcategoryName);
                        startActivity(i);
                    }
                }
            });
            return v;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.icon, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){

            case R.id.person:
                Toast.makeText(c,"Item person Selected",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(c,A1.class);
                startActivity(intent);
                return true;

            case R.id.cart:
                Toast.makeText(c,"Item cart Selected",Toast.LENGTH_LONG).show();
                Intent i1 = new Intent(c,Bookmark.class);
                startActivity(i1);
                return true;

            case R.id.download:
                Toast.makeText(c,"Item download Selected",Toast.LENGTH_LONG).show();
                databasep1();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void databasep1(){
        SQLiteDatabase db = offline.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("category_id",catid);
        cv.put("category_name",catname);

        Cursor cursor = db.rawQuery("select * from category where category_id="+catid,null);
/*        if (cursor.getCount()>0){
            Toast.makeText(c,"Already",Toast.LENGTH_SHORT).show();
            return;
        }*/

        long x = db.insert("category",null,cv);
        if(x!=-1){
//            Toast.makeText(c,"Record inserted Successfully",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(c,"Record not inserted",Toast.LENGTH_SHORT).show();
        }
        databasep2(catid);
    }
    void databasep2(final String catid1){
        Call<SubCategory> Subdata = service.getSubCategory(Service.APIKEY,catid1);

        try {
            Subdata.enqueue(new Callback<SubCategory>() {
                @Override
                public void onResponse(Call<SubCategory> call, Response<SubCategory> response) {
                    if (response.body().data != null && response.body().data.size() > 0){
                        for (int i=0 ; i<categorydata.size() ; i++){
                            String subcategoryid = categorydata.get(i).id;
                            SQLiteDatabase db = offline.getReadableDatabase();
                            ContentValues cv = new ContentValues();
                            cv.put("category_id",catid1);
                            cv.put("subcategory_id",subcategoryid);
                            cv.put("subcategory_name",categorydata.get(i).subcategoryName);

                            long y = db.insert("subcategory",null,cv);
                            if(y!=-1){
//                                Toast.makeText(c,"Record inserted Successfully",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(c,"Record not inserted",Toast.LENGTH_SHORT).show();
                            }
                            databasep3(subcategoryid);
                        }
                    }else{
                        Toast.makeText(c,"Sorry, Response Return Null",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SubCategory> call, Throwable t) {
                    Toast.makeText(c,"Sorry data not found", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            SubForError.setText("Error = "+e);
        }
    }
    void databasep3(final String subcatid){
        Log.d("DATA_LOG",""+subcatid);
        Call<ProductCategory> Productdata = service.getProductCategory(Service.APIKEY,subcatid);
        try {
            Productdata.enqueue(new Callback<ProductCategory>() {
                @Override
                public void onResponse(Call<ProductCategory> call, Response<ProductCategory> response) {
                    if (response != null){
                        categorydata2 = response.body().data;
                        Log.d("DATA_LOG","Categorydata size is : "+categorydata2.size());
                        for (int dt = 0; dt < categorydata2.size(); dt++){
                            String myid = categorydata2.get(dt).id;
                            SQLiteDatabase dtbs = offline.getReadableDatabase();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("subcategory_id",subcatid);
                            contentValues.put("productcategory_id",categorydata2.get(dt).id);
                            contentValues.put("productcategory_name",categorydata2.get(dt).productTitle);
                            contentValues.put("productcategory_price",categorydata2.get(dt).price);
                            contentValues.put("productcategory_image",categorydata2.get(dt).image1);
                            Log.d("DATA_LOG","dt : "+dt+" ID : "+myid);

                            long z = dtbs.insert("productcategory",null,contentValues);
                            if(z!=-1){
                                Toast.makeText(c,"Record inserted Successfully",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(c,"Record not inserted",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(c,"Sorry, Response Return Null", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProductCategory> call, Throwable t) {
                    Toast.makeText(c,"Sorry data not found"+t, Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(c,"Sorry here Exception found"+e, Toast.LENGTH_SHORT).show();
        }
    }
}
