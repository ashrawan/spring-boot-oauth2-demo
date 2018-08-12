package com.example.demo1.repository;


import com.example.demo1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    @Override
    Optional<User> findById(Long aLong);

    User findOneByUsername(String username);

}
