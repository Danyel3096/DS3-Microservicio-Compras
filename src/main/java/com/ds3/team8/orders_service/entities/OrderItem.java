package com.ds3.team8.orders_service.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Genera automáticamente getters, setters, equals, hashCode y toString
@NoArgsConstructor  // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Entity  // Indica que esta clase es una entidad JPA
@Table(name = "order_items")  // Nombre de la tabla en la base de datos
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Autoincremental
    private Long id;

    @Column(nullable = false)
    private Long order_id; // ID de la orden a la que pertenece el item

    @Column(nullable = false)
    private Long product_id; // Identificador del producto

    @Column(nullable = false)
    private Long quantity; // Cantidad del producto (stock)

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}