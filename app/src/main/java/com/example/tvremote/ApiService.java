package com.example.tvremote;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("getVideos")
    Call<List<VideoItem>> getVideos();
}

