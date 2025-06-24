package com.ds3.team8.orders_service.services;

import com.ds3.team8.orders_service.dtos.OrderItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderItemService {
    OrderItemResponse findById(Long id); // Obtener un ítem por su id
    Page<OrderItemResponse> findAllPageable(Pageable pageable); // Obtener ítems paginados
    List<OrderItemResponse> findAll(); // Obtener todos los ítems de orden
    List<OrderItemResponse> findAllByOrderId(Long orderId); // Obtener ítems de orden por id de orden
    Page<OrderItemResponse> findAllByOrderIdPageable(Long orderId, Pageable pageable); // Obtener ítems de orden por id de orden con paginación
    Boolean orderItemHasProducts(Long productId); // Verificar si un producto está en algún OrderItem activo
}
