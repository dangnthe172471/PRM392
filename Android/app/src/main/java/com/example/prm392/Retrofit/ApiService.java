package com.example.prm392.Retrofit;

import com.example.prm392.model.LoginRequest;
import com.example.prm392.model.RegisterRequest;
import com.example.prm392.model.LoginResponse;
import com.example.prm392.model.UserProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiService api = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5266/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/Auth/register")
    Call<LoginResponse> register(@Body RegisterRequest request);

    @GET("api/User/profile")
    Call<UserProfile> getUserProfile(@Query("userId") int userId);
} 