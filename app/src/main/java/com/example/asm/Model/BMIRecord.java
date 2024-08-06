package com.example.asm.Model;

public class BMIRecord {
    private float weight;
    private float height;
    private float bmi;
    private String bmiCategory;
    private String dietRecommendations;
    private String uid; // Thêm trường này

    // Constructor rỗng cần thiết cho Firebase
    public BMIRecord() {}

    // Constructor có UID
    public BMIRecord(float weight, float height, float bmi, String bmiCategory, String dietRecommendations, String uid) {
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.bmiCategory = bmiCategory;
        this.dietRecommendations = dietRecommendations;
        this.uid = uid;
    }

    // Getter và Setter cho UID
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    // Các getter và setter khác
    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getBmi() {
        return bmi;
    }

    public void setBmi(float bmi) {
        this.bmi = bmi;
    }

    public String getBmiCategory() {
        return bmiCategory;
    }

    public void setBmiCategory(String bmiCategory) {
        this.bmiCategory = bmiCategory;
    }

    public String getDietRecommendations() {
        return dietRecommendations;
    }

    public void setDietRecommendations(String dietRecommendations) {
        this.dietRecommendations = dietRecommendations;
    }
}
