package com.ds3.team8.orders_service.repositories;

import com.ds3.team8.orders_service.entities.Order;
import com.ds3.team8.orders_service.entities.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IOrderItemRepositoryTest {
    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderItemRepository orderItemRepository;

    private Order order;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();  // Limpia la base de datos antes de cada prueba
        orderItemRepository.deleteAll();

        // Crear y guardar una orden antes de cada prueba
        order = orderRepository.save(new Order(null, 1L, LocalDateTime.parse("2025-05-10T08:16:00"), "En camino", true, new ArrayList<>()));
    }

    @Test
    void testSaveOrder() {
        // Crear y guardar un orden
        OrderItem orderItem = orderItemRepository.save(new OrderItem(null, order, 1L, 8L, true));
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        // Verificar que se guardó correctamente
        assertNotNull(savedOrderItem.getId());
        assertEquals(order, savedOrderItem.getOrder());
        assertTrue(savedOrderItem.getIsActive());
    }

    @Test
    void testFindById() {
        // Guardar un item de una orden
        OrderItem orderItem = orderItemRepository.save(new OrderItem(null, order, 2L, 11L, true));

        // Buscar por ID
        Optional<OrderItem> foundOrderItem = orderItemRepository.findById(orderItem.getId());

        // Verificar que el orden se encontró
        assertTrue(foundOrderItem.isPresent());
        assertEquals(order, foundOrderItem.get().getOrder());
    }

    @Test
    void testFindAll() {
        // Guardar varias ordenes
        orderItemRepository.save(new OrderItem(null, order, 1L, 8L, true));
        orderItemRepository.save(new OrderItem(null, order, 2L, 11L, true));

        // Obtener todos los ordens
        List<OrderItem> orderItems = orderItemRepository.findAll();

        // Verificar que se guardaron 2 ordens
        assertEquals(2, orderItems.size());
    }
}