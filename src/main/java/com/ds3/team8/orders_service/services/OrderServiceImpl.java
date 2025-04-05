package com.ds3.team8.orders_service.services;

import com.ds3.team8.orders_service.repositories.IOrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements IOrderService {

    private IOrderRepository orderRepository;

    public OrderServiceImpl(IOrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }
}
