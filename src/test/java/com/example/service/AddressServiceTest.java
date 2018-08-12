package com.example.service;

import com.example.demo1.exception.DataNotFoundException;
import com.example.demo1.model.Address;
import com.example.demo1.model.User;
import com.example.demo1.repository.AddressRepository;
import com.example.demo1.serviceImpl.AddressServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddressServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressServiceTest.class);

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressServiceImpl;

    private Address address;

    @Before
    public void setUp() throws Exception {
        address = new Address(1L, "ktm", "cc", 1, new User());
    }

    @Test
    public void getAll_thenResultAddressList() {

        LOGGER.info("Test Get All Addresss");
        List<Address> addressList = new ArrayList<>();
        when(addressRepository.findAll()).thenReturn(addressList);
        Assert.assertEquals(addressServiceImpl.getAllAddresss(),addressList);
    }

    @Test(expected = DataNotFoundException.class)
    public void getAll_whenAddressListIsNullOrSizeIsZero_thenReturnDataNotFoundExcetion(){

        when(addressRepository.findAll()).thenReturn(null);
        addressServiceImpl.getAllAddresss();
    }

    @Test
    public void getAddressById_thenResultAddress() {

        LOGGER.info("Test Get Address By Id");
        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
        Assert.assertEquals(address, addressServiceImpl.getAddressById(1L));
    }

    @Test(expected = DataNotFoundException.class)
    public void getByAddressId_whenAddressIdIsNull_thenReturnDataNotFoundException(){

        addressServiceImpl.getAddressById(null);
    }

    @Test
    public void addAddress_thenResultAddress(){

        LOGGER.info("Test Create Address");
        when(addressRepository.save(address)).thenReturn(address);
        Assert.assertEquals(addressServiceImpl.createAddress(address), address);
    }

    @Test(expected = DataNotFoundException.class)
    public void addAddress_whenAddressIsNull_thenReturnDataNotFoundException() {

        addressServiceImpl.createAddress(null);
    }

    @Test
    public void updateAddress_thenResultTrue(){

        LOGGER.info("Test Update Address");
        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
        when(addressRepository.save(address)).thenReturn(address);
        Assert.assertTrue(addressServiceImpl.updateAddress(address,address.getId()));
    }

    @Test(expected = DataNotFoundException.class)
    public void updateAddress_whenGetAddressByIdIsNull_thenReturnDataNotFoundException(){

        addressServiceImpl.updateAddress(address,null);
    }

    @Test
    public void deleteAddress_thenResultTrue(){

        LOGGER.info("Test Delete Address");
        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
        doNothing().when(addressRepository).delete(address);
        Assert.assertTrue(addressServiceImpl.deleteAddress(address.getId()));
    }

    @Test(expected = DataNotFoundException.class)
    public void deleteAddress_whenAddressIsNull_thenReturnDataNotFoundException(){

        addressServiceImpl.deleteAddress(null);
    }

}
