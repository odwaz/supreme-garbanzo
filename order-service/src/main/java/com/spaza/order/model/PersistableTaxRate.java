package com.spaza.order.model;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PersistableTaxRate {
    @NotBlank
    private String country;
    private String stateProvince;
    @NotNull
    private BigDecimal rate;
    @NotNull
    private Long taxClassId;
}
