package com.spaza.shipping.model;

import java.util.List;

public class ReadableShippingSummary {
    private String cartCode;
    private List<ShippingOption> shippingOptions;
    private AddressLocation deliveryAddress;
    
    public String getCartCode() { return cartCode; }
    public void setCartCode(String cartCode) { this.cartCode = cartCode; }
    public List<ShippingOption> getShippingOptions() { return shippingOptions; }
    public void setShippingOptions(List<ShippingOption> shippingOptions) { this.shippingOptions = shippingOptions; }
    public AddressLocation getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(AddressLocation deliveryAddress) { this.deliveryAddress = deliveryAddress; }
}
