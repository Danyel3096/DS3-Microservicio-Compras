package com.ds3.team8.orders_service.services;

import com.ds3.team8.orders_service.repositories.IOrderItemRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements IOrderItemService {

    private IOrderItemRepository orderItemRepository;

    public OrderItemServiceImpl(IOrderItemRepository orderItemRepository){
        this.orderItemRepository = orderItemRepository;
    }
}
