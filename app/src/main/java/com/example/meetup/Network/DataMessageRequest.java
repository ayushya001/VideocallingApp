package com.example.meetup.Network;

public class DataMessageRequest {
    private String to;
    private Data data;
    private String priority;

    public DataMessageRequest(String to, Data data, String priority) {
        this.to = to;
        this.data = data;
        this.priority = priority;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
