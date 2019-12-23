package com.example.shoppingapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductCategory {

    @SerializedName("data")
    @Expose
    public List<information> data = null;

    public class information{

        @SerializedName("id")
        @Expose
        public String id;
        
        @SerializedName("category_id")
        @Expose
        public String categoryId;

        @SerializedName("subcategory_id")
        @Expose
        public String subcategoryId;

        @SerializedName("product_title")
        @Expose
        public String productTitle;

        @SerializedName("image1")
        @Expose
        public String image1;

        @SerializedName("price")
        @Expose
        public String price;
    }
}