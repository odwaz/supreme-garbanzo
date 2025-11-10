package com.spaza.customer.model;

import java.util.List;

public class ReadableCustomerList {
    private List<ReadableCustomer> customers;
    private int totalPages;
    private long totalElements;
    
    public ReadableCustomerList() {}
    
    public ReadableCustomerList(List<ReadableCustomer> customers, int totalPages, long totalElements) {
        this.customers = customers;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
    
    public List<ReadableCustomer> getCustomers() { return customers; }
    public void setCustomers(List<ReadableCustomer> customers) { this.customers = customers; }
    
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
}