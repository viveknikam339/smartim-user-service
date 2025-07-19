package com.smartim.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Represents an address entity in the system.
 * Stores users addresses.
 */
@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String userName;

    @NonNull
    private String receiverName;

    @NonNull
    private String mobileNumber;

    @NonNull
    private String label;

    @NonNull
    private String line1;

    private String line2;

    private String line3;

    @NonNull
    private String city;

    @NonNull
    private String state;

    @NonNull
    private String postalCode;

    @NonNull
    private String country;

    private String plusCode;
}
