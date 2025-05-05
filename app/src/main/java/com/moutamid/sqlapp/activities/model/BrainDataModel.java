package com.moutamid.sqlapp.activities.model;

public class BrainDataModel {

    public String name, lat, lng, description;
    public int headerImg;

    public BrainDataModel(String name, int headerImg, String lat, String lng, String description) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.description = description;
        this.headerImg = headerImg;
    }

    public BrainDataModel() {
    }
}