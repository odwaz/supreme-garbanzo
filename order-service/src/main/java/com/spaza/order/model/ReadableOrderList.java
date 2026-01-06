package com.spaza.order.model;

import java.util.List;

public class ReadableOrderList {
    private List<ReadableOrder> orders;
    private int total;

    public ReadableOrderList(List<ReadableOrder> orders, int total) {
        this.orders = orders;
        this.total = total;
    }

    public List<ReadableOrder> getOrders() { return orders; }
    public void setOrders(List<ReadableOrder> orders) { this.orders = orders; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
}
