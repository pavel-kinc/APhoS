package cz.muni.aphos.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The Hibernate entity representing a user.
 */
@Entity
@Table(name = "users", schema = "public")
public class User {

    /**
     * the Google internal id of the user used also as an id here
     */
    @Id
    private String googleSub;
    private String username;

    /**
     * the description of the user's profile
     */
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
