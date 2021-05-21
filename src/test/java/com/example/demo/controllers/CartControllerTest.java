package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    User user = new User();
    Item item = new Item();
    Cart cart = new Cart();

    @Before
    public void init(){
        cartController = new CartController();



        TestUtils.injectObject(cartController, "userRepository", userRepo);
        TestUtils.injectObject(cartController, "itemRepository", itemRepo);
        TestUtils.injectObject(cartController, "cartRepository", cartRepo);

        user.setId(0L);
        user.setUsername("nami");
        user.setPassword("pass123");
        user.setCart(cart);
        when(userRepo.findByUsername("nami")).thenReturn(user);

        item.setId(0L);
        item.setName("hat");
        item.setPrice(BigDecimal.valueOf(5.0));
        item.setDescription("tennis hat");
        when(itemRepo.findById(0L)).thenReturn(Optional.of(item));
    }

    @Test
    public void addToCart_Test() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername(user.getUsername());

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(BigDecimal.valueOf(5.0), response.getBody().getTotal());
        assertNotNull(response.getBody().getItems());
    }

    @Test
    public void removeFromCart_Test() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername(user.getUsername());
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(BigDecimal.valueOf(10.0), response.getBody().getTotal());
        assertEquals(200, response.getStatusCodeValue());

        ModifyCartRequest modifyCartRequest2 = new ModifyCartRequest();
        modifyCartRequest2.setItemId(0L);
        modifyCartRequest2.setQuantity(1);
        modifyCartRequest2.setUsername(user.getUsername());
        response = cartController.removeFromcart(modifyCartRequest2);
        assertEquals(BigDecimal.valueOf(5.0), response.getBody().getTotal());
    }
}
