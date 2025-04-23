package com.moutamid.sqlapp.activities.Calender.calenderapp.database;

public class Event {
    public long id;
    public String title;
    public String date;
    public String time;
    public String description;
    public String exact_time;
    public boolean checked;

    public  int complete;


    public float left;
    public float width;
    public float top;
    public float bottom;

    public Event() {
        // Default constructor
    }

    public Event(String title, String date, String time, String description, boolean checked) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.description = description;
        this.checked = checked;
    }

    // Getters and setters for all fields

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return checked;
    }

    public String getExact_time() {
        return exact_time;
    }

    public void setExact_time(String exact_time) {
        this.exact_time = exact_time;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }
}
