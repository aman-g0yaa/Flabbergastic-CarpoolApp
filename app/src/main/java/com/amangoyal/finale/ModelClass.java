package com.amangoyal.finale;

public class ModelClass {

    private String from;
    private String to;
    private String seat;
    private String name;
    private String date;
    private String time;
    private String Username;



    public ModelClass() {
    }


    public ModelClass(String from, String to, String seat, String name, String date, String time, String username) {
        this.from = from;
        this.to = to;
        this.seat = seat;
        this.name = name;
        this.date = date;
        this.time = time;
       this.Username = username;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }


}
