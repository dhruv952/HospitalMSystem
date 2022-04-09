package com.example.hospitalmanagementsystem;

public class Treatment {
    private String name;
    private String date;
    private String time;
    private String frequency;
    private String tid;

    public Treatment() {
    }

    public Treatment(String name, String date, String time, String freq) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.frequency = frequency;
        this.tid  = tid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
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

    public String getFreq() {
        return frequency;
    }

    public void setFreq(String freq) {
        this.frequency = frequency;
    }
}
