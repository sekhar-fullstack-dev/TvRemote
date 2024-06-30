package com.example.tvremote;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvremote.adapters.VideoAdapter;
import com.example.tvremote.interfaces.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryActivity extends FragmentActivity {
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private ArrayList<VideoItem> videoItems;

    private RecyclerView recyclerView2;
    private VideoAdapter adapter2;
    private ArrayList<VideoItem> videoItems2;

    private RecyclerView recyclerView3;
    private VideoAdapter adapter3;
    private ArrayList<VideoItem> videoItems3;
    private String mobileIP;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mobileIP = getIntent().getStringExtra("IP");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        videoItems = new ArrayList<>();

        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setHasFixedSize(true);
        videoItems2 = new ArrayList<>();

        recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView3.setHasFixedSize(true);
        videoItems3 = new ArrayList<>();

        // Assume you have a method fetchVideos that makes a network request to get videos
        fetchVideos();
    }

    private void fetchVideos() {
        // Use Retrofit or any other HTTP client to fetch video data
        // For example:
        try {
            ApiService apiService = ApiClient.getClient(mobileIP).create(ApiService.class);
            Call<List<VideoItem>> call = apiService.getVideos();
            call.enqueue(new Callback<List<VideoItem>>() {
                @Override
                public void onResponse(Call<List<VideoItem>> call, Response<List<VideoItem>> response) {
                    videoItems.clear();
                    videoItems.addAll(response.body());
                    videoItems2.addAll(videoItems);
                    Collections.shuffle(videoItems2);
                    videoItems3.addAll(videoItems);
                    Collections.shuffle(videoItems3);
                    adapter = new VideoAdapter(GalleryActivity.this, videoItems, new OnRecyclerItemClickListener() {
                        @Override
                        public void onClick(int position) {
                            Toast.makeText(GalleryActivity.this, "Click", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(GalleryActivity.this, VideoPlayerActivity.class);
                            intent.putExtra("videoUrl", Constants.URL+"/streamVideo/"+videoItems.get(position).getId());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Sometimes needed in certain contexts
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);

                    adapter2 = new VideoAdapter(GalleryActivity.this, videoItems2, new OnRecyclerItemClickListener() {
                        @Override
                        public void onClick(int position) {
                            Toast.makeText(GalleryActivity.this, "Click", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(GalleryActivity.this, VideoPlayerActivity.class);
                            intent.putExtra("videoUrl", Constants.URL+"/streamVideo/"+videoItems2.get(position).getId());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Sometimes needed in certain contexts
                            startActivity(intent);
                        }
                    });
                    recyclerView2.setAdapter(adapter2);

                    adapter3 = new VideoAdapter(GalleryActivity.this, videoItems3, new OnRecyclerItemClickListener() {
                        @Override
                        public void onClick(int position) {
                            Toast.makeText(GalleryActivity.this, "Click", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(GalleryActivity.this, VideoPlayerActivity.class);
                            intent.putExtra("videoUrl", Constants.URL+"/streamVideo/"+videoItems3.get(position).getId());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Sometimes needed in certain contexts
                            startActivity(intent);
                        }
                    });
                    recyclerView3.setAdapter(adapter3);
                }

                @Override
                public void onFailure(Call<List<VideoItem>> call, Throwable t) {
                    Toast.makeText(GalleryActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}
