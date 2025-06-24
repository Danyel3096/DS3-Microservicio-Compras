package com.ds3.team8.orders_service.controllers;

import com.ds3.team8.orders_service.client.enums.Role;
import com.ds3.team8.orders_service.dtos.OrderRequest;
import com.ds3.team8.orders_service.dtos.OrderResponse;
import com.ds3.team8.orders_service.dtos.OrderStatusRequest;
import com.ds3.team8.orders_service.services.IOrderService;
import com.ds3.team8.orders_service.utils.SecurityUtil;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Pedidos", description = "Endpoints para pedidos")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    // Obtener todas las órdenes
    @Operation(summary = "Obtener todas las órdenes", description = "Obtener todas las órdenes del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(
        @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        return ResponseEntity.ok(orderService.findAll());
    }

    // Crear una nueva orden
    @Operation(summary = "Crear una nueva orden", description = "Crear una nueva orden en el sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @PostMapping
    public ResponseEntity<OrderResponse> saveOrder(
        @Valid @RequestBody OrderRequest orderRequest,
        @RequestHeader("X-Authenticated-User-Id") String userIdHeader
    ) {
        Long userId = SecurityUtil.parseUserId(userIdHeader);
        OrderResponse savedOrder = orderService.save(orderRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    // Eliminar una orden por ID
    @Operation(summary = "Eliminar una orden por ID", description = "Eliminar una orden por su ID del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
        @PathVariable Long id,
        @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener orden por ID
    @Operation(summary = "Obtener una orden por su id", description = "Obtener una orden por su id del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    // Obtener órdenes paginadas
    // Ejemplo URL /api/v1/orders/pageable?page=0&size=8
    @Operation(summary = "Obtener órdenes paginadas", description = "Obtener órdenes paginadas del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/pageable")
    public Page<OrderResponse> getOrdersPageable(
        Pageable pageable,
        @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        return orderService.findAllPageable(pageable);
    }

    // Obtener órdenes por usuario
    @Operation(summary = "Obtener órdenes por usuario", description = "Obtener órdenes por usuario del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/user")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(
        @RequestHeader("X-Authenticated-User-Id") String userIdHeader
    ) {
        Long userId = SecurityUtil.parseUserId(userIdHeader);
        return ResponseEntity.ok(orderService.findAllByUserId(userId));
    }

    // Obtener órdenes por usuario paginadas
    // Ejemplo URL /api/v1/orders/user/pageable?page=0&size=8
    @Operation(summary = "Obtener órdenes por usuario paginadas", description = "Obtener órdenes por usuario paginadas del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/user/pageable")
    public Page<OrderResponse> getOrdersByUserIdPageable(
        @RequestHeader("X-Authenticated-User-Id") String userIdHeader,
        Pageable pageable
    ) {
        Long userId = SecurityUtil.parseUserId(userIdHeader);
        return orderService.findAllByUserIdPageable(userId, pageable);
    }

    // Actualizar el estado de una orden
    @Hidden
    @PostMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
        @PathVariable Long id,
        @Valid @RequestBody OrderStatusRequest orderStatusRequest
    ) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, orderStatusRequest));
    }

    // Verificar si un usuario tiene órdenes activas
    @Hidden
    @GetMapping("/user/{userId}/exists")
    public Boolean userHasOrders(@PathVariable Long userId) {
        return orderService.userHasOrders(userId);
    }
}
