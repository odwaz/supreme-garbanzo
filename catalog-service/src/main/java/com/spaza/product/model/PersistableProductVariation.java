package com.spaza.product.model;

import java.util.List;

public class PersistableProductVariation {
    private Long id;
    private String code;
    private String name;
    private List<ProductOptionDescription> descriptions;
    private List<String> optionValues;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductOptionDescription> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<ProductOptionDescription> descriptions) {
        this.descriptions = descriptions;
    }

    public List<String> getOptionValues() {
        return optionValues;
    }

    public void setOptionValues(List<String> optionValues) {
        this.optionValues = optionValues;
    }
}
