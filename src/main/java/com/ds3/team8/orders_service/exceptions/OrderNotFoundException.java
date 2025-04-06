package com.ds3.team8.orders_service.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("La orden con ID " + id + " no fue encontrada.");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
