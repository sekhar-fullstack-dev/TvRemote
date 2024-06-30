package com.example.tvremote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = Constants.URL+"/"; // Use your server IP and port
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String mobileIP) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://"+mobileIP+":3000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

