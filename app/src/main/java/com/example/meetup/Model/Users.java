package com.example.meetup.Model;

public class Users {
    String name,fullname,email,userid,password,fcm_token;

    public Users(String name, String fullname, String email, String userid, String password, String fcm_token) {
        this.name = name;
        this.fullname = fullname;
        this.email = email;
        this.userid = userid;
        this.password = password;
        this.fcm_token = fcm_token;
    }

    public Users() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }
}
