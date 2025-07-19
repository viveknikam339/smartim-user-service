package com.smartim.userservice.repository;

import com.smartim.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity.
 * Extends JpaRepository to provide basic CRUD operations and
 * additional query methods for finding users by email, username, mobile number, etc.
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

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
     * Finds user by user-ame and user status.
     *
     * @param userName the user-name to search
     * @param userStatus the user status number to search
     * @return a user matching the user-name and userName
     */
    Optional<User> findByUserNameAndUserStatus(String userName, Boolean userStatus);

    /**
     * Finds users by either email or mobile number.
     *
     * @param email the email to search
     * @param mobileNumber the mobile number to search
     * @param userName the user-name to search
     * @return a list of users matching either the email, mobile number or userName
     */
    List<User> findByEmailOrMobileNumberOrUserName(String email, String mobileNumber, String userName);

    void deleteByUserName(String userName);
}
