package com.example.medicarebackend.dto;

import com.example.medicarebackend.model.Order;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A whole bill (all its medicine lines added together).
 * Used by "My Orders" in the user panel and by the sales report.
 */
public class BillSummary {

    private String billNumber;
    private String customerName;
    private String mobile;
    private String userEmail;
    private LocalDateTime orderDate;

    private int itemCount;
    private int totalQuantity;
    private double subtotal;      // before discount
    private double discount;      // total discount given
    private double taxable;       // subtotal - discount
    private double gst;
    private double total;
    private double cost;          // what the store paid for these goods
    private double profit;        // taxable - cost
    private boolean costKnown = true;   // false if any line has no cost

    private List<Order> items;

    public BillSummary(String billNumber, List<Order> items) {

        this.billNumber = billNumber;
        this.items = items;

        if (!items.isEmpty()) {
            Order first = items.get(0);
            this.customerName = first.getCustomerName();
            this.mobile = first.getMobile();
            this.userEmail = first.getUserEmail();
            this.orderDate = first.getOrderDate();
        }

        this.itemCount = items.size();

        for (Order line : items) {
            this.totalQuantity += value(line.getQuantity());
            this.subtotal += value(line.getSubtotal());
            this.discount += value(line.getDiscount());
            this.gst += value(line.getGst());
            this.total += value(line.getTotal());
            this.cost += line.getCostAmount();

            if (!line.isCostKnown()) {
                this.costKnown = false;
            }
        }

        this.taxable = round(this.subtotal - this.discount);
        this.profit = round(this.taxable - this.cost);

        this.subtotal = round(this.subtotal);
        this.discount = round(this.discount);
        this.gst = round(this.gst);
        this.total = round(this.total);
        this.cost = round(this.cost);
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private int value(Integer n) { return n == null ? 0 : n; }
    private double value(Double n) { return n == null ? 0 : n; }

    public String getBillNumber() { return billNumber; }
    public String getCustomerName() { return customerName; }
    public String getMobile() { return mobile; }
    public String getUserEmail() { return userEmail; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public int getItemCount() { return itemCount; }
    public int getTotalQuantity() { return totalQuantity; }
    public double getSubtotal() { return subtotal; }
    public double getDiscount() { return discount; }
    public double getTaxable() { return taxable; }
    public double getGst() { return gst; }
    public double getTotal() { return total; }
    public double getCost() { return cost; }
    public double getProfit() { return profit; }
    public boolean isCostKnown() { return costKnown; }
    public List<Order> getItems() { return items; }
}
