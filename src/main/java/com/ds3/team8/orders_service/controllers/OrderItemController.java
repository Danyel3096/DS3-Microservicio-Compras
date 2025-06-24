package com.ds3.team8.orders_service.controllers;

import com.ds3.team8.orders_service.client.enums.Role;
import com.ds3.team8.orders_service.dtos.OrderItemResponse;
import com.ds3.team8.orders_service.services.IOrderItemService;
import com.ds3.team8.orders_service.utils.SecurityUtil;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-items")
@Tag(name = "Ítems de Pedido", description = "Endpoints para ítems de pedido")
public class OrderItemController {

    private final IOrderItemService orderItemService;

    public OrderItemController(IOrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    // Obtener todos los ítems
    @Operation(summary = "Obtener todos los ítems", description = "Obtener todos los ítems de pedido del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping
    public ResponseEntity<List<OrderItemResponse>> getAllOrderItems(
        @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        return ResponseEntity.ok(orderItemService.findAll());
    }

    // Obtener ítem por ID
    @Operation(summary = "Obtener un ítem de orden por su id", description = "Obtener un ítem de pedido por su id del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponse> getOrderItemById(@PathVariable Long id) {
        return ResponseEntity.ok(orderItemService.findById(id));
    }

    // Obtener ítems paginados
    // Ejemplo URL /api/v1/order-items/pageable?page=0&size=8
    @Operation(summary = "Obtener ítems de orden paginados", description = "Obtener ítems de pedido paginados del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/pageable")
    public Page<OrderItemResponse> getOrderItemsPageable(
        Pageable pageable,
        @RequestHeader("X-Authenticated-User-Role") String roleHeader
    ) {
        SecurityUtil.validateRole(roleHeader, Role.ADMIN);
        return orderItemService.findAllPageable(pageable);
    }

    // Obtener ítems por ID de orden
    @Operation(summary = "Obtener ítems de orden por ID de orden", description = "Obtener ítems de pedido por ID de orden del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemResponse>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItemResponse> items = orderItemService.findAllByOrderId(orderId);
        return ResponseEntity.ok(items);
    }

    // Obtener ítems por ID de orden paginados
    // Ejemplo URL /api/v1/order-items/order/1/pageable?page=0&size=8
    @Operation(summary = "Obtener ítems de orden por ID de orden paginados", description = "Obtener ítems de pedido por ID de orden paginados del sistema.", security = { @SecurityRequirement(name = "Bearer Authentication") })
    @GetMapping("/order/{orderId}/pageable")
    public Page<OrderItemResponse> getOrderItemsByOrderIdPageable(
            @PathVariable Long orderId,
            Pageable pageable) {
        return orderItemService.findAllByOrderIdPageable(orderId, pageable);
    }

    // Verificar si existe un ítem de orden por ID de producto
    @Hidden
    @GetMapping("/product/{productId}/exists")
    public Boolean orderItemHasProducts(@PathVariable Long productId) {
        return orderItemService.orderItemHasProducts(productId);
    }
}
