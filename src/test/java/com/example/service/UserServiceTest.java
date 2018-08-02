package com.example.service;

import com.example.demo1.exception.DataNotFoundException;
import com.example.demo1.model.User;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.serviceImpl.UserServiceImpl;
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
//@RunWith(SpringRunner.class)
public class UserServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;

    @Before
    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
        user = new User(1L, "Test Name", "test", "test", User.USER_ROLE.ROLE_USER, 1);

    }

    @Test
    public void getAll_thenResultUserList() {

        LOGGER.info("Test Get All Users");
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);
        Assert.assertEquals(userServiceImpl.getAllUsers(),userList);
    }

    @Test
    public void getUserById_thenResultUser() {

        LOGGER.info("Test Get User By Id");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Assert.assertEquals(user, userServiceImpl.getUserById((long) 1));
    }

    @Test
    public void addUser_thenResultUser(){

        LOGGER.info("Test Create User");
        when(userRepository.save(user)).thenReturn(user);
        Assert.assertEquals(userServiceImpl.createUser(user), user);
    }

    @Test
    public void updateUser_thenResultTrue(){

        LOGGER.info("Test Update User");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        Assert.assertTrue(userServiceImpl.updateUser(user,user.getId()));
    }

    @Test
    public void deleteUser_thenResultTrue(){

        LOGGER.info("Test Delete User");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);
        Assert.assertTrue(userServiceImpl.deleteUser(user.getId()));
    }

    // --------- Failure Testing --------------

    @Test(expected = DataNotFoundException.class)
    public void getAll_whenUserListIsNullOrSizeIsZero_thenReturnDataNotFoundExcetion(){

        when(userRepository.findAll()).thenReturn(null);
        userServiceImpl.getAllUsers();
    }

    @Test(expected = DataNotFoundException.class)
    public void getByUserId_whenUserIdIsNull_thenReturnDataNotFoundException(){

        userServiceImpl.getUserById(null);
    }

    @Test(expected = DataNotFoundException.class)
    public void addUser_whenUserIsNull_thenReturnDataNotFoundException() {

        userServiceImpl.createUser(null);
    }

    @Test(expected = DataNotFoundException.class)
    public void updateUser_whenGetUserByIdIsNull_thenReturnDataNotFoundException(){

        userServiceImpl.updateUser(user,null);
    }

    @Test(expected = DataNotFoundException.class)
    public void deleteUser_whenUserIsNull_thenReturnDataNotFoundException(){

//        expectedException.expect(DataNotFoundException.class);
//        expectedException.expectMessage("cannot delete user");
        userServiceImpl.deleteUser(null);
    }


}
