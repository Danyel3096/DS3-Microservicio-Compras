package com.ds3.team8.orders_service.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El estado de la orden es obligatorio")
    @Size(min = 3, max = 50, message = "El estado debe tener entre 3 y 50 caracteres")
    private String status;

    // Lista de items que se van a incluir en la orden
    private List<OrderItemRequest> items;

    // Fecha opcionalmente enviada desde el cliente
    private LocalDateTime orderDate;
}
