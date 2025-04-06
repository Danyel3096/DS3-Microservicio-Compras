/*package com.ds3.team8.orders_service.services;

import com.ds3.team8.orders_service.dtos.OrderRequest;
import com.ds3.team8.orders_service.dtos.OrderResponse;
import com.ds3.team8.orders_service.entities.Order;
import com.ds3.team8.orders_service.entities.OrderItem;
import com.ds3.team8.orders_service.exceptions.OrderItemNotFoundException;
import com.ds3.team8.orders_service.exceptions.OrderNotFoundException;
import com.ds3.team8.orders_service.repositories.IOrderRepository;
import com.ds3.team8.orders_service.repositories.IOrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import javax.management.relation.RoleNotFoundException;
*/
//
package com.ds3.team8.orders_service.services;

import com.ds3.team8.orders_service.dtos.OrderRequest;
import com.ds3.team8.orders_service.dtos.OrderResponse;
import com.ds3.team8.orders_service.entities.Order;
import com.ds3.team8.orders_service.exceptions.OrderNotFoundException;
import com.ds3.team8.orders_service.repositories.IOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository orderRepository;

    public OrderServiceImpl(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Obtener todas las órdenes
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        // Obtener todas las órdenes
        return orderRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Crear una orden
    @Override
    @Transactional
    public OrderResponse save(OrderRequest orderRequest) {
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setOrderDate(orderRequest.getOrderDate());
        order.setStatus(orderRequest.getStatus());
        order.setIsActive(true); // Asegurar que la orden esté activa por defecto

        // Guardar y devolver DTO
        Order savedOrder = orderRepository.save(order);
        return convertToResponse(savedOrder);
    }

    // Actualizar/Modificar una orden
    @Override
    @Transactional
    public OrderResponse update(Long id, OrderRequest orderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        // Actualizar los campos solo si no son nulos
        if (orderRequest.getUserId() != null) order.setUserId(orderRequest.getUserId());
        if (orderRequest.getOrderDate() != null) order.setOrderDate(orderRequest.getOrderDate());
        if (orderRequest.getStatus() != null) order.setStatus(orderRequest.getStatus());

        // Guardar cambios y retornar el DTO
        Order updatedOrder = orderRepository.save(order);
        return convertToResponse(updatedOrder);
    }

    // Buscar órdenes con paginación
    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> findAllPageable(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    // Buscar una orden por ID
    @Override
    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return convertToResponse(order);
    }

    // Eliminar (desactivar) una orden
    @Override
    @Transactional
    public void delete(Long id){
        // Buscar la orden en la base de datos
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        // Cambiar el estado a inactivo
        existingOrder.setIsActive(false);

        // Guardar los cambios en la base de datos
        orderRepository.save(existingOrder);
    }

    private OrderResponse convertToResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getOrderDate(),
                order.getStatus(),
                order.getIsActive()
        );
    }
}
