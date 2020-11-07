package com.example.katundu.ui;

public class Post {
    public String id;
    private String title;
    private String user;
    private String description;
    private String time;
    public Post(){
    }

    public Post(String id,String title, String user, String description, String time) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.user = user;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title= title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {return time; }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {return id; }





}
