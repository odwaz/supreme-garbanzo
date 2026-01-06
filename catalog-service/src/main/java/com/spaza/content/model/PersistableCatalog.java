package com.spaza.content.model;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class PersistableCatalog {
    @NotBlank
    private String code;
    @NotBlank
    private String name;
    private Boolean visible;
}
