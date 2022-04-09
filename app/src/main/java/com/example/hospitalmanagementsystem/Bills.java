package com.example.hospitalmanagementsystem;

public class Bills {
    private String desc;
    private String amount;
    private String bid;
    private String date;
    private String time;

    public Bills() {
    }

    public Bills(String desc, String amount, String bid, String date, String time) {
        this.desc = desc;
        this.amount = amount;
        this.bid = bid;
        this.date = date;
        this.time = time;
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

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
