package com.spaza.cart.controller;

import com.spaza.cart.api.ShoppingCartApiCtrl;
import com.spaza.cart.delegate.ShoppingCartApiCtrlDelegate;
import com.spaza.cart.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class ShoppingCartApiCtrlController implements ShoppingCartApiCtrl {

    @Autowired
    private ShoppingCartApiCtrlDelegate delegate;

    @Override
    public ResponseEntity<ReadableShoppingCart> addToCart(@Valid @RequestBody PersistableShoppingCartItem shoppingCartItem) {
        return delegate.addToCart(shoppingCartItem);
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> getByCode(@PathVariable String code) {
        return delegate.getByCode(code);
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> modifyCart(@PathVariable String code, @Valid @RequestBody PersistableShoppingCartItem shoppingCartItem) {
        return delegate.modifyCart(code, shoppingCartItem);
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> modifyCart(@PathVariable String code, @Valid @RequestBody PersistableShoppingCartItem[] shoppingCartItems) {
        return delegate.modifyCart(code, shoppingCartItems);
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> deleteCartItem(@PathVariable String code, @PathVariable String sku, @RequestParam(defaultValue = "false") boolean body) {
        return delegate.deleteCartItem(code, sku, body);
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> modifyCart(@PathVariable String code, @PathVariable String promo) {
        return delegate.modifyCart(code, promo);
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> getByCustomer(@RequestParam(required = false) String cart) {
        return delegate.getByCustomer(cart);
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> getByCustomer(@PathVariable Long id, @RequestParam(required = false) String cart) {
        return delegate.getByCustomer(id, cart);
    }

    @Override
    public ResponseEntity<ReadableShoppingCart> addToCart(@PathVariable Long id, @Valid @RequestBody PersistableShoppingCartItem shoppingCartItem) {
        return delegate.addToCart(id, shoppingCartItem);
    }
}