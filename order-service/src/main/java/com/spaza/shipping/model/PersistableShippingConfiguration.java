package com.spaza.shipping.model;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class PersistableShippingConfiguration {
    @NotBlank
    private String code;
    @NotBlank
    private String name;
    private Boolean active;
    private Map<String, String> properties;
}
