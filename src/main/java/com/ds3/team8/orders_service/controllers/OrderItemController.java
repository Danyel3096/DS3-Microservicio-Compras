package com.ds3.team8.orders_service.controllers;

import com.ds3.team8.orders_service.dtos.OrderItemRequest;
import com.ds3.team8.orders_service.dtos.OrderItemResponse;
import com.ds3.team8.orders_service.services.IOrderItemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-items")
public class OrderItemController {

    private final IOrderItemService orderItemService;

    public OrderItemController(IOrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    // Obtener todos los ítems
    @GetMapping
    public ResponseEntity<List<OrderItemResponse>> getAllOrderItems() {
        return ResponseEntity.ok(orderItemService.findAll());
    }

    // Crear un ítem de orden
    @PostMapping
    public ResponseEntity<OrderItemResponse> saveOrderItem(@Valid @RequestBody OrderItemRequest orderItemRequest) {
        OrderItemResponse savedItem = orderItemService.save(orderItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }

    // Actualizar ítem
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemResponse> updateOrderItem(
            @PathVariable Long id,
            @Valid @RequestBody OrderItemRequest orderItemRequest) {
        OrderItemResponse updatedItem = orderItemService.update(id, orderItemRequest);
        return ResponseEntity.ok(updatedItem);
    }

    // Obtener ítem por ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponse> getOrderItemById(@PathVariable Long id) {
        return ResponseEntity.ok(orderItemService.findById(id));
    }

    // Eliminar ítem lógicamente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        orderItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener ítems paginados
    // Obtener órdenes paginadas
    // Cambia la URL a algo como /api/v1/order-items/pageable?page=0&size=10&sort=productId,desc
    // Para el sort= se puede usar cualquier atributo de la entidad User
    // Por ejemplo para ordenar por apellido desde la Z a la A sería sort=lastName,desc
    // Para el caso de roles se puede usar sort=role.name,asc (sort=entity.attribute,asc o desc)
    @GetMapping("/pageable")
    public Page<OrderItemResponse> getOrderItemsPageable(Pageable pageable) {
        return orderItemService.findAllPageable(pageable);
    }
}
