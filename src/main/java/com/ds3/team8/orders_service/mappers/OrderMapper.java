package com.ds3.team8.orders_service.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.ds3.team8.orders_service.dtos.OrderResponse;
import com.ds3.team8.orders_service.entities.Order;

@Component
public class OrderMapper {

    public OrderResponse toOrderResponse(Order order) {
        if (order == null) return null;

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getOrderStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    public List<OrderResponse> toOrderResponseList(List<Order> orders) {
        if (orders == null) return List.of();
        if (orders.isEmpty()) return List.of();
        
        return orders.stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
    }
}
