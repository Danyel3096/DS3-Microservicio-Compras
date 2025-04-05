package com.ds3.team8.orders_service.entities;

import java.sql.Date;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Genera automáticamente getters, setters, equals, hashCode y toString
@NoArgsConstructor  // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Entity  // Indica que esta clase es una entidad JPA
@Table(name = "orders")  // Nombre de la tabla en la base de datos
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Autoincremental
    private Long id;

    @Column(nullable = false)
    private Long user_id; // ID del usuario que realizó la orden

    @Column(nullable = false)
    private Date order_date; // Número de telefono del usuario

    @Column(nullable = false)
    private String status; // Estado de la orden (pendiente, en proceso, completada, cancelada)

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}