package com.example.demo1.service;

import com.example.demo1.model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers();

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public User getUserById(Long id);

    public User createUser(User user);

//    @PreAuthorize("#user.username == principal.username")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public boolean updateUser(User user, Long id);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean deleteUser(Long id);
}
