package com.spaza.shipping.model;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "shipping_quotes")
public class ReadableShippingQuote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Double cost;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }
}
