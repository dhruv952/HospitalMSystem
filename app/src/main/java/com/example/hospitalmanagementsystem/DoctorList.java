package com.example.hospitalmanagementsystem;

public class DoctorList {
    String dname;
    String did;
    String qualification;
    String specialization;
    String image;
    String date;
    String time;
    String ukey;

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
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

    public String getUkey() {
        return ukey;
    }

    public void setUkey(String ukey) {
        this.ukey = ukey;
    }

    public DoctorList(String dname, String did, String qualification, String specialization, String image, String date, String time, String ukey) {
        this.dname = dname;
        this.did = did;
        this.qualification = qualification;
        this.specialization = specialization;
        this.image = image;
        this.date = date;
        this.time = time;
        this.ukey = ukey;
    }

    public DoctorList() {
    }
}

