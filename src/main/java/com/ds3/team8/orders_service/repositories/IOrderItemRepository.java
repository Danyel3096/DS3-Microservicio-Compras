package com.ds3.team8.orders_service.repositories;

import com.ds3.team8.orders_service.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Obtener los ítems por ID de orden (por ejemplo, para mostrar los detalles de una orden)
    List<OrderItem> findByOrderId(Long orderId);

    // Obtener los ítems activos por ID de orden (útil si aplicas soft delete con is_active)
    List<OrderItem> findByOrderIdAndIsActiveTrue(Long orderId);
}
