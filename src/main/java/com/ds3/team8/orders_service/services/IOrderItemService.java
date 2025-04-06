package com.ds3.team8.orders_service.services;

import com.ds3.team8.orders_service.dtos.OrderItemRequest;
import com.ds3.team8.orders_service.dtos.OrderItemResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderItemService {
    List<OrderItemResponse> findByOrderId(Long orderId); // Obtener ítems por id de orden
    OrderItemResponse save(OrderItemRequest orderItemRequest); // Crear un ítem de orden
    OrderItemResponse update(Long id, OrderItemRequest orderItemRequest); // Actualizar un ítem de orden
    void delete(Long id); // Eliminar (inactivar) un ítem por su id
    OrderItemResponse findById(Long id); // Obtener un ítem por su id
    Page<OrderItemResponse> findAllPageable(Pageable pageable); // Obtener ítems paginados
    List<OrderItemResponse> findAll(); // Obtener todos los ítems de orden
}
