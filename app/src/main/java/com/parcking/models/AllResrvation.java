package com.parcking.models;

public class AllResrvation {

    public String id_park;
    public String from;
    public String to;

    public AllResrvation(String id_park, String from, String to) {
        this.id_park = id_park;
        this.from = from;
        this.to = to;
    }

    public String getId_park() {
        return id_park;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
