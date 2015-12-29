package com.jp.smartgpsapp.Domain;

/**
 * Created by Zhu on 2015-12-26.
 */
public class User {
    private  String callsign;
    private  String email;
    private  String username;
    private  String password;
    private  String created_at;
    private  String location;

    public User(String callsign, String email, String username,
                String password, String location, String created_at){
        this.callsign = callsign;
        this.created_at = created_at;
        this.email = email;
        this.location = location;
        this.password = password;
        this.username = username;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
