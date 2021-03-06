package com.parcking.models;

/**
 * Created by locall on 2/14/2018.
 */

public class Employee {
    private String email;
    private String password;
    private String typeUser;
    private String city;
    private String name;
    private String phone;
    private String id_org;
    private String name_org;

    public Employee(){}


    public Employee(String email, String password, String typeUser, String city, String name, String phone, String id_org, String name_org) {
        this.email = email;
        this.password = password;
        this.typeUser = typeUser;
        this.city = city;
        this.name = name;
        this.phone = phone;
        this.id_org = id_org;
        this.name_org = name_org;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getId_org() {
        return id_org;
    }

    public void setId_org(String id_org) {
        this.id_org = id_org;
    }

    public String getName_org() {
        return name_org;
    }

    public void setName_org(String name_org) {
        this.name_org = name_org;
    }

    public String getPassword() {
        return password;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public String getEmail() {
        return email;
    }



    public void setPassword(String password) {
        this.password = password;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public void setEmail(String username) {
        this.email = username;
    }
}
