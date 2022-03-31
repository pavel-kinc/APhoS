package com.example.astroapp.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    private String googleSub;
    private String username;
    private String description;

    public User(String googleSub) {
        this.googleSub = googleSub;
    }

    public User(String googleSub, String username) {
    }

    public User() {
    }

    public String getGoogleSub() {
        return googleSub;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
