package com.ds3.team8.orders_service.dtos;

import com.ds3.team8.orders_service.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusRequest {
    @NotNull(message = "El campo 'orderStatus' es obligatorio")
    private OrderStatus orderStatus;
}
