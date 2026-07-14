package com.smartim.userservice.repository;

import com.smartim.userservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserName("testuser");
        user.setEmail("test@example.com");
        user.setMobileNumber("1234567890");
        user.setRole("USER");
        user.setUserStatus(true);
        user.setCreatedBy("testuser");
        user.setCreatedOn(LocalDateTime.now());
        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    void findByEmail_ShouldReturnUser() {
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUserName());
    }

    @Test
    void findByUserName_ShouldReturnUser() {
        Optional<User> foundUser = userRepository.findByUserName("testuser");
        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void findByMobileNumber_ShouldReturnUser() {
        Optional<User> foundUser = userRepository.findByMobileNumber("1234567890");
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUserName());
    }

    @Test
    void findByRole_ShouldReturnListOfUsers() {
        Optional<List<User>> foundUsers = userRepository.findByRole("USER");
        assertTrue(foundUsers.isPresent());
        assertEquals(1, foundUsers.get().size());
    }

    @Test
    void findByUserNameAndUserStatus_ShouldReturnUser() {
        Optional<User> foundUser = userRepository.findByUserNameAndUserStatus("testuser", true);
        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void findByEmailOrMobileNumber_ShouldReturnUser() {
        List<User> foundUsers = userRepository.findByEmailOrMobileNumber("test@example.com", "0987654321");
        assertEquals(1, foundUsers.size());

        foundUsers = userRepository.findByEmailOrMobileNumber("other@example.com", "1234567890");
        assertEquals(1, foundUsers.size());
    }

    @Test
    void deleteByUserName_ShouldDeleteUser() {
        userRepository.deleteByUserName("testuser");
        Optional<User> deletedUser = userRepository.findByUserName("testuser");
        assertFalse(deletedUser.isPresent());
    }
}