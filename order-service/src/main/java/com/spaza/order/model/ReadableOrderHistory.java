package com.spaza.order.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReadableOrderHistory {
    private Long id;
    private String status;
    private String comments;
    private LocalDateTime dateAdded;
}
