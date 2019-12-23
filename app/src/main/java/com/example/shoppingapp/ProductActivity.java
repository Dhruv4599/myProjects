package com.example.shoppingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Model.ProductCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.shoppingapp.Service.IMAGE;

public class ProductActivity extends AppCompatActivity {


    Context c;
    TextView Productloader,ProductForError;
    RecyclerView Productrv;
    DBMain objdb;
    Main main;
    Service service;
    String TAG = "==MSG==";
    List<ProductCategory.information> categorydata = new ArrayList<>();
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        c = ProductActivity.this;
        main = new Main(c);
        service = main.service;
        Productloader = findViewById(R.id.Productloader);
        ProductForError = findViewById(R.id.ProductForError);
        Productrv = findViewById(R.id.Productrv);
        objdb = new DBMain(c);

        String subcatid = getIntent().getStringExtra("subcatid");
        String subcatname = getIntent().getStringExtra("subcatname");
        if (subcatid != null){
            setTitle(subcatname);
            fatchdata(subcatid);
        }
    }
    void fatchdata(String subcatid) {
        Call<ProductCategory> Productdata = service.getProductCategory(Service.APIKEY,subcatid);

        try {
            Productdata.enqueue(new Callback<ProductCategory>() {
                @Override
                public void onResponse(Call<ProductCategory> call, Response<ProductCategory> response) {
                    if (response != null){
                        Productloader.setVisibility(View.GONE);
                        categorydata = response.body().data;
                        Custom adapter = new Custom(categorydata);
                        RecyclerView.LayoutManager lm = new GridLayoutManager(c,2);
                        Productrv.setLayoutManager(lm);
                        Productrv.setAdapter(adapter);
                    }else{
                        Toast.makeText(c,"Sorry, Response Return Null", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProductCategory> call, Throwable t) {
                    Toast.makeText(c,"Sorry data not found", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            ProductForError.setText("Error = "+e);
        }

    }

    class Custom extends RecyclerView.Adapter<Custom.VH>{

        List<ProductCategory.information> data;
        public Custom(List<ProductCategory.information> data) {
            this.data = data;
        }

        private class VH extends RecyclerView.ViewHolder {
            ImageView product_image;
            final TextView product_name,product_price;
            Button add_bm;
            public VH(@NonNull View v) {
                super(v);

                product_image = v.findViewById(R.id.product_image);
                product_name = v.findViewById(R.id.product_name);
                product_price = v.findViewById(R.id.product_price);
                add_bm = v.findViewById(R.id.add_bm);
            }
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(c).inflate(R.layout.single_layout_product,viewGroup,false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH vh, int i) {
            final ProductCategory.information thisData = data.get(i);
            Picasso.with(c)
                    .load(IMAGE+thisData.image1)
                    .placeholder(R.color.colorAccent)
                    .error(R.color.colorAccent)
                    .into(vh.product_image);
            vh.product_price.setText(thisData.price+" rs.");
            vh.product_name.setText(thisData.productTitle);
            vh.add_bm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder dlg = new AlertDialog.Builder(c);
                    View view = LayoutInflater.from(c).inflate(R.layout.add_bookmark,null);
                    dlg.setView(view);
                    final TextView product_name = view.findViewById(R.id.product_name);
                    final EditText add_qty = view.findViewById(R.id.add_qty);
                    Button add_bm_db = view.findViewById(R.id.add_bm_db);
                    product_name.setText(thisData.productTitle);

                    final AlertDialog ad = dlg.create();
                    ad.show();
                    add_bm_db.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SQLiteDatabase db = objdb.getReadableDatabase();
                            ContentValues cv = new ContentValues();
                            cv.put("main_id",thisData.id);
                            cv.put("prodect_name",thisData.productTitle);
                            cv.put("product_price",thisData.price);
                            cv.put("bm_qty",add_qty.getText().toString());
                            cv.put("prodect_image",thisData.image1);

                            Cursor cursor = db.rawQuery("select * from user where main_id="+thisData.id,null);
                            if (cursor.getCount()>0){
                                Toast.makeText(c,"Already",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            long a = db.insert("user",null,cv);
                            if(a!=-1){
                                Toast.makeText(c,"Record inserted Successfully",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(c,"Record not inserted",Toast.LENGTH_SHORT).show();
                            }
                            ad.dismiss();
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_icon, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.cart:
                Toast.makeText(c,"Item cart Selected",Toast.LENGTH_LONG).show();
                Intent i = new Intent(c,Bookmark.class);
                startActivity(i);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
