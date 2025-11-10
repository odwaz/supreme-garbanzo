package com.spaza.cart.api;

import com.spaza.cart.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface ShoppingCartApiCtrl {

    @PostMapping("/api/v1/cart")
    ResponseEntity<ReadableShoppingCart> addToCart(@Valid @RequestBody PersistableShoppingCartItem shoppingCartItem);

    @GetMapping("/api/v1/cart/{code}")
    ResponseEntity<ReadableShoppingCart> getByCode(@PathVariable String code);

    @PutMapping("/api/v1/cart/{code}")
    ResponseEntity<ReadableShoppingCart> modifyCart(@PathVariable String code, @Valid @RequestBody PersistableShoppingCartItem shoppingCartItem);

    @PostMapping("/api/v1/cart/{code}/multi")
    ResponseEntity<ReadableShoppingCart> modifyCart(@PathVariable String code, @Valid @RequestBody PersistableShoppingCartItem[] shoppingCartItems);

    @DeleteMapping("/api/v1/cart/{code}/product/{sku}")
    ResponseEntity<ReadableShoppingCart> deleteCartItem(@PathVariable String code, @PathVariable String sku, @RequestParam(defaultValue = "false") boolean body);

    @PostMapping("/api/v1/cart/{code}/promo/{promo}")
    ResponseEntity<ReadableShoppingCart> modifyCart(@PathVariable String code, @PathVariable String promo);

    @GetMapping("/api/v1/auth/customer/cart")
    ResponseEntity<ReadableShoppingCart> getByCustomer(@RequestParam(required = false) String cart);

    @GetMapping("/api/v1/auth/customer/{id}/cart")
    ResponseEntity<ReadableShoppingCart> getByCustomer(@PathVariable Long id, @RequestParam(required = false) String cart);

    @PostMapping("/api/v1/customers/{id}/cart")
    ResponseEntity<ReadableShoppingCart> addToCart(@PathVariable Long id, @Valid @RequestBody PersistableShoppingCartItem shoppingCartItem);
}