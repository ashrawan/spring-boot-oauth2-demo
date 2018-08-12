package com.example.demo1.controller;

import com.example.demo1.model.Address;
import com.example.demo1.model.User;
import com.example.demo1.service.AddressService;
import com.example.demo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class AddressController {

    private final AddressService addressService;

    public AddressController(final AddressService addressService, final UserService userService){

        this.addressService = addressService;
    }

    @GetMapping("/address")
    public ResponseEntity<List<Address>> getAllAddress(){
        List<Address> addresseList = addressService.getAllAddresss();
        if(addresseList==null || addresseList.size()<=0){
            return new ResponseEntity<>(addresseList, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(addresseList, HttpStatus.OK);
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<Address> retrieveAddress(@PathVariable long id) {

        Address addressRe = addressService.getAddressById(id);
        if(addressRe == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Address>(addressRe, HttpStatus.OK);
    }

    @GetMapping("/address/user/{id}")
    public ResponseEntity<List<Address>> retrieveAddressByUser(@PathVariable long id) {

        List<Address> addressRe = addressService.getAllByUserId(id);
        if(addressRe == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Address>>(addressRe, HttpStatus.OK);
    }
    
    @PostMapping("/address")
    public ResponseEntity<Address> createAddress(@RequestBody @Valid Address address) {

        Address addressRe = addressService.createAddress(address);
        if(addressRe == null ){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<Address>(addressRe, HttpStatus.OK);

    }

    @PutMapping("/address/{id}")
    public ResponseEntity<Boolean> updateAddress(@RequestBody @Valid Address address, @PathVariable long id) {

        boolean b=addressService.updateAddress(address, id);
        if(b==false){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(b, HttpStatus.OK);

    }
    
    @DeleteMapping("/address/{id}")
    public ResponseEntity<Boolean> deleteAddress(@PathVariable long id) {

        boolean b=addressService.deleteAddress(id);
        if(b==false){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(b, HttpStatus.OK);
    }
}
