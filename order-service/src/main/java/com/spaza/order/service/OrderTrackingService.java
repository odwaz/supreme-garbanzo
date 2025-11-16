package com.spaza.order.service;

import com.spaza.order.model.*;
import com.spaza.order.entity.OrderHistory;
import com.spaza.order.repository.OrderHistoryRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderTrackingService {

    private final OrderHistoryRepository repository;

    public OrderTrackingService(OrderHistoryRepository repository) {
        this.repository = repository;
    }

    public ReadableOrderTracking getTracking(Long id) {
        ReadableOrderTracking tracking = new ReadableOrderTracking();
        tracking.setOrderId(id);
        tracking.setTrackingNumber("TRK123456");
        tracking.setCarrier("DHL");
        tracking.setStatus("In Transit");
        tracking.setEstimatedDelivery("2024-01-15");
        return tracking;
    }

    public ReadableOrderHistory[] getHistory(Long id) {
        return repository.findByOrderId(id).stream()
            .map(this::toReadable)
            .toArray(ReadableOrderHistory[]::new);
    }

    private ReadableOrderHistory toReadable(OrderHistory entity) {
        ReadableOrderHistory readable = new ReadableOrderHistory();
        readable.setId(entity.getId());
        readable.setStatus(entity.getStatus());
        readable.setComments(entity.getComments());
        readable.setDateAdded(entity.getDateAdded());
        return readable;
    }
}
