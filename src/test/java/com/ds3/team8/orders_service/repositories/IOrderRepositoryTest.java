package com.ds3.team8.orders_service.repositories;

import com.ds3.team8.orders_service.entities.Order;
//import com.ds3.team8.orders_service.entities.OrderItem;
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
public class IOrderRepositoryTest {
    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderItemRepository orderItemRepository;

    private Order order;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();  // Limpia la base de datos antes de cada prueba
        orderItemRepository.deleteAll();

        // Crear y guardar un role antes de cada prueba
        //order = orderRepository.save(new Order(null, 1L, LocalDateTime.parse("2025-05-10T08:16:00"), "En camino", true, new ArrayList<>()));
    }

    @Test
    void testSaveOrder() {
        // Crear y guardar un orden
        //OrderItem orderItem = orderItemRepository.save(new OrderItem(null, order, 1L, 8L, true));
        order = orderRepository.save(new Order(null, 1L, LocalDateTime.parse("2025-05-10T08:16:00"), "En camino", true, new ArrayList<>()));
        Order savedOrder = orderRepository.save(order);

        // Verificar que se guardó correctamente
        assertNotNull(savedOrder.getId());
        assertEquals(LocalDateTime.parse("2025-05-10T08:16:00"), savedOrder.getOrderDate());
        assertTrue(savedOrder.getIsActive());
    }

    @Test
    void testFindById() {
        // Guardar un orden
        order = orderRepository.save(new Order(null, 2L, LocalDateTime.parse("2025-05-10T05:27:00"), "Entregado", true, new ArrayList<>()));

        // Buscar por ID
        Optional<Order> foundOrder = orderRepository.findById(order.getId());

        // Verificar que el orden se encontró
        assertTrue(foundOrder.isPresent());
        assertEquals(LocalDateTime.parse("2025-05-10T05:27:00"), foundOrder.get().getOrderDate());
    }

    @Test
    void testFindAll() {
        // Guardar varias ordenes
        order = orderRepository.save(new Order(null, 1L, LocalDateTime.parse("2025-05-10T08:16:00"), "En camino", true, new ArrayList<>()));
        order = orderRepository.save(new Order(null, 2L, LocalDateTime.parse("2025-05-10T05:27:00"), "Entregado", true, new ArrayList<>()));

        // Obtener todos los ordens
        List<Order> orders = orderRepository.findAll();

        // Verificar que se guardaron 2 ordens
        assertEquals(2, orders.size());
    }
}