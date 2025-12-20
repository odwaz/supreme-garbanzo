package com.spaza.cart.service;

import com.spaza.cart.model.*;
import com.spaza.cart.repository.CartRepository;
import com.spaza.cart.repository.CartItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Value("${catalog.service.url:http://localhost:8081}")
    private String catalogServiceUrl;
    
    private RestTemplate restTemplate = new RestTemplate();

    public ReadableShoppingCart addToCart(PersistableShoppingCartItem item) {
        Long merchantId = getProductMerchantId(item.getProduct());
        String cartCode = "CART-" + UUID.randomUUID().toString().substring(0, 8);
        ReadableShoppingCart cart = new ReadableShoppingCart();
        cart.setCode(cartCode);
        cart.setMerchantId(merchantId);
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProductId(item.getProduct());
        cartItem.setQuantity(item.getQuantity());
        cart.getItems().add(cartItem);
        cart.setTotal(calculateTotal(cart));
        return cartRepository.save(cart);
    }

    public ReadableShoppingCart getByCode(String code) {
        return cartRepository.findByCode(code).orElse(createEmptyCart(code));
    }

    public ReadableShoppingCart modifyCart(String code, PersistableShoppingCartItem item) throws IllegalStateException {
        ReadableShoppingCart cart = getByCode(code);
        Long productMerchantId = getProductMerchantId(item.getProduct());
        if (cart.getMerchantId() != null && !cart.getMerchantId().equals(productMerchantId)) {
            throw new IllegalStateException("Cannot add products from different merchants to the same cart");
        }
        if (cart.getMerchantId() == null) {
            cart.setMerchantId(productMerchantId);
        }
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProductId(item.getProduct());
        cartItem.setQuantity(item.getQuantity());
        cart.getItems().add(cartItem);
        cart.setTotal(calculateTotal(cart));
        return cartRepository.save(cart);
    }

    public ReadableShoppingCart modifyCart(String code, PersistableShoppingCartItem[] items) {
        ReadableShoppingCart cart = getByCode(code);
        for (PersistableShoppingCartItem item : items) {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProductId(item.getProduct());
            cartItem.setQuantity(item.getQuantity());
            cart.getItems().add(cartItem);
        }
        return cartRepository.save(cart);
    }

    public ReadableShoppingCart deleteCartItem(String code, String sku, boolean returnBody) {
        ReadableShoppingCart cart = getByCode(code);
        cart.getItems().removeIf(item -> item.getProductId().toString().equals(sku));
        cart.setTotal(calculateTotal(cart));
        cartRepository.save(cart);
        return returnBody ? cart : null;
    }
    
    private java.math.BigDecimal calculateTotal(ReadableShoppingCart cart) {
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            try {
                String productUrl = catalogServiceUrl + "/api/v1/product/" + item.getProductId();
                Map<String, Object> product = restTemplate.getForObject(productUrl, Map.class);
                if (product != null && product.get("price") != null) {
                    java.math.BigDecimal price = new java.math.BigDecimal(product.get("price").toString());
                    total = total.add(price.multiply(java.math.BigDecimal.valueOf(item.getQuantity())));
                }
            } catch (Exception e) {
                log.error("Error fetching product price: {}", e.getMessage());
            }
        }
        return total;
    }

    public ReadableShoppingCart addPromo(String code) {
        return getByCode(code);
    }

    public ReadableShoppingCart getByCustomer(String cartCode) {
        return getByCode(cartCode);
    }

    public ReadableShoppingCart getByCustomer(Long customerId) {
        return getByCode("CART-" + customerId);
    }

    public ReadableShoppingCart addToCart(PersistableShoppingCartItem item) {
        return addToCart(item);
    }

    private ReadableShoppingCart createEmptyCart(String code) {
        ReadableShoppingCart cart = new ReadableShoppingCart();
        cart.setCode(code);
        cart.setItems(new ArrayList<>());
        return cart;
    }
    
    private Long getProductMerchantId(Long productId) {
        try {
            String productUrl = catalogServiceUrl + "/api/v1/product/" + productId;
            Map<String, Object> product = restTemplate.getForObject(productUrl, Map.class);
            if (product != null && product.get("merchantId") != null) {
                return Long.valueOf(product.get("merchantId").toString());
            }
        } catch (Exception e) {
            log.error("Error fetching product merchant: {}", e.getMessage());
        }
        return null;
    }
}