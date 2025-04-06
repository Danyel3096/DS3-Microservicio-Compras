package com.ds3.team8.orders_service.exceptions;

public class OrderItemNotFoundException extends RuntimeException {
    public OrderItemNotFoundException(Long id, Long order_id) {
        super("El item con ID " + id + ", de la orden con ID " + order_id + " no fue encontrado.");
    }

    public OrderItemNotFoundException(String name) {
        super("El item de la orden con nombre '" + name + "' no fue encontrado.");
    }
}
