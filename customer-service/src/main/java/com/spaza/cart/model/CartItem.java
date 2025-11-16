package com.spaza.cart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private ReadableShoppingCart cart;
    
    private Long productId;
    private Integer quantity;
    
    public CartItem() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public ReadableShoppingCart getCart() { return cart; }
    public void setCart(ReadableShoppingCart cart) { this.cart = cart; }
    
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
