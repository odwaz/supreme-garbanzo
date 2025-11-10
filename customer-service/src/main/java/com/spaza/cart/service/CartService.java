package com.spaza.cart.service;

import com.spaza.cart.model.*;
import com.spaza.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public ReadableShoppingCart addToCart(PersistableShoppingCartItem item) {
        String cartCode = "CART-" + UUID.randomUUID().toString().substring(0, 8);
        ReadableShoppingCart cart = new ReadableShoppingCart();
        cart.setCode(cartCode);
        cart.setItems(Arrays.asList("Product-" + item.getProduct()));
        return cartRepository.save(cart);
    }

    public ReadableShoppingCart getByCode(String code) {
        return cartRepository.findByCode(code).orElse(createEmptyCart(code));
    }

    public ReadableShoppingCart modifyCart(String code, PersistableShoppingCartItem item) {
        ReadableShoppingCart cart = getByCode(code);
        cart.getItems().add("Product-" + item.getProduct());
        return cartRepository.save(cart);
    }

    public ReadableShoppingCart modifyCart(String code, PersistableShoppingCartItem[] items) {
        ReadableShoppingCart cart = getByCode(code);
        for (PersistableShoppingCartItem item : items) {
            cart.getItems().add("Product-" + item.getProduct());
        }
        return cartRepository.save(cart);
    }

    public ReadableShoppingCart deleteCartItem(String code, String sku, boolean returnBody) {
        ReadableShoppingCart cart = getByCode(code);
        cart.getItems().removeIf(item -> item.contains(sku));
        cartRepository.save(cart);
        return returnBody ? cart : null;
    }

    public ReadableShoppingCart addPromo(String code, String promo) {
        return getByCode(code);
    }

    public ReadableShoppingCart getByCustomer(String cartCode) {
        return getByCode(cartCode);
    }

    public ReadableShoppingCart getByCustomer(Long customerId, String cartCode) {
        return getByCode(cartCode);
    }

    public ReadableShoppingCart addToCart(Long customerId, PersistableShoppingCartItem item) {
        return addToCart(item);
    }

    private ReadableShoppingCart createEmptyCart(String code) {
        ReadableShoppingCart cart = new ReadableShoppingCart();
        cart.setCode(code);
        cart.setItems(new ArrayList<>());
        return cart;
    }
}