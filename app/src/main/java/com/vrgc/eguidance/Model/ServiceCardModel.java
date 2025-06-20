package com.vrgc.eguidance.Model;

public class ServiceCardModel {
    public int imageResId;
    public String title;
    public String description;

    public ServiceCardModel(int imageResId, String title, String description) {
        this.imageResId = imageResId;
        this.title = title;
        this.description = description;
    }
}