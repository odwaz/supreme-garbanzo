package com.spaza.cart.model;

import javax.validation.constraints.NotNull;

public class PersistableShoppingCartItem {
    @NotNull
    private Long product;
    
    @NotNull
    private Integer quantity;
    
    public PersistableShoppingCartItem() {}
    
    public Long getProduct() { return product; }
    public void setProduct(Long product) { this.product = product; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
