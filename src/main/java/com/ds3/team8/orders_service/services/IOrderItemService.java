package com.ds3.team8.orders_service.services;

import com.ds3.team8.orders_service.dtos.OrderItemRequest;
import com.ds3.team8.orders_service.dtos.OrderItemResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderItemService {
    List<OrderItemResponse> findByOrderId(Long orderId); // Obtener ítems por id de orden
    OrderItemResponse save(OrderItemRequest orderItemRequest); // Crear un ítem de orden
    OrderItemResponse update(Long id, OrderItemRequest orderItemRequest); // Actualizar un ítem de orden
    void delete(Long id); // Eliminar (inactivar) un ítem por su id
}
