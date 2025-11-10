package com.spaza.order.service;

import com.spaza.order.model.*;
import com.spaza.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private PersistableOrder persistableOrder;
    private PersistableAnonymousOrder anonymousOrder;
    private ReadableOrder readableOrder;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("test@example.com");

        persistableOrder = new PersistableOrder();
        persistableOrder.setCustomerId(1L);
        persistableOrder.setTotal(100.0);

        anonymousOrder = new PersistableAnonymousOrder();
        anonymousOrder.setEmail("anonymous@example.com");
        anonymousOrder.setTotal(100.0);

        readableOrder = new ReadableOrder();
        readableOrder.setId(1L);
        readableOrder.setStatus("PENDING");
        readableOrder.setTotal(100.0);
        readableOrder.setCustomerId(1L);
    }

    @Test
    void checkout_WithCustomer_ShouldCreateOrderAndReturnConfirmation() {
        when(orderRepository.save(any(ReadableOrder.class))).thenAnswer(i -> {
            ReadableOrder order = i.getArgument(0);
            order.setId(1L);
            return order;
        });
        
        ReadableOrderConfirmation result = orderService.checkout("CART-123", persistableOrder);

        assertNotNull(result);
        assertNotNull(result.getOrderId());
        assertTrue(result.getConfirmationNumber().startsWith("ORD-"));
        verify(orderRepository).save(any(ReadableOrder.class));
    }

    @Test
    void checkout_Anonymous_ShouldCreateOrderAndReturnConfirmation() {
        when(orderRepository.save(any(ReadableOrder.class))).thenAnswer(i -> {
            ReadableOrder order = i.getArgument(0);
            order.setId(1L);
            return order;
        });
        
        ReadableOrderConfirmation result = orderService.checkout("CART-123", anonymousOrder);

        assertNotNull(result);
        assertNotNull(result.getOrderId());
        assertTrue(result.getConfirmationNumber().startsWith("ORD-"));
        verify(orderRepository).save(any(ReadableOrder.class));
    }

    @Test
    void list_ShouldReturnOrderList() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(readableOrder));
        
        ReadableOrderList result = orderService.list(10, 0);

        assertNotNull(result);
        assertEquals(1, result.getOrders().size());
        assertEquals(1, result.getTotal());
        verify(orderRepository).findAll();
    }

    @Test
    void getOrder_ShouldReturnOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(readableOrder));
        
        ReadableOrder result = orderService.getOrder(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("PENDING", result.getStatus());
        verify(orderRepository).findById(1L);
    }

    @Test
    void get_ShouldReturnOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(readableOrder));
        
        ReadableOrder result = orderService.get(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderRepository).findById(1L);
    }

    @Test
    void updateOrderCustomer_ShouldUpdateCustomerEmail() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(readableOrder));
        when(orderRepository.save(any(ReadableOrder.class))).thenReturn(readableOrder);
        
        Customer newCustomer = new Customer();
        newCustomer.setId(2L);
        newCustomer.setEmail("updated@example.com");

        orderService.updateOrderCustomer(1L, newCustomer);
        
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(ReadableOrder.class));
    }

    @Test
    void updateOrderStatus_ShouldUpdateStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(readableOrder));
        when(orderRepository.save(any(ReadableOrder.class))).thenReturn(readableOrder);
        
        orderService.updateOrderStatus(1L, "SHIPPED");
        
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(ReadableOrder.class));
    }

    @Test
    void listByCustomer_ShouldReturnCustomerOrders() {
        when(orderRepository.findByCustomerId(1L)).thenReturn(Arrays.asList(readableOrder));
        
        ReadableOrderList result = orderService.listByCustomer(10, 1L, 0);

        assertNotNull(result);
        assertEquals(1, result.getOrders().size());
        verify(orderRepository).findByCustomerId(1L);
    }

    @Test
    void list_WithFilters_ShouldReturnFilteredOrders() {
        when(orderRepository.findByStatus("PENDING")).thenReturn(Arrays.asList(readableOrder));
        
        ReadableOrderList result = orderService.list(10, "test@example.com", 1L, "John", 0, "123456", "PENDING");

        assertNotNull(result);
        assertEquals(1, result.getOrders().size());
        verify(orderRepository).findByStatus("PENDING");
    }
}