package com.example.astroapp.dao;

import com.example.astroapp.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface UserRepo extends CrudRepository<User, String> {

    @Query(nativeQuery = true, value =
            "SELECT * FROM users where google_sub = ?1")
    User findByUserID(String userID);

    @Query("select (count(u) > 0) from User u where u.username = ?1")
    boolean existsByUsernameEquals(String username);
}
