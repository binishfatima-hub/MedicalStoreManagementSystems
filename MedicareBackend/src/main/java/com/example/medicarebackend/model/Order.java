package com.example.medicarebackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * One row of the "orders" table = ONE medicine line inside a bill.
 *
 * If a customer buys 3 different medicines in one bill, 3 rows are
 * saved and all 3 share the same bill_number.
 *
 * The prices are COPIED here at the moment of the sale. If the admin
 * changes a medicine's price tomorrow, an old bill still shows the
 * price that was actually charged.
 *
 * How one line is calculated:
 *
 *   subtotal = price x quantity            (MRP, before discount)
 *   discount = subtotal x discountPercent / 100
 *   taxable  = subtotal - discount         (what the customer pays for goods)
 *   gst      = taxable x 5%
 *   total    = taxable + gst
 *   profit   = taxable - (purchasePrice x quantity)
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "bill_number")
    private String billNumber;

    /** Which registered user placed the order (null for a counter sale). */
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "customer_name")
    private String customerName;

    private String mobile;

    @Column(name = "medicine_id")
    private Integer medicineId;

    @Column(name = "medicine_name")
    private String medicineName;

    /** Cost price at the time of sale - used for the profit report. */
    @Column(name = "purchase_price")
    private Double purchasePrice;

    private Integer quantity;

    /** Selling price (MRP) of one unit at the time of sale. */
    private Double price;

    /** Discount percent that was applied to this line. */
    @Column(name = "discount_percent")
    private Double discountPercent;

    /** price x quantity, BEFORE the discount. */
    private Double subtotal;

    /** Discount amount in rupees for this line. */
    private Double discount;

    /** GST amount for this line (charged after the discount). */
    private Double gst;

    /** subtotal - discount + gst. */
    private Double total;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    public Order() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public Integer getMedicineId() { return medicineId; }
    public void setMedicineId(Integer medicineId) { this.medicineId = medicineId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public Double getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(Double purchasePrice) { this.purchasePrice = purchasePrice; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }

    public Double getGst() { return gst; }
    public void setGst(Double gst) { this.gst = gst; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    /* ================= calculated, not stored ================= */

    /** What the customer paid for the goods of this line (no GST). */
    public double getTaxableAmount() {
        return round(value(subtotal) - value(discount));
    }

    /** What the store paid for the goods of this line. */
    public double getCostAmount() {
        return round(value(purchasePrice) * value(quantity));
    }

    /** Profit earned on this line. Only meaningful when the cost is known. */
    public double getProfit() {
        return round(getTaxableAmount() - getCostAmount());
    }

    /**
     * False when this line was saved without a purchase price (an old bill,
     * or a medicine whose cost the admin never filled in).
     *
     * Without it the profit would wrongly look like the whole selling
     * price, so the reports show "-" instead of a made-up number.
     */
    public boolean isCostKnown() {
        return purchasePrice != null && purchasePrice > 0;
    }

    private double value(Double n) { return n == null ? 0 : n; }
    private int value(Integer n) { return n == null ? 0 : n; }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
