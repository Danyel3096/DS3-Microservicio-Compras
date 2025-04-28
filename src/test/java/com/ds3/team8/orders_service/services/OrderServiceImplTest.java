package com.ds3.team8.orders_service.services;

import com.ds3.team8.orders_service.dtos.OrderRequest;
import com.ds3.team8.orders_service.dtos.OrderResponse;
import com.ds3.team8.orders_service.entities.Order;
import com.ds3.team8.orders_service.entities.OrderItem;
import com.ds3.team8.orders_service.exceptions.OrderNotFoundException;
import com.ds3.team8.orders_service.repositories.IOrderItemRepository;
import com.ds3.team8.orders_service.repositories.IOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IOrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private OrderRequest orderRequest;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        order = new Order();
        order.setId(1L);
        order.setUserId(100L);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Created");
        order.setIsActive(true);

        orderRequest = new OrderRequest();
        orderRequest.setUserId(100L);
        orderRequest.setOrderDate(LocalDateTime.now());
        orderRequest.setStatus("Created");

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setOrder(order);
        orderItem.setProductId(200L);
        orderItem.setQuantity(2L);
        orderItem.setIsActive(true);
    }

    @Test
    void findAll_ReturnsOrderList() {
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderItemRepository.findByOrderId(order.getId())).thenReturn(List.of(orderItem));

        List<OrderResponse> orders = orderService.findAll();

        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
        assertEquals(order.getUserId(), orders.get(0).getUserId());

        verify(orderRepository, times(1)).findAll();
        verify(orderItemRepository, times(1)).findByOrderId(order.getId());
    }

    @Test
    void save_SuccessfullyCreatesOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.save(orderRequest);

        assertNotNull(response);
        assertEquals(order.getUserId(), response.getUserId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void update_WhenOrderExists_UpdatesOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderRequest updatedRequest = new OrderRequest();
        updatedRequest.setStatus("Updated");

        OrderResponse response = orderService.update(1L, updatedRequest);

        assertNotNull(response);
        assertEquals("Updated", response.getStatus());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void update_WhenOrderDoesNotExist_ThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.update(1L, orderRequest));

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void findAllPageable_ReturnsPagedOrders() {
        Page<Order> orderPage = new PageImpl<>(List.of(order));
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(orderPage);
        when(orderItemRepository.findByOrderId(order.getId())).thenReturn(List.of(orderItem));

        Page<OrderResponse> responsePage = orderService.findAllPageable(Pageable.unpaged());

        assertFalse(responsePage.isEmpty());
        assertEquals(1, responsePage.getTotalElements());

        verify(orderRepository, times(1)).findAll(any(Pageable.class));
        verify(orderItemRepository, times(1)).findByOrderId(order.getId());
    }

    @Test
    void findById_WhenOrderExists_ReturnsOrderResponse() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(order.getId())).thenReturn(List.of(orderItem));

        OrderResponse response = orderService.findById(1L);

        assertNotNull(response);
        assertEquals(order.getUserId(), response.getUserId());

        verify(orderRepository, times(1)).findById(1L);
        verify(orderItemRepository, times(1)).findByOrderId(order.getId());
    }

    @Test
    void findById_WhenOrderDoesNotExist_ThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.findById(1L));

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void delete_WhenOrderExists_SetsInactive() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.delete(1L);

        assertFalse(order.getIsActive());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void delete_WhenOrderDoesNotExist_ThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.delete(1L));

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }
}
