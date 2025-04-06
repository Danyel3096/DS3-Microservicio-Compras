/*package com.ds3.team8.orders_service.services;

public interface IOrderService {
}*/
package com.ds3.team8.orders_service.services;

import com.ds3.team8.orders_service.dtos.OrderRequest;
import com.ds3.team8.orders_service.dtos.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderService {
    List<OrderResponse> findAll(); // Obtener todas las órdenes
    OrderResponse save(OrderRequest orderRequest); // Crear una orden
    OrderResponse update(Long id, OrderRequest orderRequest); // Actualizar/Modificar una orden
    Page<OrderResponse> findAllPageable(Pageable pageable); // Obtener todas las órdenes con paginación
    OrderResponse findById(Long id); // Obtener una orden por su id
    void delete(Long id); // Eliminar una orden por su id (inactivarla)
}
