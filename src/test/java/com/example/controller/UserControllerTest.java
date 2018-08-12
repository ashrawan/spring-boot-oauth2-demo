package com.example.controller;

import com.example.demo1.controller.UserController;
import com.example.demo1.exception.DataNotFoundException;
import com.example.demo1.model.User;
import com.example.demo1.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerTest.class);

    private MockMvc mvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    User user;
    User newUser;

    private static final long ID = 1;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
        user = new User(ID, "Test Name", "test", "test", User.USER_ROLE.ROLE_ADMIN, 1);
    }

    @Test
    public void allUsers_thenReturnStatusOK() throws Exception {

        given(userService.getAllUsers()).willReturn(Arrays.asList(user));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/users")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void allAddress_whenThereIsNoAddressInTable_thenReturnStatus_NOT_FOUND() throws Exception{

        given(userService.getAllUsers()).willReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/users").contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void getUserBydId_thenReturnStatusOK() throws Exception {

        given(userService.getUserById(ID)).willReturn(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/users/"+ID)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void getUserById_whenThereisNoSuchUserInTable_thenReturnStatus_NOT_FOUND() throws Exception {

        given(userService.getUserById(1L)).willReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/users/"+ID)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void addUser_whenAdded_thenReturnUser()  throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(user);
        given(userService.createUser(user)).willReturn(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .accept(MediaType.APPLICATION_JSON).content(jsonString).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        String outputInJson = response.getContentAsString();
        User returnedUser = mapper.readValue(outputInJson,User.class);

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    @Test
    public void updateUser_thenReturnStatusOK() throws Exception {

        /**
         * provides functionality for reading and writing JSON,
         * either to and from basic POJOs, or JSON Tree Model
         * provides functionality for performing conversions
         */
        ObjectMapper mapper = new ObjectMapper();

        // used to serialize any Java value as JSON output
        String jsonString = mapper.writeValueAsString(user);

        // when - want the mock to return particular value when particular method is called.
        given(userService.updateUser(user, ID)).willReturn(true);

        // used to prepare the request.
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rest/users/"+ID)
                .accept(MediaType.APPLICATION_JSON).content(jsonString).contentType(MediaType.APPLICATION_JSON);

        // perform a request and return the result of the executed request for direct access to the results.
        MvcResult result = mvc.perform(requestBuilder).andReturn();

        // provide HTTP-specific functionality,  HttpServletResponse object
        MockHttpServletResponse response = result.getResponse();

        // Returns HTTP content as a string, taking care of any encodings of the content if necessary.
        String outputInJson = response.getContentAsString();

        Boolean b = Boolean.parseBoolean(outputInJson);

        Assert.assertTrue(b);
    }

    @Test
    public void updateUser_whenThereIsNoSuchRecordInUserTable_thenReturnStatus_CONFLICT()throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(user);
        given(userService.updateUser(user, ID)).willReturn(false);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rest/users/"+ID)
                .accept(MediaType.APPLICATION_JSON).content(jsonString).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String outputInJson = response.getContentAsString();
        Boolean b = Boolean.parseBoolean(outputInJson);
        Assert.assertFalse(b);
        Assert.assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    public void deleteUser_thenReturnStatusOK() throws Exception {
        given(userService.deleteUser(ID)).willReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/rest/users/"+ID)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String outputInJson = response.getContentAsString();
        Boolean b = Boolean.parseBoolean(outputInJson);
        Assert.assertTrue(b);
    }

    @Test
    public void deleteUser_whenThereIsNoSuchRecordInUserTable_thenReturnStatus_CONFLICT() throws Exception {
        given(userService.deleteUser(ID)).willReturn(false);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/rest/users/1")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String outputInJson = response.getContentAsString();
        Boolean b = Boolean.parseBoolean(outputInJson);
        Assert.assertFalse(b);
        Assert.assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

}
