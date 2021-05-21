package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private OrderRepository orderRepo = mock(OrderRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);

    @Before
    public void init(){
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "orderRepository", orderRepo);
        TestUtils.injectObject(orderController, "userRepository", userRepo);

        Item item = new Item();
        List<Item> items = new ArrayList<Item>();
        User user = new User();
        Cart cart = new Cart();
        UserOrder userOrder = new UserOrder();

        item.setId(0L);
        item.setPrice(BigDecimal.valueOf(5.0));
        item.setName("Hat");
        item.setDescription("Tennis Hat");
        items.add(item);
        user.setId(0L);
        user.setUsername("nami");
        user.setPassword("pass123");
        when(userRepo.findByUsername("nami")).thenReturn(user);
        cart.setId(0L);
        cart.setItems(items);
        cart.setUser(user);
        cart.setTotal(BigDecimal.valueOf(5.0));
        user.setCart(cart);
        userOrder.setTotal(BigDecimal.valueOf(5.0));
        userOrder.setUser(user);
        userOrder.setItems(items);
        userOrder.setId(0L);
        when(orderRepo.findByUser(user)).thenReturn(Collections.singletonList(userOrder));
    }

    @Test
    public void submit_Test(){
        ResponseEntity<UserOrder> response = orderController.submit("nami");
        assertEquals(1, response.getBody().getItems().size());
        assertEquals(BigDecimal.valueOf(5.0), response.getBody().getTotal());
        assertNotNull(response.getBody().getUser());

        ResponseEntity<UserOrder> response2 = orderController.submit("namita");
        assertEquals(404, response2.getStatusCodeValue());

    }
    @Test
    public void getOrdersForUser_Test(){
        ResponseEntity<List<UserOrder>> orders = orderController.getOrdersForUser("nami");
        assertEquals(200, orders.getStatusCodeValue());
        assertNotNull(orders);


    }

}
