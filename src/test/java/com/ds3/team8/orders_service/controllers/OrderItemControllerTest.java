package com.ds3.team8.orders_service.controllers;

import com.ds3.team8.orders_service.dtos.OrderItemRequest;
import com.ds3.team8.orders_service.dtos.OrderItemResponse;
import com.ds3.team8.orders_service.services.IOrderItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1/order-items") // Indica la URL base para acceder a los servicios de esta clase
public class OrderItemControllerTest {
    
    private IOrderItemService orderItemService;

    public OrderItemController(IOrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    // Obtener todos los items de una orden
    @GetMapping
    public ResponseEntity<List<OrderItemResponse>> getAllOrderItems() {
        return ResponseEntity.ok(orderItemService.findAll());
    }

    // Crear una orden
    @PostMapping
    public ResponseEntity<OrderItemResponse> saveOrderItem(@Valid @RequestBody OrderItemRequest orderItemRequest) {
        OrderItemResponse savedOrderItem = orderItemService.save(orderItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrderItem);
    }

    // Actualizar una orden
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemResponse> updateOrderItem(
            @PathVariable Long id,
            @Valid @RequestBody OrderItemRequest orderItemRequest) {
        OrderItemResponse updatedOrderItem = orderItemService.update(id, orderItemRequest);
        return ResponseEntity.ok(updatedOrderItem);
    }

    // Buscar itemes de una orden con paginación
    // Cambia la URL a algo como /api/v1/order-items/pageable?page=0&size=10&sort=firstName,asc
    // Para el sort= se puede usar cualquier atributo de la entidad User
    // Por ejemplo para ordenar por apellido desde la Z a la A sería sort=lastName,desc
    // Para el caso de roles se puede usar sort=role.name,asc (sort=entity.attribute,asc o desc)
    @GetMapping("/pageable")
    public Page<OrderItemResponse> findAllPageable(Pageable pageable) {
        return orderItemService.findAllPageable(pageable);
    }

    // Obtener un item de una orden por ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponse> getOrderItemById(@PathVariable Long id) {
        return ResponseEntity.ok(orderItemService.findById(id));
    }

    // Eliminación lógica de un item de una orden
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        orderItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
