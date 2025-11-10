package com.spaza.order.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ReadableTaxRate {
    private Long id;
    private String country;
    private String stateProvince;
    private BigDecimal rate;
    private Long taxClassId;
}
