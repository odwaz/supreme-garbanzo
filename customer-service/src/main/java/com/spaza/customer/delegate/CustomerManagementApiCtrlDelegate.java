package com.spaza.customer.delegate;

import com.spaza.customer.model.*;
import org.springframework.http.ResponseEntity;

public interface CustomerManagementApiCtrlDelegate {
    ResponseEntity<ReadableCustomer> getAuthUser();
    ResponseEntity<Customer> update(Customer customer);
    ResponseEntity<Void> delete();
    ResponseEntity<Void> updateAuthUserAddress(Customer customer);
    ResponseEntity<ReadableCustomer> create(Customer customer);
    ResponseEntity<ReadableCustomer> get(Long id);
    ResponseEntity<Customer> update(Long id, Customer customer);
    ResponseEntity<Void> delete(Long id);
    ResponseEntity<Void> updateAddress(Long id, Customer customer);
    ResponseEntity<ReadableCustomerList> list(Integer count, Integer page);
    ResponseEntity<ReadableCustomer> getPrivateProfile();
}
