package com.example.demo1.repository;

import com.example.demo1.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query(value = "SELECT * FROM address a where a.user = ?1", nativeQuery = true)
    public List<Address> findByUserId(Long userId);

}
