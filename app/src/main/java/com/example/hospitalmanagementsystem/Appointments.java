package com.example.hospitalmanagementsystem;

public class Appointments {
    private String doctor;
    private String name;
    private String phone;
    private String status;

    public Appointments() {
    }

    public Appointments(String doctor, String name, String phone, String status) {
        this.doctor = doctor;
        this.name = name;
        this.phone = phone;
        this.status = status;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
