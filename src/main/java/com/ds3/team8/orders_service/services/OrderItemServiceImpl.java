package com.ds3.team8.orders_service.services;

import com.ds3.team8.orders_service.dtos.OrderItemRequest;
import com.ds3.team8.orders_service.dtos.OrderItemResponse;
import com.ds3.team8.orders_service.entities.Order;
import com.ds3.team8.orders_service.entities.OrderItem;
import com.ds3.team8.orders_service.exceptions.OrderItemNotFoundException;
import com.ds3.team8.orders_service.exceptions.OrderNotFoundException;
import com.ds3.team8.orders_service.repositories.IOrderItemRepository;
import com.ds3.team8.orders_service.repositories.IOrderRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderItemServiceImpl implements IOrderItemService {

    private final IOrderItemRepository orderItemRepository;

    private final IOrderRepository orderRepository;

    public OrderItemServiceImpl(IOrderItemRepository orderItemRepository, IOrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
    }

    // Obtener todos los items de orden
    @Override
    @Transactional(readOnly = true)
    public List<OrderItemResponse> findAll() {
        return orderItemRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Crear un nuevo item de orden
    @Override
    @Transactional
    public OrderItemResponse save(OrderItemRequest request) {
        OrderItem item = new OrderItem();
        Order order = orderRepository.findById(request.getOrderId())
        .orElseThrow(() -> new OrderNotFoundException(request.getOrderId()));
        item.setOrder(order);
        item.setProductId(request.getProductId());
        item.setQuantity(request.getQuantity());
        item.setIsActive(true);

        OrderItem savedItem = orderItemRepository.save(item);
        return convertToResponse(savedItem);
    }

    // Actualizar un item de orden
    @Override
    @Transactional
    public OrderItemResponse update(Long id, OrderItemRequest request) {
        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException(id));

        if (request.getOrderId() != null) {
            Order order = new Order();
            order.setId(request.getOrderId());
            item.setOrder(order);
        }                
        if (request.getProductId() != null) item.setProductId(request.getProductId());
        if (request.getQuantity() != null) item.setQuantity(request.getQuantity());

        OrderItem updatedItem = orderItemRepository.save(item);
        return convertToResponse(updatedItem);
    }

    // Buscar items con paginación
    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemResponse> findAllPageable(Pageable pageable) {
        return orderItemRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    // Buscar item por ID
    @Override
    @Transactional(readOnly = true)
    public OrderItemResponse findById(Long id) {
        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException(id));

        return convertToResponse(item);
    }

    // Buscar items por ID de orden
    @Override
    @Transactional(readOnly = true)
    public List<OrderItemResponse> findByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Eliminar (desactivar) un item
    @Override
    @Transactional
    public void delete(Long id){
        OrderItem existingItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException(id));

        existingItem.setIsActive(false);
        orderItemRepository.save(existingItem);
    }

    private OrderItemResponse convertToResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getOrder().getId(),/*OJO: CAMBIAR EN ORDERITEM.JAVA TAMBIEN SI SE USA item.getOrderId(),*/
                item.getProductId(),
                item.getQuantity(),
                item.getIsActive()
        );
    }
}
