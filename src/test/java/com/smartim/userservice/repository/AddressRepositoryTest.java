package com.smartim.userservice.repository;

import com.smartim.userservice.entity.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class AddressRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void findByUserName_ShouldReturnListOfAddresses() {
        Address address1 = new Address();
        address1.setUserName("testuser");
        address1.setCity("City1");
        address1.setCreatedBy("testuser");
        address1.setCreatedOn(LocalDateTime.now());
        entityManager.persist(address1);

        Address address2 = new Address();
        address2.setUserName("testuser");
        address2.setCity("City2");
        address2.setCreatedBy("testuser");
        address2.setCreatedOn(LocalDateTime.now());
        entityManager.persist(address2);

        Address address3 = new Address();
        address3.setUserName("anotheruser");
        address3.setCity("City3");
        address3.setCreatedBy("testuser");
        address3.setCreatedOn(LocalDateTime.now());
        entityManager.persist(address3);

        entityManager.flush();

        List<Address> foundAddresses = addressRepository.findByUserName("testuser");

        assertNotNull(foundAddresses);
        assertEquals(2, foundAddresses.size());
        assertEquals("City1", foundAddresses.getFirst().getCity());
    }

    @Test
    void findByUserName_ShouldReturnEmptyList_WhenNoAddressesFound() {
        List<Address> foundAddresses = addressRepository.findByUserName("nonexistentuser");

        assertNotNull(foundAddresses);
        assertEquals(0, foundAddresses.size());
    }
}