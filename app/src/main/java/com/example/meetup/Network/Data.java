package com.example.meetup.Network;

public class Data {
    private  String type,meetingType,name,email,inviterToken;

    public Data(String type, String meetingType, String name, String email, String inviterToken) {
        this.type = type;
        this.meetingType = meetingType;
        this.name = name;
        this.email = email;
        this.inviterToken = inviterToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInviterToken() {
        return inviterToken;
    }

    public void setInviterToken(String inviterToken) {
        this.inviterToken = inviterToken;
    }
}
