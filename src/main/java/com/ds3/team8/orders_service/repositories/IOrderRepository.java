package com.ds3.team8.orders_service.repositories;

import com.ds3.team8.orders_service.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    // Obtener todas las órdenes de un usuario (útil para historial de compras, por ejemplo)
    List<Order> findByUserId(Long userId);
}

