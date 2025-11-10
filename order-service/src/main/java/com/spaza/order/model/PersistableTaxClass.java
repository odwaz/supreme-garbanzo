package com.spaza.order.model;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class PersistableTaxClass {
    @NotBlank
    private String code;
    @NotBlank
    private String title;
}
