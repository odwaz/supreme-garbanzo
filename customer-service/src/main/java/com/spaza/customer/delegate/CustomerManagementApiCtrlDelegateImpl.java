package com.spaza.customer.delegate;

import com.spaza.customer.model.*;
import com.spaza.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerManagementApiCtrlDelegateImpl implements CustomerManagementApiCtrlDelegate {

    @Autowired
    private CustomerService customerService;

    @Override
    public ResponseEntity<ReadableCustomer> getAuthUser() {
        String email = getAuthenticatedEmail();
        return customerService.findByEmail(email)
                .map(this::toReadable)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Customer> update(Customer customer) {
        String email = getAuthenticatedEmail();
        return customerService.findByEmail(email)
                .map(existing -> {
                    customer.setId(existing.getId());
                    Customer updated = customerService.save(customer);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> delete() {
        String email = getAuthenticatedEmail();
        customerService.findByEmail(email).ifPresent(c -> customerService.deleteById(c.getId()));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateAuthUserAddress(Customer customer) {
        String email = getAuthenticatedEmail();
        return customerService.findByEmail(email)
                .map(existing -> {
                    existing.setBillingAddress(customer.getBillingAddress());
                    existing.setDeliveryAddress(customer.getDeliveryAddress());
                    customerService.save(existing);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ReadableCustomer> create(Customer customer) {
        Customer saved = customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(toReadable(saved));
    }

    @Override
    public ResponseEntity<ReadableCustomer> get(Long id) {
        return customerService.findById(id)
                .map(this::toReadable)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Customer> update(Long id, Customer customer) {
        customer.setId(id);
        Customer updated = customerService.save(customer);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateAddress(Long id, Customer customer) {
        return customerService.findById(id)
                .map(existing -> {
                    existing.setBillingAddress(customer.getBillingAddress());
                    existing.setDeliveryAddress(customer.getDeliveryAddress());
                    customerService.save(existing);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ReadableCustomerList> list(Integer count, Integer page) {
        List<Customer> customers = customerService.findAll(0, 0);
        ReadableCustomerList list = new ReadableCustomerList();
        list.setCustomers(customers.stream().map(this::toReadable).collect(Collectors.toList()));
        list.setTotal(customers.size());
        return ResponseEntity.ok(list);
    }
    
    @Override
    public ResponseEntity<ReadableCustomer> getPrivateProfile() {
        String email = getAuthenticatedEmail();
        return customerService.findByEmail(email)
                .map(this::toReadable)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private String getAuthenticatedEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }

    private ReadableCustomer toReadable(Customer customer) {
        ReadableCustomer readable = new ReadableCustomer();
        readable.setId(customer.getId());
        readable.setEmail(customer.getEmail());
        readable.setFirstName(customer.getFirstName());
        readable.setLastName(customer.getLastName());
        readable.setPhone(customer.getPhone());
        readable.setBillingAddress(customer.getBillingAddress());
        readable.setDeliveryAddress(customer.getDeliveryAddress());
        return readable;
    }
}
