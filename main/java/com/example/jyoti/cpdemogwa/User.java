package com.example.jyoti.cpdemogwa;

import java.io.Serializable;

/**
 * Created by jyoti on 7/26/2017.
 */
// bean or model or POJO
public class User implements Serializable {
    int id;
    String name;
    String email;
    String password;
    String gender;
    String city;

    public User(int id, String name, String email, String password, String gender, String city) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.city = city;
    }

    public User() {

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public void setCity(String city) {
        this.city = city;

    }
}
