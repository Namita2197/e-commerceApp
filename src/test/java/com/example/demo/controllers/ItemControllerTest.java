package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void init(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);

        Item hat = new Item();
        hat.setId(0L);
        hat.setName("Hat");
        hat.setDescription("tennis hat");
        when(itemRepository.findById(0L)).thenReturn(Optional.of(hat));

        List<Item> itemList = new ArrayList<>();
        itemList.add(hat);
        when(itemRepository.findByName("Hat")).thenReturn(itemList);

        Item shoes = new Item();
        shoes.setName("shoes");
        shoes.setDescription("running shoe");
        List<Item> itemList2 = new ArrayList<>();
        itemList2.add(shoes);
        when(itemRepository.findAll()).thenReturn(itemList2);
    }

    @Test
    public void getItems_Test(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertEquals(1, response.getBody().size());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getItemById_Test(){
        ResponseEntity<Item> response = itemController.getItemById(0L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Hat", response.getBody().getName());
    }

    @Test
    public void getItemsByNameTest(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Hat");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }



}
