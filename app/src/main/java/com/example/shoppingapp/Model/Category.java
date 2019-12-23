package com.example.shoppingapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Category {

    @SerializedName("data")
    @Expose
    public List<information> data = null;

    public class information{

        @SerializedName("id")
        @Expose
        public String id;

        @SerializedName("category_name")
        @Expose
        public String categoryName;

    }
}