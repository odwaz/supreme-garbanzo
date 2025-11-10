package com.spaza.cart.delegate;

import com.spaza.cart.model.*;
import com.spaza.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartApiCtrlDelegateImpl implements ShoppingCartApiCtrlDelegate {

    @Autowired
    private CartService cartService;

    @Override
    public ResponseEntity<ReadableShoppingCart> addToCart(PersistableShoppingCartItem shoppingCartItem) {
        ReadableShoppingCart cart = cartService.addToCart(shoppingCartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> getByCode(String code) {
        ReadableShoppingCart cart = cartService.getByCode(code);
        return ResponseEntity.ok(cart);
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> modifyCart(String code, PersistableShoppingCartItem shoppingCartItem) {
        ReadableShoppingCart cart = cartService.modifyCart(code, shoppingCartItem);
        return ResponseEntity.ok(cart);
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> modifyCart(String code, PersistableShoppingCartItem[] shoppingCartItems) {
        ReadableShoppingCart cart = cartService.modifyCart(code, shoppingCartItems);
        return ResponseEntity.ok(cart);
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> deleteCartItem(String code, String sku, boolean body) {
        ReadableShoppingCart cart = cartService.deleteCartItem(code, sku, body);
        return body ? ResponseEntity.ok(cart) : ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> modifyCart(String code, String promo) {
        ReadableShoppingCart cart = cartService.addPromo(code, promo);
        return ResponseEntity.ok(cart);
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> getByCustomer(String cart) {
        ReadableShoppingCart shoppingCart = cartService.getByCustomer(cart);
        return ResponseEntity.ok(shoppingCart);
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> getByCustomer(Long id, String cart) {
        ReadableShoppingCart shoppingCart = cartService.getByCustomer(id, cart);
        return ResponseEntity.ok(shoppingCart);
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> addToCart(Long id, PersistableShoppingCartItem shoppingCartItem) {
        ReadableShoppingCart cart = cartService.addToCart(id, shoppingCartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }
}