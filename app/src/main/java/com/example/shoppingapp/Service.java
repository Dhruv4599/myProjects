package com.example.shoppingapp;

import com.example.shoppingapp.Model.Category;
import com.example.shoppingapp.Model.ProductCategory;
import com.example.shoppingapp.Model.SubCategory;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Service {

    String APIKEY = "8B89B11A17E191149D5382B338602B6E";
    String BASEPATH = "http://interierdesign.webtechinfoway.pw/feed/";
    String IMAGE = "http://interierdesign.webtechinfoway.pw/uploads/";
    String ID = "0";

    @FormUrlEncoded
    @POST("Category")
    Call<Category> getCategory(@Field("api_key") String apikey);

    @FormUrlEncoded
    @POST("Subcategory/bycatid")
    Call<SubCategory> getSubCategory(@Field("api_key") String apikey, @Field("cat_id") String catid);

    @FormUrlEncoded
    @POST("Interier/bysubcatid")
    Call<ProductCategory> getProductCategory(@Field("api_key") String apikey, @Field("subcat_id") String subcatid);
}