package com.spaza.order.service;

import com.spaza.order.entity.Order;
import com.spaza.order.exception.*;
import com.spaza.payment.model.PersistablePayment;
import com.spaza.payment.service.PaymentService;
import com.spaza.order.model.*;
import com.spaza.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    private static final String ORDER_NOT_FOUND = "Order not found: ";
    private final String customerServiceUrl;
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final PaymentService paymentService;

    public OrderService(@Value("${customer.service.url}") String customerServiceUrl,
                       OrderRepository orderRepository,
                       RestTemplate restTemplate,
                       PaymentService paymentService) {
        this.customerServiceUrl = customerServiceUrl;
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.paymentService = paymentService;
    }
    
    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }
    
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
    
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
    
    public List<Order> findByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
    
    public List<Order> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
    
    public List<Order> findByMerchantId(Long merchantId) {
        return orderRepository.findByMerchantId(merchantId);
    }
    
    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
    
    public Long getCustomerIdByEmail(String email) {
        try {
            String url = customerServiceUrl + "/api/v1/private/customer/email/" + email;
            Customer customer = restTemplate.getForObject(url, Customer.class);
            return customer != null ? customer.getId() : null;
        } catch (Exception e) {
            log.error("Failed to fetch customer by email: {}", email, e);
            return null;
        }
    }
    
    @Transactional
    public ReadableOrderConfirmation createOrder(String cartCode, PersistableOrder persistableOrder, Long authenticatedCustomerId) {
        log.info("Creating order for customer: {}, cart: {}", authenticatedCustomerId, cartCode);
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        Long merchantId = null;
        
        try {
            String cartUrl = customerServiceUrl + "/api/v1/cart/" + cartCode;
            log.debug("Fetching cart: {}", cartUrl);
            Map<String, Object> cartResponse = restTemplate.getForObject(cartUrl, Map.class);
            
            if (cartResponse != null) {
                if (cartResponse.get("total") != null) {
                    total = new java.math.BigDecimal(cartResponse.get("total").toString());
                }
                if (cartResponse.get("merchantId") != null) {
                    merchantId = Long.valueOf(cartResponse.get("merchantId").toString());
                } else {
                    throw new ValidationException("Cart has no merchant ID");
                }
            } else {
                throw new ServiceException("Cart not found: " + cartCode);
            }
        } catch (ValidationException e) {
            throw e;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to create order", e);
            throw new ServiceException("Failed to create order: " + e.getMessage());
        }
        
        Order order = new Order();
        order.setCustomerId(authenticatedCustomerId);
        order.setMerchantId(merchantId);
        order.setStatus("PENDING");
        order.setTotal(total);
        order.setTax(persistableOrder.getTax());
        order.setShipping(persistableOrder.getShipping());
        order.setPaymentMethod(persistableOrder.getPaymentMethod());
        order.setShippingAddress(persistableOrder.getShippingAddress());
        order.setBillingAddress(persistableOrder.getBillingAddress());
        order.setShippingCity(persistableOrder.getShippingCity());
        order.setShippingPostalCode(persistableOrder.getShippingPostalCode());
        order.setShippingCountry(persistableOrder.getShippingCountry());
        order.setBillingCity(persistableOrder.getBillingCity());
        order.setBillingPostalCode(persistableOrder.getBillingPostalCode());
        order.setBillingCountry(persistableOrder.getBillingCountry());
        order.setPhone(persistableOrder.getPhone());
        order.setEmail(persistableOrder.getEmail());
        order.setCurrencyCode(persistableOrder.getCurrencyCode());
        order.setDateCreated(java.time.LocalDateTime.now());
        
        if (total.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Invalid order total");
        }
        
        Order saved = orderRepository.save(order);
        log.info("Order created: {}, total: {}, merchant: {}", saved.getId(), saved.getTotal(), merchantId);

        String method = persistableOrder.getPaymentMethod();
        if (method == null || method.isBlank()) {
            method = "STRIPE";
        }
        PersistablePayment payment = new PersistablePayment();
        payment.setAmount(saved.getTotal() != null ? saved.getTotal().doubleValue() : 0.0);
        payment.setPaymentMethod(method);
        try {
            paymentService.init(String.valueOf(saved.getId()), payment);
            log.info("Payment transaction initialized: {} for order: {}", method, saved.getId());
        } catch (Exception e) {
            log.error("Failed to init transaction for order: {}", saved.getId(), e);
        }
        
        return new ReadableOrderConfirmation(saved.getId(), "ORD-" + saved.getId());
    }
    
    @Transactional
    public ReadableOrderConfirmation createAnonymousOrder(String cartCode, PersistableAnonymousOrder order) {
        log.info("Creating anonymous order for cart: {}", cartCode);
        Order newOrder = new Order();
        newOrder.setStatus("PENDING");
        newOrder.setTotal(java.math.BigDecimal.ZERO);
        Order saved = orderRepository.save(newOrder);

        PersistablePayment payment = new PersistablePayment();
        payment.setAmount(0.0);
        payment.setPaymentMethod("STRIPE");
        try {
            paymentService.init(String.valueOf(saved.getId()), payment);
            log.info("Payment transaction initialized for anonymous order: {}", saved.getId());
        } catch (Exception e) {
            log.error("Failed to init transaction for anonymous order: {}", saved.getId(), e);
        }

        return new ReadableOrderConfirmation(saved.getId(), "ORD-" + saved.getId());
    }
    
    public ReadableOrderList getOrders() {
        List<Order> orders = orderRepository.findAll();
        List<ReadableOrder> readableOrders = orders.stream().map(this::toReadable).collect(Collectors.toList());
        return new ReadableOrderList(readableOrders, readableOrders.size());
    }
    
    public ReadableOrder getOrder(Long id) {
        return orderRepository.findById(id)
            .map(this::toReadable)
            .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND + id));
    }
    
    public ReadableOrderList searchOrders(String status, Long merchantId) {
        List<Order> orders;
        
        if (merchantId != null) {
            orders = orderRepository.findByMerchantId(merchantId);
            if (status != null) {
                orders = orders.stream().filter(o -> status.equals(o.getStatus())).collect(Collectors.toList());
            }
        } else {
            orders = status != null ? orderRepository.findByStatus(status) : orderRepository.findAll();
        }
        
        List<ReadableOrder> readableOrders = orders.stream().map(this::toReadable).collect(Collectors.toList());
        return new ReadableOrderList(readableOrders, readableOrders.size());
    }
    
    public ReadableOrderList getCustomerOrders(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        List<ReadableOrder> readableOrders = orders.stream().map(this::toReadable).collect(Collectors.toList());
        return new ReadableOrderList(readableOrders, readableOrders.size());
    }
    
    @Transactional
    public void updateCustomer(Long orderId, Customer customer) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND + orderId));
        order.setCustomerId(customer.getId());
        orderRepository.save(order);
        log.info("Updated customer for order: {}", orderId);
    }
    
    @Transactional
    public void updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND + orderId));
        order.setStatus(status);
        orderRepository.save(order);
        log.info("Updated order {} status to: {}", orderId, status);
    }
    
    private ReadableOrder toReadable(Order order) {
        ReadableOrder readable = new ReadableOrder();
        readable.setId(order.getId());
        readable.setCustomerId(order.getCustomerId());
        readable.setStatus(order.getStatus());
        readable.setTotal(order.getTotal());
        return readable;
    }
}
