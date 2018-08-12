package com.example.demo1.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="address")
public class Address {

    public enum AddressType { PERMANENT, TEMPORARY}

    @Id
    @Column(name = "address_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotNull(message = "district must be specified")
    @Size(min = 2, max = 50, message = "district name must be between 2 and 50.")
    private String district;

    @Column
    @NotNull(message = "city must be specified")
    @Size(min = 2, max = 50, message = "city must be between 2 and 50.")
    private String city;

    @Enumerated(EnumType.STRING)
    @Column
    private AddressType addressType = AddressType.PERMANENT;

    @Column
    private int status;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    public Address() {
    }

    public Address(long id, String district, String city, int status, User user) {
        this.id = id;
        this.district = district;
        this.city = city;
        this.status = status;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id == address.id &&
                status == address.status &&
                Objects.equals(district, address.district) &&
                Objects.equals(city, address.city) &&
                Objects.equals(user, address.user);
    }

    @Override
    public int hashCode() {

        return Objects.hash(district, city, status);
    }
}
