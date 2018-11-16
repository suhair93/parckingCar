package com.parcking.models;

import java.sql.Time;
import java.util.List;

public class parking {

    public String name;
    public String img;
    public String emailEmployee;
    public List<ParkingItem> time;
    private String date;
    private String idOrg;

    public parking( ) {

    }

    public parking(String name, String img, String emailEmployee, List<ParkingItem> time,String date,String idOrg) {
        this.name = name;
        this.img = img;
        this.emailEmployee = emailEmployee;
        this.time = time;
        this.date= date;
        this.idOrg = idOrg;
    }

    public String getDate() {
        return date;
    }

    public String getIdOrg() {
        return idOrg;
    }

    public void setIdOrg(String idOrg) {
        this.idOrg = idOrg;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEmailEmployee() {
        return emailEmployee;
    }

    public void setEmailEmployee(String emailEmployee) {
        this.emailEmployee = emailEmployee;
    }

    public List<ParkingItem> getTime() {
        return time;
    }

    public void setTime(List<ParkingItem> time) {
        this.time = time;
    }
}
