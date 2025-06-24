package com.ds3.team8.orders_service.services;

import com.ds3.team8.orders_service.exceptions.NotFoundException;
import com.ds3.team8.orders_service.client.ProductClient;
import com.ds3.team8.orders_service.dtos.OrderItemResponse;
import com.ds3.team8.orders_service.entities.OrderItem;
import com.ds3.team8.orders_service.mappers.OrderItemMapper;
import com.ds3.team8.orders_service.repositories.IOrderItemRepository;
import com.ds3.team8.orders_service.utils.ProductUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemServiceImpl implements IOrderItemService {

    private final IOrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final ProductClient productClient;

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    public OrderItemServiceImpl(IOrderItemRepository orderItemRepository, OrderItemMapper orderItemMapper, ProductClient productClient) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
        this.productClient = productClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemResponse> findAll() {
        List<OrderItem> orderItems = orderItemRepository.findAllByIsActiveTrue();
        if (orderItems.isEmpty()) {
            logger.warn("No se encontraron ítems de orden activos");
            throw new NotFoundException("No se encontraron ítems de orden activos");
        }
        logger.info("Se encontraron {} ítems de orden activos", orderItems.size());
        return orderItemMapper.toOrderItemResponseList(orderItems);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemResponse> findAllPageable(Pageable pageable) {
        Page<OrderItem> orderItems = orderItemRepository.findAllByIsActiveTrue(pageable);
        if (orderItems.isEmpty()) {
            logger.warn("No se encontraron ítems de orden activos");
            throw new NotFoundException("No se encontraron ítems de orden activos");
        }
        logger.info("Se encontraron {} ítems de orden activos", orderItems.getTotalElements());
        return orderItems.map(orderItemMapper::toOrderItemResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemResponse findById(Long id) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findByIdAndIsActiveTrue(id);
        if (orderItemOptional.isEmpty()) {
            logger.error("Ítem de orden con ID {} no encontrado", id);
            throw new NotFoundException("Item de orden no encontrado");
        }
        logger.info("Ítem de orden con ID {} encontrado", id);
        return orderItemMapper.toOrderItemResponse(orderItemOptional.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemResponse> findAllByOrderId(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderIdAndIsActiveTrue(orderId);
        if (orderItems.isEmpty()) {
            logger.warn("No se encontraron ítems de orden activos para la orden con ID {}", orderId);
            throw new NotFoundException("No se encontraron ítems de orden activos para la orden especificada");
        }
        logger.info("Se encontraron {} ítems de orden activos para la orden con ID {}", orderItems.size(), orderId);
        return orderItemMapper.toOrderItemResponseList(orderItems);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemResponse> findAllByOrderIdPageable(Long orderId, Pageable pageable) {
        Page<OrderItem> orderItems = orderItemRepository.findAllByOrderIdAndIsActiveTrue(orderId, pageable);
        if (orderItems.isEmpty()) {
            logger.warn("No se encontraron ítems de orden activos para la orden con ID {}", orderId);
            throw new NotFoundException("No se encontraron ítems de orden activos para la orden especificada");
        }
        logger.info("Se encontraron {} ítems de orden activos para la orden con ID {}", orderItems.getTotalElements(), orderId);
        return orderItems.map(orderItemMapper::toOrderItemResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean orderItemHasProducts(Long productId) {
        // Validar que el producto existe
        ProductUtil.validateProduct(productClient, productId);
        // Verificar si existe un ítem de orden activo con el producto especificado
        logger.info("Verificando si existe un ítem de orden activo con el producto ID {}", productId);
        return orderItemRepository.existsByProductIdAndOrderIsActiveTrue(productId);
    }


}
