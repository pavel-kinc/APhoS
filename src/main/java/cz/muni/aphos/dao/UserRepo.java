package cz.muni.aphos.dao;

import cz.muni.aphos.dto.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


/**
 * The crud repository for the User entity.
 */
public interface UserRepo extends CrudRepository<User, String> {

    /**
     * Find user by id.
     *
     * @param userID the user id
     * @return the user
     */
    @Query(nativeQuery = true, value =
            "SELECT * FROM users where google_sub = ?1")
    User findByUserID(String userID);

    /**
     * Check if the user with given username exists.
     *
     * @param username the username
     * @return true if the user exists
     */
    @Query("select (count(u) > 0) from User u where u.username = ?1")
    boolean existsByUsernameEquals(String username);
}
