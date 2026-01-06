package com.spaza.order.model;

import lombok.Data;

@Data
public class ReadableOrderTracking {
    private Long orderId;
    private String trackingNumber;
    private String carrier;
    private String status;
    private String estimatedDelivery;
}
