package com.spaza.cart.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shopping_carts")
public class ReadableShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    
    @ElementCollection
    private List<String> items = new ArrayList<>();
    
    public ReadableShoppingCart() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }
}
