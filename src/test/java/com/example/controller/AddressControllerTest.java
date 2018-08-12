package com.example.controller;

import com.example.demo1.controller.AddressController;
import com.example.demo1.model.Address;
import com.example.demo1.model.User;
import com.example.demo1.service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AddressControllerTest {

    private MockMvc mvc;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    private Address address;

    public static final long ID = 1;
    @Before
    public void setup(){
        mvc = MockMvcBuilders.standaloneSetup(addressController).build();
        address = new Address(ID, "Ktm", "cc", 1, new User());
    }

    @Test
    public void allAddress_thenReturnStatusOk() throws Exception{
        given(addressService.getAllAddresss()).willReturn(Arrays.asList(address));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/address").contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void allAddress_whenThereIsNoAddressInTable_thenReturnStatus_NOT_FOUND() throws Exception{

        given(addressService.getAllAddresss()).willReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/address").contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void getAddressBydId_thenReturnStatusOK() throws Exception {

        given(addressService.getAddressById(ID)).willReturn(address);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/address/"+ID)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void getAddressById_whenThereisNoSuchAddressInTable_thenReturnStatus_NOT_FOUND() throws Exception {

        given(addressService.getAddressById(ID)).willReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/address/"+ID)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }


    @Test
    public void createAddress_thenReturnStatusOk() throws Exception{

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(address);
        given(addressService.createAddress(address)).willReturn(address);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rest/address")
                .accept(MediaType.APPLICATION_JSON).content(jsonString).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String outputInJson = response.getContentAsString();
        Address returnedAddress = mapper.readValue(outputInJson,Address.class);
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }


    @Test
    public void updateAddress_thenReturnStatusOK() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(address);
        given(addressService.updateAddress(address, ID)).willReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rest/address/"+ID)
                .accept(MediaType.APPLICATION_JSON).content(jsonString).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String outputInJson = response.getContentAsString();
        Boolean b = Boolean.parseBoolean(outputInJson);
        Assert.assertTrue(b);
    }

    @Test
    public void updateAddress_whenThereIsNoSuchRecordInAddressTable_thenReturnStatus_CONFLICT()throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(address);
        given(addressService.updateAddress(address, ID)).willReturn(false);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rest/address/"+ID)
                .accept(MediaType.APPLICATION_JSON).content(jsonString).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String outputInJson = response.getContentAsString();
        Boolean b = Boolean.parseBoolean(outputInJson);
        Assert.assertFalse(b);
        Assert.assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    public void deleteAddress_thenReturnStatusOK() throws Exception {

        given(addressService.deleteAddress(ID)).willReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/rest/address/"+ID)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String outputInJson = response.getContentAsString();
        Boolean b = Boolean.parseBoolean(outputInJson);
        Assert.assertTrue(b);
    }

    @Test
    public void deleteAddress_whenThereIsNoSuchRecordInAddressTable_thenReturnStatus_CONFLICT() throws Exception {

        given(addressService.deleteAddress(ID)).willReturn(false);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/rest/address/"+ID)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String outputInJson = response.getContentAsString();
        Boolean b = Boolean.parseBoolean(outputInJson);
        Assert.assertFalse(b);
        Assert.assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

}
