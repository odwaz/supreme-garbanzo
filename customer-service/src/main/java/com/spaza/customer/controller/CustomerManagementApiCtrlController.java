package com.spaza.customer.controller;

import com.spaza.customer.api.CustomerManagementApiCtrl;
import com.spaza.customer.delegate.CustomerManagementApiCtrlDelegate;
import com.spaza.customer.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerManagementApiCtrlController implements CustomerManagementApiCtrl {

    @Autowired
    private CustomerManagementApiCtrlDelegate delegate;

    @Override
    public ResponseEntity<ReadableCustomer> getAuthUser() {
        return delegate.getAuthUser();
    }

    @Override
    public ResponseEntity<Customer> update(Customer customer) {
        return delegate.update(customer);
    }

    @Override
    public ResponseEntity<Void> delete() {
        return delegate.delete();
    }

    @Override
    public ResponseEntity<Void> updateAuthUserAddress(Customer customer) {
        return delegate.updateAuthUserAddress(customer);
    }

    @Override
    public ResponseEntity<ReadableCustomer> create(Customer customer) {
        return delegate.create(customer);
    }

    @Override
    public ResponseEntity<ReadableCustomer> get(Long id) {
        return delegate.get(id);
    }

    @Override
    public ResponseEntity<Customer> update(Long id, Customer customer) {
        return delegate.update(id, customer);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        return delegate.delete(id);
    }

    @Override
    public ResponseEntity<Void> updateAddress(Long id, Customer customer) {
        return delegate.updateAddress(id, customer);
    }

    @Override
    public ResponseEntity<ReadableCustomerList> list(Integer count, Integer page) {
        return delegate.list(count, page);
    }
}
