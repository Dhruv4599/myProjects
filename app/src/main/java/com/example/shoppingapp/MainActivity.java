package com.example.shoppingapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.shoppingapp.Model.Category;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Context c;
    TextView loader,ForError;
    ListView lv;
    AdView ad;
    Main main;
    Service service;
    String TAG = "==MSG==";
    List<Category.information> categorydata = new ArrayList<>();
    SharedPreferences sh;
    Boolean firstrun = true;
    String newdata;
    NotificationHandler nHandler;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        c = MainActivity.this;
        main = new Main(c);
        nHandler = NotificationHandler.getInstance(this);
        service = main.service;
        loader = findViewById(R.id.loader);
        ForError = findViewById(R.id.ForError);
        lv = findViewById(R.id.lv);
        button = findViewById(R.id.button);
        sh = getSharedPreferences("data",MODE_PRIVATE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nHandler.createSimpleNotification(c);
            }
        });

        ad = findViewById(R.id.adView);

        ad = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        ad.loadAd(adRequest);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASEPATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service service = retrofit.create(Service.class);


        fatchdata();
    }

    void fatchdata(){
        Call<Category> data = service.getCategory(Service.APIKEY);

        try {
            data.enqueue(new Callback<Category>() {
                @Override
                public void onResponse(retrofit2.Call<Category> call, Response<Category> response) {
                    if (response != null){
                        loader.setVisibility(View.GONE);
                        List<Category.information> categorydata = response.body().data;
                        Custom adapter = new Custom(categorydata);
                        lv.setAdapter(adapter);
                    }else{
                        Toast.makeText(c,"Sorry, Response Return Null",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Category> call, Throwable t) {
                    Toast.makeText(c,"Sorry data not found",Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            ForError.setText("Error = "+e);
        }
    }

    class Custom extends BaseAdapter {

        List<Category.information> data;

        public Custom(List<Category.information> data) {
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
            v = LayoutInflater.from(c).inflate(R.layout.single_layout,parent,false);

            final TextView tvid = v.findViewById(R.id.tvid);
            TextView tvname = v.findViewById(R.id.tvname);

            tvid.setText(""+data.get(position).id);
            tvname.setText(""+data.get(position).categoryName);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(c,""+data.get(position).id,Toast.LENGTH_SHORT).show();
                    if (data.get(position).id != null){
                        Intent i = new Intent(c,SubActivity.class);
                        i.putExtra("catid",data.get(position).id);
                        i.putExtra("catname",data.get(position).categoryName);
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
        getMenuInflater().inflate(R.menu.cart_icon, menu);
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
                Intent i = new Intent(c,Bookmark.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void addNotification()
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("John's Android Studio Tutorials")
                .setContentText("A video has just arrived!");
        Log.d("YES","yes"+builder.toString());
        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
