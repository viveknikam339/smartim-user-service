package com.smartim.userservice.repository;

import com.smartim.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity.
 * Extends JpaRepository to provide basic CRUD operations and
 * additional query methods for finding users by email, username, mobile number, etc.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by email.
     *
     * @param email the email to search
     * @return an Optional containing the user if found, else empty
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by username.
     *
     * @param userName the username to search
     * @return an Optional containing the user if found, else empty
     */
    Optional<User> findByUserName(String userName);

    /**
     * Finds a user by mobile number.
     *
     * @param mobileNumber the mobile number to search
     * @return an Optional containing the user if found, else empty
     */
    Optional<User> findByMobileNumber(String mobileNumber);

    /**
     * Finds users by role.
     *
     * @param role the role to filter by
     * @return an Optional containing a list of users with the given role
     */
    Optional<List<User>> findByRole(String role);

    /**
     * Finds users by either email or mobile number.
     *
     * @param email the email to search
     * @param mobileNumber the mobile number to search
     * @return a list of users matching either the email or mobile number
     */
    List<User> findByEmailOrMobileNumber(String email, String mobileNumber);
}
