package com.ds3.team8.orders_service.repositories;

import com.ds3.team8.orders_service.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByIdAndIsActiveTrue(Long id); // Obtener OrderItem por ID y activo
    List<OrderItem> findAllByIsActiveTrue(); // Obtener todos los OrderItems activos
    Page<OrderItem> findAllByIsActiveTrue(Pageable pageable); // Obtener todos los OrderItems activos con paginación
    List<OrderItem> findAllByOrderIdAndIsActiveTrue(Long orderId); // Obtener OrderItems por ID de orden y activo
    Page<OrderItem> findAllByOrderIdAndIsActiveTrue(Long orderId, Pageable pageable); // Obtener OrderItems por ID de orden y activo con paginación
    Boolean existsByProductIdAndOrderIsActiveTrue(Long productId); // Verificar si un producto está en algún OrderItem activo
}