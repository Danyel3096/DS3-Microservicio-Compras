package com.ds3.team8.orders_service.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ds3.team8.orders_service.dtos.OrderRequest;
import com.ds3.team8.orders_service.dtos.OrderResponse;
import com.ds3.team8.orders_service.entities.Order;
import com.ds3.team8.orders_service.services.IOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private IOrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Debe obtener todas las ordenes")
    void testGetAllOrders() throws Exception {
        List<OrderResponse> orders = List.of(new OrderResponse(1L, 3L, LocalDateTime.parse("2025-05-23T13:27:00"), "En preparación", true, new ArrayList<>()));

        when(orderService.findAll()).thenReturn(orders);

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(orders.size()))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("Debe crear una orden")
    void testSaveOrder() throws Exception {
        OrderRequest request = new OrderRequest(2L, "Entregado", new ArrayList<>(), LocalDateTime.parse("2025-05-09T21:06:00"));
        OrderResponse response = new OrderResponse(2L, 2L, LocalDateTime.parse("2025-05-09T21:06:00"), "Entregado", true, new ArrayList<>());

        when(orderService.save(any(OrderRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.status").value("Entregado"));
    }

    @Test
    @DisplayName("Debe actualizar una orden")
    void testUpdateOrder() throws Exception {
        OrderRequest request = new OrderRequest(2L, "En camino", new ArrayList<>(), LocalDateTime.parse("2025-06-09T21:17:00"));
        OrderResponse response = new OrderResponse(3L, 2L, LocalDateTime.parse("2025-06-09T21:17:00"), "En camino", true, new ArrayList<>());

        when(orderService.update(eq(1L), any(OrderRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L));
    }

    @Test
    @DisplayName("Debe obtener una orden por ID")
    void testGetOrderById() throws Exception {
        OrderResponse order = new OrderResponse(1L, 2L, LocalDateTime.parse("2025-06-19T01:17:00"), "En camino", true, new ArrayList<>());

        when(orderService.findById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("En camino"));
    }

    @Test
    @DisplayName("Debe eliminar una orden")
    void testDeleteOrder() throws Exception {
        doNothing().when(orderService).delete(1L);

        mockMvc.perform(delete("/api/v1/orders/1"))
                .andExpect(status().isNoContent());
    }
}
