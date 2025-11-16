package com.spaza.customer.api;

import com.spaza.customer.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface CustomerManagementApiCtrl {

    @GetMapping("/api/v1/auth/customer/profile")
    ResponseEntity<ReadableCustomer> getAuthUser();

    @PatchMapping("/api/v1/auth/customer/")
    ResponseEntity<Customer> update(@Valid @RequestBody Customer customer);

    @DeleteMapping("/api/v1/auth/customer/")
    ResponseEntity<Void> delete();

    @PatchMapping("/api/v1/auth/customer/address")
    ResponseEntity<Void> updateAuthUserAddress(@Valid @RequestBody Customer customer);

    @PostMapping("/api/v1/private/customer")
    ResponseEntity<ReadableCustomer> create(@Valid @RequestBody Customer customer);

    @GetMapping("/api/v1/private/customer/{id}")
    ResponseEntity<ReadableCustomer> get(@PathVariable Long id);

    @PutMapping("/api/v1/private/customer/{id}")
    ResponseEntity<Customer> update(@PathVariable Long id, @Valid @RequestBody Customer customer);

    @DeleteMapping("/api/v1/private/customer/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);

    @PatchMapping("/api/v1/private/customer/{id}/address")
    ResponseEntity<Void> updateAddress(@PathVariable Long id, @Valid @RequestBody Customer customer);

    @GetMapping("/api/v1/private/customers")
    ResponseEntity<ReadableCustomerList> list(@RequestParam(required = false) Integer count, @RequestParam(required = false) Integer page);
    
    @GetMapping("/api/v1/private/customer/email/{email}")
    ResponseEntity<ReadableCustomer> getByEmail(@PathVariable String email);
}