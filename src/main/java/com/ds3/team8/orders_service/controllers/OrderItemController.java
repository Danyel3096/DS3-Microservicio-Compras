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
@RequestMapping("/api/v1/orders/:id/order-item") // Indica la URL base para acceder a los servicios de esta clase
public class OrderItemController {

    private IOrderItemService orderItemService;

    public OrderItemController(IOrderItemService orderItemService){
        this.orderItemService = orderItemService;
    }

    // Obtener todos los items de un item para una órden
    @GetMapping
    public ResponseEntity<List<OrderItemResponse>> getAllItemOrders() {
        return ResponseEntity.ok(orderItemService.findAll());
    }

    // Crear un item para una órden
    @PostMapping
    public ResponseEntity<OrderItemResponse> saveOrder(@Valid @RequestBody OrderItemRequest orderItemRequest) {
        OrderItemResponse savedItemOrder = orderItemService.save(orderItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItemOrder);
    }

    // Actualizar un item para una órden
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItemOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderItemRequest orderItemRequest) {
        OrderItemResponse updatedOrder = orderItemService.update(id, orderItemRequest);
        return ResponseEntity.ok(updatedOrder);
    }

    // Busca item para una órdens con paginación
    // Cambia la URL a algo como /api/v1/orders/pageable?page=0&size=10&sort=firstName,asc
    // Para el sort= se puede usar cualquier atributo de la entidad Order
    // Por ejemplo para ordenar por apellido desde la Z a la A sería sort=lastName,desc
    // Para el caso de roles se puede usar sort=role.name,asc (sort=entity.attribute,asc o desc)
    @GetMapping("/pageable")
    public Page<OrderItemResponse> findAllPageable(Pageable pageable) {
        return orderItemService.findAllPageable(pageable);
    }


    // Obtener un item para una órden por ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderItemService.findById(id));
    }

    // Eliminación lógica de un item para una órden
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderItemService.delete(id);
        return ResponseEntity.noContent().build();
    }


}