package com.moutamid.sqlapp.model;

public class Review {
    private float rating;
    private String review;
    private long timestamp;

    public Review() {} // Required for Firebase

    public float getRating() { return rating; }
    public String getReview() { return review; }
    public long getTimestamp() { return timestamp; }
}
