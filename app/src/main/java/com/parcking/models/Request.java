package com.parcking.models;

import java.util.List;

public class Request {

    public String nameEmployee;
    public boolean status;
    public String emailEmployee;
    public List<parking> parking;
    public String days;
    public String time;
    public String price;
    public String information;
    public String nameOrg;
    public String idOrg;

    //  public String

    public Request(String nameEmployee, boolean status) {
        this.nameEmployee = nameEmployee;
        this.status = status;
    }
    public Request() {

    }

    public boolean isStatus(boolean b) {
        return status;
    }

    public String getNameOrg() {
        return nameOrg;
    }

    public void setNameOrg(String nameOrg) {
        this.nameOrg = nameOrg;
    }

    public String getIdOrg() {
        return idOrg;
    }

    public void setIdOrg(String idOrg) {
        this.idOrg = idOrg;
    }

    public String getEmailEmployee() {
        return emailEmployee;
    }

    public void setEmailEmployee(String emailEmployee) {
        this.emailEmployee = emailEmployee;
    }

    public List<com.parcking.models.parking> getParking() {
        return parking;
    }

    public void setParking(List<parking> parking) {
        this.parking = parking;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getNameEmployee() {
        return nameEmployee;
    }


    public void setNameEmployee(String nameEmployee) {
        this.nameEmployee = nameEmployee;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
