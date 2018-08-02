package com.example.demo1.service;

import com.example.demo1.model.Address;

import java.util.List;

public interface AddressService {

    public List<Address> getAllAddresss();

    public Address getAddressById(Long id);

    public Address createAddress(Address address);

    public boolean updateAddress(Address address, Long id);

    public boolean deleteAddress(Long id);

    public List<Address> getAllByUserId(Long id);
}
