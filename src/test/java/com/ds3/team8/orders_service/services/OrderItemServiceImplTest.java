package com.ds3.team8.orders_service.services;

import com.ds3.team8.orders_service.dtos.OrderItemRequest;
import com.ds3.team8.orders_service.dtos.OrderItemResponse;
import com.ds3.team8.orders_service.entities.Order;
import com.ds3.team8.orders_service.entities.OrderItem;
import com.ds3.team8.orders_service.exceptions.OrderItemNotFoundException;
import com.ds3.team8.orders_service.exceptions.OrderNotFoundException;
import com.ds3.team8.orders_service.repositories.IOrderItemRepository;
import com.ds3.team8.orders_service.repositories.IOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest {

    @Mock
    private IOrderItemRepository orderItemRepository;

    @Mock
    private IOrderRepository orderRepository;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    private Order order;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setOrder(order);
        orderItem.setProductId(100L);
        orderItem.setQuantity(5L);
        orderItem.setIsActive(true);
    }

    @Test
    void findAll_ShouldReturnListOfOrderItemResponses() {
        when(orderItemRepository.findAll()).thenReturn(List.of(orderItem));

        List<OrderItemResponse> responses = orderItemService.findAll();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getProductId()).isEqualTo(100L);
    }

    @Test
    void save_ShouldSaveAndReturnOrderItemResponse() {
        OrderItemRequest request = new OrderItemRequest(1L, 100L, 5L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItemResponse response = orderItemService.save(request);

        assertThat(response.getProductId()).isEqualTo(100L);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void save_ShouldThrowOrderNotFoundException_WhenOrderDoesNotExist() {
        OrderItemRequest request = new OrderItemRequest(1L, 100L, 5L);

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderItemService.save(request));
    }

    @Test
    void update_ShouldUpdateAndReturnOrderItemResponse() {
        OrderItemRequest request = new OrderItemRequest(2L, 200L, 10L);

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItemResponse response = orderItemService.update(1L, request);

        assertThat(response).isNotNull();
        verify(orderItemRepository).save(any(OrderItem.class));
    }

    @Test
    void update_ShouldThrowOrderItemNotFoundException_WhenItemNotFound() {
        OrderItemRequest request = new OrderItemRequest(2L, 200L, 10L);

        when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderItemNotFoundException.class, () -> orderItemService.update(1L, request));
    }

    @Test
    void findAllPageable_ShouldReturnPageOfOrderItemResponses() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<OrderItem> page = new PageImpl<>(List.of(orderItem));

        when(orderItemRepository.findAll(pageable)).thenReturn(page);

        Page<OrderItemResponse> responsePage = orderItemService.findAllPageable(pageable);

        assertThat(responsePage.getContent()).hasSize(1);
        assertThat(responsePage.getContent().get(0).getProductId()).isEqualTo(100L);
    }

    @Test
    void findById_ShouldReturnOrderItemResponse() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));

        OrderItemResponse response = orderItemService.findById(1L);

        assertThat(response.getProductId()).isEqualTo(100L);
    }

    @Test
    void findById_ShouldThrowOrderItemNotFoundException_WhenNotFound() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderItemNotFoundException.class, () -> orderItemService.findById(1L));
    }

    @Test
    void findByOrderId_ShouldReturnListOfOrderItemResponses() {
        when(orderItemRepository.findByOrderId(1L)).thenReturn(List.of(orderItem));

        List<OrderItemResponse> responses = orderItemService.findByOrderId(1L);

        assertThat(responses).hasSize(1);
    }

    @Test
    void delete_ShouldDeactivateOrderItem() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));

        orderItemService.delete(1L);

        verify(orderItemRepository).save(orderItem);
        assertThat(orderItem.getIsActive()).isFalse();
    }

    @Test
    void delete_ShouldThrowOrderItemNotFoundException_WhenNotFound() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderItemNotFoundException.class, () -> orderItemService.delete(1L));
    }
}
