package com.example.hospitalmanagementsystem;

public class PatientList {
    private String uid;
    private String pname;
    private String description;
    private String image;
    private String date;
    private String time;
    private String medicalcondition;
    private String pid;

    public PatientList(String uid, String pname, String description, String image, String date, String time, String medicalcondition, String pid) {
        this.uid = uid;
        this.pname = pname;
        this.description = description;
        this.image = image;
        this.date = date;
        this.time = time;
        this.medicalcondition = medicalcondition;
        this.pid = pid;
    }

    public PatientList() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getMedicalcondition() {
        return medicalcondition;
    }

    public void setMedicalcondition(String medicalcondition) {
        this.medicalcondition = medicalcondition;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
