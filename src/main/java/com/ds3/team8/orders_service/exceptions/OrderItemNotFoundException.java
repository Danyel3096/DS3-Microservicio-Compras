package com.ds3.team8.orders_service.exceptions;

public class OrderItemNotFoundException extends RuntimeException {
    public OrderItemNotFoundException(Long id) {
        super("El item con ID " + id + " no fue encontrado en la orden.");
    }

    public OrderItemNotFoundException(String name) {
        super("El item de la orden con nombre '" + name + "' no fue encontrado.");
    }
}
