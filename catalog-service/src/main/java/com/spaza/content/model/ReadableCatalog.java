package com.spaza.content.model;

import lombok.Data;

@Data
public class ReadableCatalog {
    private Long id;
    private String code;
    private String name;
    private Boolean visible;
}
