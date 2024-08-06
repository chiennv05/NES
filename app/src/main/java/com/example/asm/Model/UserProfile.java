package com.example.asm.Model;

public class UserProfile {
    private String name;
    private String imageUrl;

    public UserProfile() {
        // Constructor mặc định yêu cầu để DataSnapshot.getValue(UserProfile.class) hoạt động
    }

    public UserProfile(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
