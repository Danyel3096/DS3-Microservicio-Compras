package com.ds3.team8.orders_service.repositories;

import com.ds3.team8.orders_service.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndIsActiveTrue(Long id); // Obtener Order por ID y activo
    List<Order> findAllByIsActiveTrue(); // Obtener todos los Orders activos
    Page<Order> findAllByIsActiveTrue(Pageable pageable); // Obtener todos los Orders activos con paginación
    List<Order> findAllByCustomerIdAndIsActiveTrue(Long userId); // Obtener Orders por ID de usuario y activo
    Page<Order> findAllByCustomerIdAndIsActiveTrue(Long userId, Pageable pageable); // Obtener Orders por ID de usuario y activo con paginación
    Boolean existsByCustomerIdAndIsActiveTrue(Long userId); // Verificar si un usuario tiene órdenes activas
}