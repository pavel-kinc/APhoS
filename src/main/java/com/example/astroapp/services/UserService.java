package com.example.astroapp.services;

import com.example.astroapp.dao.UserRepo;
import com.example.astroapp.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;


    /**
     * Save user to the database if it is a new user.
     *
     * @param sub user id
     * @return true if user is new false otherwise
     */
    public boolean processOAuthPostLogin(String sub) {
        if (!userRepo.existsById(sub)) {
            User newUser = new User(sub);
            userRepo.save(newUser);
            return true;
        }
        return false;
    }

    public User getCurrentUser() {
        OAuth2User principal = (OAuth2User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        String userId = principal.getAttribute("sub");
        return userRepo.findByUserID(userId);
    }
}
