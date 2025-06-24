package com.ds3.team8.orders_service.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.ds3.team8.orders_service.dtos.OrderItemRequest;
import com.ds3.team8.orders_service.dtos.OrderItemResponse;
import com.ds3.team8.orders_service.entities.OrderItem;

@Component
public class OrderItemMapper {

    public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        if (orderItem == null) return null;

        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getOrder().getId(),
                orderItem.getProductId(),
                orderItem.getQuantity(),
                orderItem.getCreatedAt(),
                orderItem.getUpdatedAt()
        );
    }

    public OrderItem toOrderItem(OrderItemRequest orderItemRequest) {
        if (orderItemRequest == null) return null;
        
        return new OrderItem(
                orderItemRequest.getProductId(),
                orderItemRequest.getQuantity()
        );
    }

    public OrderItem updateOrderItem(OrderItem orderItem, OrderItemRequest orderItemRequest) {
        if (orderItem == null || orderItemRequest == null) return null;

        orderItem.setProductId(orderItemRequest.getProductId());
        orderItem.setQuantity(orderItemRequest.getQuantity());

        return orderItem;
    }

    public List<OrderItemResponse> toOrderItemResponseList(List<OrderItem> orderItems) {
        if (orderItems == null) return List.of();
        if (orderItems.isEmpty()) return List.of();

        return orderItems.stream()
                .map(this::toOrderItemResponse)
                .collect(Collectors.toList());
    }
}
