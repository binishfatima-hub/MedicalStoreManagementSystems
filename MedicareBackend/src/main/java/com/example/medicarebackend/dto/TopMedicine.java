package com.example.medicarebackend.dto;

/** One line of the "Top Selling Medicines" table in the sales report. */
public class TopMedicine {

    private String medicineName;
    private int quantitySold;
    private double revenue;
    private double discount;
    private double profit;
    private boolean costKnown = true;

    public TopMedicine(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineName() { return medicineName; }
    public int getQuantitySold() { return quantitySold; }
    public double getRevenue() { return round(revenue); }
    public double getDiscount() { return round(discount); }
    public double getProfit() { return round(profit); }
    public boolean isCostKnown() { return costKnown; }

    public void add(int quantity, double amount, double discount,
                    double profit, boolean costKnown) {
        this.quantitySold += quantity;
        this.revenue += amount;
        this.discount += discount;
        this.profit += profit;

        if (!costKnown) {
            this.costKnown = false;
        }
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
