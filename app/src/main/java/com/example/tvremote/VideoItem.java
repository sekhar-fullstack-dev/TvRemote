package com.example.tvremote;

public class VideoItem {
    private String name;
    private String thumbnailUrl;
    private long id; // Assuming each video has a unique ID

    // Constructor
    public VideoItem(long id, String name, String thumbnailUrl) {
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public long getId() {
        return id;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setId(long id) {
        this.id = id;
    }
}

