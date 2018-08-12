package com.example.demo1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="user")
public class User {

    public enum USER_ROLE {ROLE_ADMIN, ROLE_USER}

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull(message="fullName must be specified.")
    @Size(min = 2, max = 50, message = "fullName must be between 2 and 50.")
    private String fullName;


    @Column(unique = true)
    @NotNull(message="username must be specified.")
    @Size(min = 2, max = 50, message = "username must be between 2 and 50.")
    private String username;

    @Column
    @NotNull(message="password must be specified.")
    @Size(min = 2, max = 50, message = "password must be between 2 and 50.")
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private USER_ROLE role;

    @Column
    private  int status;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Address> address = new ArrayList<>();

    public User() {

    }

    public User(Long id, String fullName, String username, String password, USER_ROLE role, int status) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.status = status;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public USER_ROLE getRole() {
        return role;
    }

    public void setRole(USER_ROLE role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return status == user.status &&
                Objects.equals(id, user.id) &&
                Objects.equals(fullName, user.fullName) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(role, user.role) &&
                Objects.equals(address, user.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fullName, username, password, role, status, address);
    }
}
