package cz.muni.aphos.services;

import cz.muni.aphos.dao.UserRepo;
import cz.muni.aphos.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * The service for working with the users.
 */
@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    /**
     * Save user to the database if it is a new user.
     *
     * @param sub user google id
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

    /**
     * Get the currently signed-in user who made the request.
     *
     * @return the current user
     */
    public User getCurrentUser() {
        OAuth2User principal = (OAuth2User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        String userId = principal.getAttribute("sub");
        return userRepo.findByUserID(userId);
    }
}
