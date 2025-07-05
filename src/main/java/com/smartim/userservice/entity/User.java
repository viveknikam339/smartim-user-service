package com.smartim.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


/**
 * Represents a user entity in the system.
 * Stores user authentication and profile-related information.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    private String fullName;

    @Column(unique = true)
    @NonNull
    private String mobileNumber;

    private Boolean userStatus = true;

    @NonNull
    private String role;

    @Column(unique = true)
    @NonNull
    private String userName;
}
