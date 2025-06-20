package com.smartim.userservice.repository;

import com.smartim.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserName(String userName);

    Optional<User> findByMobileNumber(String mobileNumber);

    Optional<List<User>> findByRole(String role);

    List<User> findByEmailOrMobileNumber(String email, String mobileNumber);
}
