package com.ds3.team8.orders_service.controllers;

import com.ds3.team8.orders_service.dtos.OrderItemRequest;
import com.ds3.team8.orders_service.dtos.OrderItemResponse;
import com.ds3.team8.orders_service.services.IOrderItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderItemControllerTest {
    
    private MockMvc mockMvc;
    
    @Mock
    private IOrderItemService orderItemService;

    @InjectMocks
    private OrderItemController orderItemController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(orderItemController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Debe obtener todos los items de una orden")
    void testGetAllOrderItems() throws Exception {
        List<OrderItemResponse> orderItems = List.of(new OrderItemResponse(1L, 1L, 1L, 8L, true));

        when(orderItemService.findAll()).thenReturn(orderItems);

        mockMvc.perform(get("/api/v1/order-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(orderItems.size()))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("Debe crear un item de orden")
    void testSaveOrder() throws Exception {
        OrderItemRequest request = new OrderItemRequest(4L, 1L, 8L);
        OrderItemResponse response = new OrderItemResponse(2L, 4L, 1L, 8L, true);

        when(orderItemService.save(any(OrderItemRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/order-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.orderId").value(4L));
    }

    @Test
    @DisplayName("Debe actualizar un item de orden")
    void testUpdateOrder() throws Exception {
        OrderItemRequest request = new OrderItemRequest(3L, 1L, 8L);
        OrderItemResponse response = new OrderItemResponse(1L, 3L, 1L, 8L, true);

        when(orderItemService.update(eq(1L), any(OrderItemRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/order-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(3L));
    }

    @Test
    @DisplayName("Debe obtener un item de orden por ID")
    void testGetOrderById() throws Exception {
        OrderItemResponse order = new OrderItemResponse(1L, 2L, 1L, 8L, true);

        when(orderItemService.findById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/v1/order-items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderId").value(2L));
    }

    @Test
    @DisplayName("Debe eliminar un item de una orden")
    void testDeleteOrder() throws Exception {
        doNothing().when(orderItemService).delete(1L);

        mockMvc.perform(delete("/api/v1/order-items/1"))
                .andExpect(status().isNoContent());
    }
}
