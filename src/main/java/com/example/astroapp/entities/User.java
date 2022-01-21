package com.example.astroapp.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

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
