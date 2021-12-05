package com.example.astroapp.services;

import com.example.astroapp.dao.UserRepo;
import com.example.astroapp.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public void processOAuthPostLogin(String sub, String name, String email) {
        if (!userRepo.existsById(sub)) {
            String firstName = name.split(" ")[0];
            String lastName = name.split(" ")[1];
            User newUser = new User(sub, email, firstName, lastName);
            userRepo.save(newUser);
        }
    }

}
