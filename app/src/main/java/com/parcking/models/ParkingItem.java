package com.parcking.models;

public class ParkingItem {

private int id;
    private String date;
    private boolean status;
    private String email;


    public ParkingItem(int id, boolean status, String email) {

        this.status = status;
        this.email = email;
        this.id = id ;
    }

    public ParkingItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
