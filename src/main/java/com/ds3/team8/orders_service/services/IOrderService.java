package com.ds3.team8.orders_service.services;

import com.ds3.team8.orders_service.dtos.OrderRequest;
import com.ds3.team8.orders_service.dtos.OrderResponse;
import com.ds3.team8.orders_service.dtos.OrderStatusRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderService {
    List<OrderResponse> findAll(); // Obtener todas las órdenes
    OrderResponse save(OrderRequest orderRequest, Long userId); // Crear una orden
    void delete(Long id); // Eliminar una orden por su id
    Page<OrderResponse> findAllPageable(Pageable pageable); // Obtener todas las órdenes con paginación
    OrderResponse findById(Long id); // Obtener una orden por su id
    List<OrderResponse> findAllByUserId(Long userId); // Obtener órdenes por id de usuario
    Page<OrderResponse> findAllByUserIdPageable(Long userId, Pageable pageable); // Obtener órdenes por id de usuario con paginación
    OrderResponse updateOrderStatus(Long id, OrderStatusRequest orderStatusRequest); // Actualizar el estado de una orden
    Boolean userHasOrders(Long userId); // Verificar si un usuario tiene órdenes
}
