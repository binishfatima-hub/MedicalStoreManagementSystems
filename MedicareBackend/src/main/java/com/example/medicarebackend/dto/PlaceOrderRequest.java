package com.example.medicarebackend.dto;

import java.util.List;

/**
 * What the checkout page sends when the user confirms the order.
 *
 * Note: only the medicine id and the quantity are sent. The price is
 * always taken from the database on the server side, so nobody can
 * change the price from the browser.
 */
public class PlaceOrderRequest {

    private Integer userId;
    private String userEmail;
    private String customerName;
    private String mobile;
    private List<OrderItemRequest> items;

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
}
