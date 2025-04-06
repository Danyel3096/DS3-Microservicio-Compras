package com.ds3.team8.orders_service.controllers;

import com.ds3.team8.orders_service.dtos.OrderRequest;
import com.ds3.team8.orders_service.dtos.OrderResponse;
import com.ds3.team8.orders_service.services.IOrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    // Obtener todas las órdenes
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    // Crear una nueva orden
    @PostMapping
    public ResponseEntity<OrderResponse> saveOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse savedOrder = orderService.save(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    // Actualizar una orden
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse updatedOrder = orderService.update(id, orderRequest);
        return ResponseEntity.ok(updatedOrder);
    }

    // Obtener orden por ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    // Eliminar (lógicamente) una orden
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener órdenes paginadas
    @GetMapping("/pageable")
    public Page<OrderResponse> getOrdersPageable(Pageable pageable) {
        return orderService.findAllPageable(pageable);
    }
}
