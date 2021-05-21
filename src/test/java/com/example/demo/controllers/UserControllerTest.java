package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private CartRepository cartRepo = mock(CartRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);
    private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void init(){
        userController = new UserController();
        TestUtils.injectObject(userController, "cartRepository", cartRepo);
        TestUtils.injectObject(userController, "userRepository", userRepo);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", passwordEncoder);

        //setting the id of user in init to keep the tests independent
        User user = new User();
        user.setId(0);
        user.setUsername("nami");
        when(userRepo.findByUsername("nami")).thenReturn(user);
        when(userRepo.findById(0L)).thenReturn(java.util.Optional.of(user));


    }

    @Test
    public void createUser_Test(){

        when(passwordEncoder.encode("pass123")).thenReturn("hashedPassword");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("nami");
        createUserRequest.setPassword("pass123");
        createUserRequest.setConfirmPassword("pass123");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.getId());
        assertEquals("nami", responseBody.getUsername());
        assertEquals("hashedPassword", responseBody.getPassword());
    }

    @Test
    public void findById_Test() {
        ResponseEntity<User> responseEntity = userController.findById(0L);
        assertNotNull(responseEntity);
        assertEquals(0, responseEntity.getBody().getId());
        responseEntity = userController.findById(1L);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
    @Test
    public void findByUsername_Test() {
        ResponseEntity<User> responseEntity = userController.findByUserName("nami");
        assertNotNull(responseEntity);
        assertEquals("nami", responseEntity.getBody().getUsername());
        responseEntity = userController.findByUserName("ttt");
        assertEquals(404, responseEntity.getStatusCodeValue());
    }



}
