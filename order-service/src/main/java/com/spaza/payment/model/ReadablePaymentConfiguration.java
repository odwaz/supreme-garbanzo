package com.spaza.payment.model;

import lombok.Data;
import java.util.Map;

@Data
public class ReadablePaymentConfiguration {
    private String code;
    private String name;
    private Boolean active;
    private Map<String, String> properties;
}
