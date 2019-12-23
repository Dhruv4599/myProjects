package com.example.shoppingapp;

import android.content.Context;
import android.widget.Toast;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class Main {
    Service service;
    private Context c;
    Main(Context c) {
        this.c = c;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASEPATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(Service.class);

    }

    void showmsg(String msg){
        Toast.makeText(c,msg, Toast.LENGTH_LONG).show();
    }
}
