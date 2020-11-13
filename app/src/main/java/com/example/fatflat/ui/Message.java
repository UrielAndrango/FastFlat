package com.example.fatflat.ui;

public class Message {

    //ATRIBUTS
    private String username = "username";
    private String message = "message";
    private String time = "time";

    //CONSTRUCTORA
    public Message(String username, String contingut_message, String time) {
        this.username = username;
        this.message = contingut_message;
        this.time = time;
    }


    //GETS & SETS
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
