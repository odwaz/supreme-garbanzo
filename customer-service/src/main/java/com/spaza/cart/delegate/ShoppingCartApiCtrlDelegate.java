package com.spaza.cart.delegate;

import com.spaza.cart.model.*;
import org.springframework.http.ResponseEntity;

public interface ShoppingCartApiCtrlDelegate {
    ResponseEntity<ReadableShoppingCart> addToCart(PersistableShoppingCartItem shoppingCartItem);
    ResponseEntity<ReadableShoppingCart> getByCode(String code);
    ResponseEntity<ReadableShoppingCart> modifyCart(String code, PersistableShoppingCartItem shoppingCartItem);
    ResponseEntity<ReadableShoppingCart> modifyCart(String code, PersistableShoppingCartItem[] shoppingCartItems);
    ResponseEntity<ReadableShoppingCart> deleteCartItem(String code, String sku, boolean body);
    ResponseEntity<ReadableShoppingCart> modifyCart(String code, String promo);
    ResponseEntity<ReadableShoppingCart> getByCustomer(String cart);
    ResponseEntity<ReadableShoppingCart> getByCustomer(Long id, String cart);
    ResponseEntity<ReadableShoppingCart> addToCart(Long id, PersistableShoppingCartItem shoppingCartItem);
}