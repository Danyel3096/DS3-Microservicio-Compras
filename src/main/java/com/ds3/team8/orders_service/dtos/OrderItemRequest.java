package com.ds3.team8.orders_service.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {
    @NotNull(message = "El campo 'orderId' es obligatorio")
    private Long orderId;

    @NotNull(message = "El campo 'productId' es obligatorio")
    private Long productId;

    @NotNull(message = "El campo 'quantity' es obligatorio")
    @Min(value = 1, message = "El campo 'quantity' debe ser mayor que cero")
    private Integer quantity;
}
