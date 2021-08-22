package com.amangoyal.finale;

public class UserHelperClass {

    String name,username,phone,password,from,to,date,time;

    public UserHelperClass() {
    }

    public UserHelperClass(String name, String username, String phone, String password) {
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.password = password;
    }
    public  UserHelperClass(String name , String username , String from , String to  , String date , String time){
        this.name = name;
        this.username = username;
        this.date=date;
        this.time=time;
        this.from=from;
        this.to=to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
