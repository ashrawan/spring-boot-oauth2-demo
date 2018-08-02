package com.example.demo1.serviceImpl;

import com.example.demo1.exception.DataNotFoundException;
import com.example.demo1.model.Address;
import com.example.demo1.repository.AddressRepository;
import com.example.demo1.service.AddressService;
import com.example.demo1.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;
    private final UserService userService;

    public AddressServiceImpl(final AddressRepository addressRepository, final UserService userService){

        this.addressRepository = addressRepository;
        this.userService = userService;
    }

    @Override
    public List<Address> getAllAddresss() {
        List<Address> addressList = addressRepository.findAll();
        if(addressList == null){
            throw new DataNotFoundException("cannot find address");
        }
        return addressList;
    }

    @Override
    public Address getAddressById(Long id) {
        if(id==null){
            throw new DataNotFoundException("cannot find address");
        }
        Optional<Address> address = addressRepository.findById(id);
        if(!address.isPresent()){
            throw new DataNotFoundException("cannot find address");
        }
        return address.get();
    }

    @Override
    public Address createAddress(Address address) {

//        // ---------------- checking if provided user id is in database -------------
//        if(address.getUser() == null || address.getUser().getId() == null){
//            throw new DataNotFoundException("user id not provided");
//        }
//        User user = userService.getUserById(address.getUser().getId());
//        if(user == null){
//            throw new DataNotFoundException("cannot find user");
//        }
//        // -----------------------

        if(address==null){
            throw new DataNotFoundException("cannot create address");
        }
        return addressRepository.save(address);
    }

    @Override
    public boolean updateAddress(Address address, Long id) {
        if(address==null || id == null){
            throw new DataNotFoundException("cannot find address");
        }
        Optional<Address> address1 = addressRepository.findById(id);
        if(!address1.isPresent()){
            throw new DataNotFoundException("cannot find address");
        }


        address1.get().setCity(address.getCity());
        address1.get().setDistrict(address.getDistrict());
        address1.get().setStatus(address.getStatus());
        address1.get().setAddressType(address.getAddressType());
//        address1.get().setUser(address.getUser());
        Address updateUser = addressRepository.save(address1.get());
        return true;
    }

    @Override
    public boolean deleteAddress(Long id) {
        if(id==null){
            throw new DataNotFoundException("cannot delete address");
        }
        Optional<Address> address = addressRepository.findById(id);
         addressRepository.delete(address.get());
         return true;
    }

    @Override
    public List<Address> getAllByUserId(Long id) {
        List<Address> addressList = addressRepository.findByUserId(id);
        if(addressList == null){
            throw new DataNotFoundException("cannot find address");
        }
        return addressList;
    }
}
