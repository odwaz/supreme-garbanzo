package com.spaza.order.service;

import com.spaza.order.model.*;
import com.spaza.order.repository.OrderHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderTrackingServiceTest {

    @Mock
    private OrderHistoryRepository repository;

    @InjectMocks
    private OrderTrackingService service;

    private OrderHistory orderHistory;

    @BeforeEach
    void setUp() {
        orderHistory = new OrderHistory();
        orderHistory.setId(1L);
        orderHistory.setOrderId(100L);
        orderHistory.setStatus("Processing");
        orderHistory.setComments("Order received");
        orderHistory.setDateAdded(LocalDateTime.now());
    }

    @Test
    void getTracking_ShouldReturnTracking() {
        ReadableOrderTracking result = service.getTracking(100L);

        assertNotNull(result);
        assertEquals(100L, result.getOrderId());
        assertEquals("TRK123456", result.getTrackingNumber());
    }

    @Test
    void getHistory_ShouldReturnOrderHistory() {
        when(repository.findByOrderId(100L)).thenReturn(Arrays.asList(orderHistory));

        ReadableOrderHistory[] result = service.getHistory(100L);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("Processing", result[0].getStatus());
        assertEquals("Order received", result[0].getComments());
        verify(repository).findByOrderId(100L);
    }

    @Test
    void getHistory_ShouldReturnEmpty_WhenNoHistory() {
        when(repository.findByOrderId(100L)).thenReturn(Arrays.asList());

        ReadableOrderHistory[] result = service.getHistory(100L);

        assertNotNull(result);
        assertEquals(0, result.length);
        verify(repository).findByOrderId(100L);
    }
}
