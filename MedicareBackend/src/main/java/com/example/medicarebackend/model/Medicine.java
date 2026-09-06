package com.example.medicarebackend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * One row of the "medicine" table = one medicine in the store's stock.
 * Only the admin panel can add / edit / delete these.
 *
 * THREE money fields, and it is important not to mix them up:
 *
 *   purchasePrice   what the STORE paid the supplier   (cost)
 *   price           the MRP shown to the customer      (selling price)
 *   discountPercent how much off the MRP the customer gets
 *
 *   finalPrice = price - discount        <-- what the customer actually pays
 *   profit     = finalPrice - purchasePrice
 *
 * The rule enforced everywhere: finalPrice can never go below purchasePrice,
 * otherwise the store would sell at a loss.
 */
@Entity
@Table(name = "medicine")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String company;

    /** Cost price - what the store paid for one unit. Never shown to users. */
    @Column(name = "purchase_price")
    private Double purchasePrice;

    /** Selling price (MRP) of one unit, in rupees. */
    private Double price;

    /** Discount given to the customer, in percent (0 = no discount). */
    @Column(name = "discount_percent")
    private Double discountPercent;

    /** How many units are left in stock. */
    private Integer quantity;

    /** Sent to / received from the website as "2027-12-31". */
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    public Medicine() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public Double getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(Double purchasePrice) { this.purchasePrice = purchasePrice; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    /* ================================================================
       Calculated values. They are not columns in the database - they
       are worked out every time and sent along in the JSON, so the
       website never has to repeat the same formula.
       ================================================================ */

    /** Discount amount on ONE unit, in rupees. */
    public double getDiscountAmount() {

        return round(price() * percent() / 100.0);
    }

    /** What the customer actually pays for ONE unit, after discount. */
    public double getFinalPrice() {

        return round(price() - getDiscountAmount());
    }

    /** Profit on ONE unit = final price - purchase price. */
    public double getProfitPerUnit() {

        return round(getFinalPrice() - purchase());
    }

    /** Profit as a percent of the purchase price (0 when cost is not set). */
    public double getProfitPercent() {

        if (purchase() <= 0) {
            return 0;
        }

        return round(getProfitPerUnit() / purchase() * 100.0);
    }

    /** Value of this medicine's whole stock at COST = purchase price x quantity. */
    public double getStockValue() {

        return round(purchase() * quantity());
    }

    /** Value of this medicine's whole stock at SELLING price after discount. */
    public double getStockSellValue() {

        return round(getFinalPrice() * quantity());
    }

    /** True when the admin has not filled the purchase price yet. */
    public boolean isPurchasePriceMissing() {
        return purchase() <= 0;
    }

    /** True when selling this medicine would lose money. */
    public boolean isLossMaking() {
        return purchase() > 0 && getFinalPrice() < purchase();
    }

    /** OUT OF STOCK / LOW STOCK / IN STOCK - shown as a coloured badge. */
    public String getStockStatus() {

        int q = quantity();

        if (q <= 0) {
            return "OUT OF STOCK";
        }

        if (q <= 10) {
            return "LOW STOCK";
        }

        return "IN STOCK";
    }

    /** EXPIRED / EXPIRING SOON (within 30 days) / OK. */
    public String getExpiryStatus() {

        if (expiryDate == null) {
            return "OK";
        }

        LocalDate today = LocalDate.now();

        if (expiryDate.isBefore(today)) {
            return "EXPIRED";
        }

        if (expiryDate.isBefore(today.plusDays(30))) {
            return "EXPIRING SOON";
        }

        return "OK";
    }

    /* ---------- small helpers so null never causes a crash ---------- */

    private double price()    { return price == null ? 0 : price; }
    private double purchase() { return purchasePrice == null ? 0 : purchasePrice; }
    private double percent()  { return discountPercent == null ? 0 : discountPercent; }
    private int quantity()    { return quantity == null ? 0 : quantity; }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
