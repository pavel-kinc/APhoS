package com.example.astroapp.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="users", schema = "public")
public class User {

    @Id
    private String googleSub;
    private String email;
    private String firstName;
    private String lastName;
    @OneToMany(mappedBy = "user")
    private List<Flux> fluxes;

    public User(String googleSub, String email, String firstName, String lastName) {
        this.googleSub = googleSub;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String googleSub, String firstName, String lastName) {
        this.googleSub = googleSub;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User() {
    }

    public String getGoogleSub() {
        return googleSub;
    }

    public void setGoogleSub(String googleSub) {
        this.googleSub = googleSub;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
