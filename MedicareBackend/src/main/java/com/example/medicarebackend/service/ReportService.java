package com.example.medicarebackend.service;

import com.example.medicarebackend.dto.BillSummary;
import com.example.medicarebackend.dto.TopMedicine;
import com.example.medicarebackend.model.Medicine;
import com.example.medicarebackend.model.Order;
import com.example.medicarebackend.model.User;
import com.example.medicarebackend.repository.MedicineRepository;
import com.example.medicarebackend.repository.OrderRepository;
import com.example.medicarebackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * All the numbers shown on the admin dashboard and in the two reports
 * are calculated here. Every value is read live from the MySQL database.
 */
@Service
public class ReportService {

    private final MedicineRepository medicineRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public ReportService(MedicineRepository medicineRepository,
                         OrderRepository orderRepository,
                         UserRepository userRepository) {
        this.medicineRepository = medicineRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    /* ============================================================
       1. DASHBOARD  -  the cards on the admin home page
       ============================================================ */
    public Map<String, Object> dashboard() {

        List<Medicine> medicines = medicineRepository.findAll();
        List<Order> lines = orderRepository.findAll();
        List<User> users = userRepository.findAll();

        LocalDate today = LocalDate.now();

        int totalStock = 0;
        int inStock = 0;
        int lowStock = 0;
        int outOfStock = 0;
        int expired = 0;
        int expiringSoon = 0;
        int missingCost = 0;
        double stockValue = 0;
        double stockSellValue = 0;

        for (Medicine medicine : medicines) {

            totalStock += value(medicine.getQuantity());
            stockValue += medicine.getStockValue();
            stockSellValue += medicine.getStockSellValue();

            if (medicine.isPurchasePriceMissing()) {
                missingCost++;
            }

            switch (medicine.getStockStatus()) {
                case "OUT OF STOCK" -> outOfStock++;
                case "LOW STOCK" -> lowStock++;
                default -> inStock++;
            }

            switch (medicine.getExpiryStatus()) {
                case "EXPIRED" -> expired++;
                case "EXPIRING SOON" -> expiringSoon++;
                default -> { }
            }
        }

        int itemsSold = 0;
        double totalSales = 0;
        double todaySales = 0;
        double totalDiscount = 0;
        double totalProfit = 0;
        double todayProfit = 0;
        int linesWithoutCost = 0;

        // a "bill" is a group of lines that share one bill number
        List<String> allBillNumbers = new ArrayList<>();
        List<String> todayBillNumbers = new ArrayList<>();

        for (Order line : lines) {

            itemsSold += value(line.getQuantity());
            totalSales += value(line.getTotal());
            totalDiscount += value(line.getDiscount());

            if (line.isCostKnown()) {
                totalProfit += line.getProfit();
            } else {
                linesWithoutCost++;
            }

            if (!allBillNumbers.contains(line.getBillNumber())) {
                allBillNumbers.add(line.getBillNumber());
            }

            if (line.getOrderDate() != null
                    && line.getOrderDate().toLocalDate().isEqual(today)) {

                todaySales += value(line.getTotal());

                if (line.isCostKnown()) {
                    todayProfit += line.getProfit();
                }

                if (!todayBillNumbers.contains(line.getBillNumber())) {
                    todayBillNumbers.add(line.getBillNumber());
                }
            }
        }

        int activeUsers = 0;

        for (User user : users) {
            if (!"BLOCKED".equalsIgnoreCase(user.getStatus())) {
                activeUsers++;
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();

        result.put("totalMedicines", medicines.size());
        result.put("totalStock", totalStock);
        result.put("inStock", inStock);
        result.put("lowStock", lowStock);
        result.put("outOfStock", outOfStock);
        result.put("expired", expired);
        result.put("expiringSoon", expiringSoon);
        result.put("missingCost", missingCost);
        result.put("stockValue", round(stockValue));
        result.put("stockSellValue", round(stockSellValue));

        result.put("totalBills", allBillNumbers.size());
        result.put("itemsSold", itemsSold);
        result.put("totalSales", round(totalSales));
        result.put("totalDiscount", round(totalDiscount));
        result.put("totalProfit", round(totalProfit));
        result.put("linesWithoutCost", linesWithoutCost);
        result.put("todayBills", todayBillNumbers.size());
        result.put("todaySales", round(todaySales));
        result.put("todayProfit", round(todayProfit));

        result.put("totalUsers", users.size());
        result.put("activeUsers", activeUsers);
        result.put("blockedUsers", users.size() - activeUsers);

        return result;
    }

    /* ============================================================
       2. STOCK REPORT  -  every medicine + a summary line
       ============================================================ */
    public Map<String, Object> stockReport() {

        List<Medicine> medicines = medicineRepository.findAllByOrderByNameAsc();

        int totalStock = 0;
        int lowStock = 0;
        int outOfStock = 0;
        int expired = 0;
        int expiringSoon = 0;
        int missingCost = 0;
        int discounted = 0;
        double stockValue = 0;
        double stockSellValue = 0;

        for (Medicine medicine : medicines) {

            totalStock += value(medicine.getQuantity());
            stockValue += medicine.getStockValue();
            stockSellValue += medicine.getStockSellValue();

            if (medicine.isPurchasePriceMissing()) {
                missingCost++;
            }

            if (value(medicine.getDiscountPercent()) > 0) {
                discounted++;
            }

            if ("LOW STOCK".equals(medicine.getStockStatus())) lowStock++;
            if ("OUT OF STOCK".equals(medicine.getStockStatus())) outOfStock++;
            if ("EXPIRED".equals(medicine.getExpiryStatus())) expired++;
            if ("EXPIRING SOON".equals(medicine.getExpiryStatus())) expiringSoon++;
        }

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalMedicines", medicines.size());
        summary.put("totalStock", totalStock);
        summary.put("lowStock", lowStock);
        summary.put("outOfStock", outOfStock);
        summary.put("expired", expired);
        summary.put("expiringSoon", expiringSoon);
        summary.put("missingCost", missingCost);
        summary.put("discounted", discounted);
        summary.put("stockValue", round(stockValue));
        summary.put("stockSellValue", round(stockSellValue));
        summary.put("expectedProfit", round(stockSellValue - stockValue));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("summary", summary);
        result.put("medicines", medicines);

        return result;
    }

    /* ============================================================
       3. SALES REPORT  -  bills between two dates + top medicines
       ============================================================ */
    public Map<String, Object> salesReport(LocalDate from, LocalDate to) {

        List<Order> lines;

        if (from != null && to != null) {

            // include the whole "to" day, so 00:00 of the next day is the limit
            LocalDateTime start = from.atStartOfDay();
            LocalDateTime end = to.plusDays(1).atStartOfDay();

            lines = orderRepository.findByOrderDateBetweenOrderByOrderDateDesc(start, end);

        } else {
            lines = orderRepository.findAllByOrderByOrderDateDesc();
        }

        double subtotal = 0;
        double discountTotal = 0;
        double gst = 0;
        double total = 0;
        double cost = 0;
        double profit = 0;
        int itemsSold = 0;
        int linesWithoutCost = 0;

        // medicine name -> running total, so we can show the best sellers
        Map<String, TopMedicine> perMedicine = new LinkedHashMap<>();

        // date (yyyy-MM-dd) -> sales on that day, for the day-wise table
        Map<String, Double> perDay = new LinkedHashMap<>();

        for (Order line : lines) {

            subtotal += value(line.getSubtotal());
            discountTotal += value(line.getDiscount());
            gst += value(line.getGst());
            total += value(line.getTotal());
            cost += line.getCostAmount();
            itemsSold += value(line.getQuantity());

            if (line.isCostKnown()) {
                profit += line.getProfit();
            } else {
                linesWithoutCost++;
            }

            String medicineName = line.getMedicineName();

            perMedicine
                    .computeIfAbsent(medicineName, TopMedicine::new)
                    .add(value(line.getQuantity()),
                         value(line.getTotal()),
                         value(line.getDiscount()),
                         line.isCostKnown() ? line.getProfit() : 0,
                         line.isCostKnown());

            if (line.getOrderDate() != null) {
                String day = line.getOrderDate().toLocalDate().toString();
                perDay.merge(day, value(line.getTotal()), Double::sum);
            }
        }

        // biggest revenue first, keep the top 10
        List<TopMedicine> topMedicines = new ArrayList<>(perMedicine.values());
        topMedicines.sort((a, b) -> Double.compare(b.getRevenue(), a.getRevenue()));

        if (topMedicines.size() > 10) {
            topMedicines = topMedicines.subList(0, 10);
        }

        List<BillSummary> bills = groupBills(lines);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("billCount", bills.size());
        summary.put("itemsSold", itemsSold);
        summary.put("subtotal", round(subtotal));
        summary.put("discount", round(discountTotal));
        summary.put("taxable", round(subtotal - discountTotal));
        summary.put("gst", round(gst));
        summary.put("totalSales", round(total));
        summary.put("cost", round(cost));
        summary.put("profit", round(profit));
        summary.put("linesWithoutCost", linesWithoutCost);
        summary.put("averageBill", bills.isEmpty() ? 0 : round(total / bills.size()));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("summary", summary);
        result.put("bills", bills);
        result.put("topMedicines", topMedicines);
        result.put("dayWise", perDay);

        return result;
    }

    /* ================= helpers ================= */

    private List<BillSummary> groupBills(List<Order> lines) {

        Map<String, List<Order>> grouped = new LinkedHashMap<>();

        for (Order line : lines) {
            grouped.computeIfAbsent(line.getBillNumber(), key -> new ArrayList<>())
                    .add(line);
        }

        List<BillSummary> bills = new ArrayList<>();

        for (Map.Entry<String, List<Order>> entry : grouped.entrySet()) {
            bills.add(new BillSummary(entry.getKey(), entry.getValue()));
        }

        return bills;
    }

    private int value(Integer n) {
        return n == null ? 0 : n;
    }

    private double value(Double n) {
        return n == null ? 0 : n;
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
