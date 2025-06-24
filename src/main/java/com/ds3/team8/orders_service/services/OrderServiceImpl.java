package com.ds3.team8.orders_service.services;

import com.ds3.team8.orders_service.exceptions.BadRequestException;
import com.ds3.team8.orders_service.exceptions.NotFoundException;
import com.ds3.team8.orders_service.client.ProductClient;
import com.ds3.team8.orders_service.client.UserClient;
import com.ds3.team8.orders_service.client.dtos.ProductResponse;
import com.ds3.team8.orders_service.client.dtos.StockRequest;
import com.ds3.team8.orders_service.dtos.OrderItemRequest;
import com.ds3.team8.orders_service.dtos.OrderRequest;
import com.ds3.team8.orders_service.dtos.OrderResponse;
import com.ds3.team8.orders_service.dtos.OrderStatusRequest;
import com.ds3.team8.orders_service.entities.Order;
import com.ds3.team8.orders_service.entities.OrderItem;
import com.ds3.team8.orders_service.enums.OrderStatus;
import com.ds3.team8.orders_service.mappers.OrderMapper;
import com.ds3.team8.orders_service.repositories.IOrderRepository;
import com.ds3.team8.orders_service.utils.ProductUtil;

import feign.FeignException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductClient productClient;
    private final UserClient userClient;

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    public OrderServiceImpl(IOrderRepository orderRepository, OrderMapper orderMapper, ProductClient productClient, UserClient userClient) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productClient = productClient;
        this.userClient = userClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        List<Order> orders = orderRepository.findAllByIsActiveTrue();
        if (orders.isEmpty()) {
            logger.warn("No se encontraron pedidos activos");
            throw new NotFoundException("No se encontraron pedidos activos.");
        }
        logger.info("Número de pedidos encontrados: {}", orders.size());
        return orderMapper.toOrderResponseList(orders);
    }

    @Override
    @Transactional
    public OrderResponse save(OrderRequest orderRequest, Long userId) {
        // Validar que el usuario existe
        validateUser(userId);

        Order order = new Order();
        order.setCustomerId(userId);

        List<OrderItem> items = new ArrayList<>();

        // Crear los items del pedido y validar los productos
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<StockRequest> stockRequests = new ArrayList<>();

        for (OrderItemRequest itemReq : orderRequest.getItems()) {

            // Validar que el producto existe
            ProductResponse product = ProductUtil.validateProduct(productClient, itemReq.getProductId());

            OrderItem item = new OrderItem();
            item.setProductId(itemReq.getProductId());
            item.setQuantity(itemReq.getQuantity());
            item.setOrder(order);
            items.add(item);

            // Agregar la solicitud de stock para validar más adelante
            stockRequests.add(new StockRequest(itemReq.getProductId(), -itemReq.getQuantity()));

            // Calcular el total del pedido
            BigDecimal productPrice = product.getPrice();
            totalAmount = totalAmount.add(productPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity())));
        }

        // Validar el stock de los productos
        validateStock(stockRequests);
        // Guardar el pedido y sus items
        order.setItems(items);
        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        logger.info("Pedido guardado con ID: {}", savedOrder.getId());
        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Order> orderOptional = orderRepository.findByIdAndIsActiveTrue(id);
        if (orderOptional.isEmpty()) {
            logger.error("Pedido con ID {} no encontrado", id);
            throw new NotFoundException("Pedido no encontrado");
        }
        Order order = orderOptional.get();

        // Validar que el pedido está en estado PENDING_PAYMENT
        if (order.getOrderStatus() != OrderStatus.PENDING_PAYMENT) {
            logger.error("No se puede eliminar el pedido con ID {} porque no está en estado PENDING_PAYMENT", id);
            throw new BadRequestException("Solo se pueden eliminar pedidos pendientes de pago.");
        }

        order.setIsActive(false); // Marcar como inactivo en lugar de eliminar
        orderRepository.save(order);
        logger.info("Pedido con ID {} eliminado (marcado como inactivo)", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> findAllPageable(Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByIsActiveTrue(pageable);
        if (orders.isEmpty()) {
            logger.warn("No se encontraron pedidos activos");
            throw new NotFoundException("No se encontraron pedidos activos.");
        }
        logger.info("Número de pedidos encontrados: {}", orders.getTotalElements());
        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        Optional<Order> orderOptional = orderRepository.findByIdAndIsActiveTrue(id);
        if (orderOptional.isEmpty()) {
            logger.error("Pedido con ID {} no encontrado", id);
            throw new NotFoundException("Pedido no encontrado");
        }
        logger.info("Pedido encontrado: {}", orderOptional.get());
        return orderMapper.toOrderResponse(orderOptional.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAllByUserId(Long userId) {
        // Validar que el usuario existe
        validateUser(userId);
        
        List<Order> orders = orderRepository.findAllByCustomerIdAndIsActiveTrue(userId);
        if (orders.isEmpty()) {
            logger.warn("No se encontraron pedidos para el usuario con ID {}", userId);
            throw new NotFoundException("No se encontraron pedidos para el usuario especificado.");
        }
        logger.info("Número de pedidos encontrados para el usuario con ID {}: {}", userId, orders.size());
        return orderMapper.toOrderResponseList(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> findAllByUserIdPageable(Long userId, Pageable pageable) {
        // Validar que el usuario existe
        validateUser(userId);

        Page<Order> orders = orderRepository.findAllByCustomerIdAndIsActiveTrue(userId, pageable);
        if (orders.isEmpty()) {
            logger.warn("No se encontraron pedidos para el usuario con ID {}", userId);
            throw new NotFoundException("No se encontraron pedidos para el usuario especificado.");
        }
        logger.info("Número de pedidos encontrados para el usuario con ID {}: {}", userId, orders.getTotalElements());
        return orders.map(orderMapper::toOrderResponse);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatusRequest orderStatusRequest) {
        Optional<Order> orderOptional = orderRepository.findByIdAndIsActiveTrue(id);
        if (orderOptional.isEmpty()) {
            logger.error("Pedido con ID {} no encontrado", id);
            throw new NotFoundException("Pedido no encontrado");
        }
        Order order = orderOptional.get();
        switch (orderStatusRequest.getOrderStatus()) {
            case PAID:
                if (order.getOrderStatus() != OrderStatus.PENDING_PAYMENT) {
                    throw new BadRequestException("Solo se puede marcar como pagada una orden pendiente de pago.");
                }
                break;
            case PROCESSING:
                if (order.getOrderStatus() != OrderStatus.PAID) {
                    throw new BadRequestException("Solo se puede procesar una orden pagada.");
                }
                break;
            case COMPLETED:
                if (order.getOrderStatus() != OrderStatus.PROCESSING) {
                    throw new BadRequestException("Solo se puede completar una orden en procesamiento.");
                }
                break;
            case CANCELED:
                if (order.getOrderStatus() == OrderStatus.COMPLETED) {
                    throw new BadRequestException("No se puede cancelar una orden completada.");
                }
                break;
            default:
                throw new BadRequestException("Transición de estado no permitida.");
        }

        order.setOrderStatus(orderStatusRequest.getOrderStatus());
        Order updatedOrder = orderRepository.save(order);
        logger.info("Estado del pedido actualizado a: {}", orderStatusRequest.getOrderStatus());
        return orderMapper.toOrderResponse(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean userHasOrders(Long userId) {
        // Validar que el usuario existe
        validateUser(userId);
        // Verificar si el usuario tiene pedidos activos
        logger.info("Verificando si el usuario con ID {} tiene pedidos activos", userId);
        return orderRepository.existsByCustomerIdAndIsActiveTrue(userId);
    }

    private void validateUser(Long userId) {
        try {
            userClient.getUserById(userId);
            logger.info("Usuario con ID {} validado correctamente", userId);
        } catch (FeignException e) {
            logger.error("Error al validar el usuario: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo validar el usuario", e);
        }
    }

    private void validateStock(List<StockRequest> stockRequests) {
        try {
            if(productClient.validateStock(stockRequests)) {
                logger.info("Stock validado correctamente para los items del pedido");
            } else {
                logger.error("No hay suficiente stock para algunos items del pedido");
                throw new BadRequestException("No hay suficiente stock para algunos items del pedido");
            }
        } catch (FeignException e) {
            logger.error("Error al validar el stock de los items del pedido: {}", e.getMessage(), e);
            throw new RuntimeException("Error al validar el stock de los items del pedido");
        }
    }

}