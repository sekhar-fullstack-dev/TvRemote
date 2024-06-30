package com.example.tvremote.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tvremote.Constants;
import com.example.tvremote.R;
import com.example.tvremote.VideoItem;
import com.example.tvremote.interfaces.OnRecyclerItemClickListener;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private final OnRecyclerItemClickListener listener;
    private List<VideoItem> videoItems;
    private Context context;

    public VideoAdapter(Context context, List<VideoItem> videoItems, OnRecyclerItemClickListener listener) {
        this.context = context;
        this.videoItems = videoItems;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VideoItem item = videoItems.get(position);
        holder.videoName.setText(item.getName());
        Glide.with(context).load(Constants.URL+"/getThumbnail/"+item.getId()).into(holder.thumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView videoName;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            videoName = itemView.findViewById(R.id.videoName);
        }
    }
}

