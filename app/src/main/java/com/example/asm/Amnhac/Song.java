package com.example.asm.Amnhac;

public class Song {
    private String url;

    public Song() {
        // Firebase requires a public no-argument constructor
    }

    public Song(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
