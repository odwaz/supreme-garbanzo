package com.spaza.shipping.model;

import lombok.Data;
import java.util.Map;

@Data
public class ReadableShippingConfiguration {
    private String code;
    private String name;
    private Boolean active;
    private Map<String, String> properties;
}
