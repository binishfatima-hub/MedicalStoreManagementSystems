package com.example.medicarebackend.service;

import com.example.medicarebackend.dto.ApiResponse;
import com.example.medicarebackend.dto.BillSummary;
import com.example.medicarebackend.dto.OrderItemRequest;
import com.example.medicarebackend.dto.PlaceOrderRequest;
import com.example.medicarebackend.model.Medicine;
import com.example.medicarebackend.model.Order;
import com.example.medicarebackend.repository.MedicineRepository;
import com.example.medicarebackend.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Placing an order, reducing the stock and reading the bills back. */
@Service
public class OrderService {

    /** GST charged on medicines, 5%. Change this one line to change the rate. */
    public static final double GST_PERCENT = 5.0;

    private final OrderRepository orderRepository;
    private final MedicineRepository medicineRepository;

    public OrderService(OrderRepository orderRepository,
                        MedicineRepository medicineRepository) {
        this.orderRepository = orderRepository;
        this.medicineRepository = medicineRepository;
    }

    /**
     * Places one order (= one bill).
     *
     * @Transactional means: either every line is saved and every stock is
     * reduced, or nothing at all happens. Half a bill can never be saved.
     */
    @Transactional
    public ApiResponse placeOrder(PlaceOrderRequest request) {

        /* ---------- 1. basic checks ---------- */

        if (request.getItems() == null || request.getItems().isEmpty()) {
            return ApiResponse.fail("Your cart is empty");
        }

        if (isBlank(request.getCustomerName())) {
            return ApiResponse.fail("Customer name is required");
        }

        if (request.getMobile() == null || !request.getMobile().matches("[0-9]{10}")) {
            return ApiResponse.fail("Mobile number must be exactly 10 digits");
        }

        /* ---------- 2. check the stock BEFORE saving anything ---------- */

        Map<Integer, Medicine> medicines = new LinkedHashMap<>();
        Map<Integer, Integer> wanted = new LinkedHashMap<>();

        for (OrderItemRequest item : request.getItems()) {

            if (item.getMedicineId() == null) {
                return ApiResponse.fail("Invalid item in cart");
            }

            int quantity = item.getQuantity() == null ? 0 : item.getQuantity();

            if (quantity <= 0) {
                return ApiResponse.fail("Quantity must be at least 1");
            }

            Optional<Medicine> found =
                    medicineRepository.findById(item.getMedicineId());

            if (found.isEmpty()) {
                return ApiResponse.fail("A medicine in your cart no longer exists");
            }

            Medicine medicine = found.get();

            // the same medicine may appear twice in the cart, so add it up
            int alreadyWanted = wanted.getOrDefault(medicine.getId(), 0);
            int totalWanted = alreadyWanted + quantity;

            int available = medicine.getQuantity() == null ? 0 : medicine.getQuantity();

            if (totalWanted > available) {
                return ApiResponse.fail(
                        "Not enough stock for " + medicine.getName()
                        + " (available: " + available + ")");
            }

            medicines.put(medicine.getId(), medicine);
            wanted.put(medicine.getId(), totalWanted);
        }

        /* ---------- 3. save the bill ---------- */

        String billNumber = generateBillNumber();
        LocalDateTime now = LocalDateTime.now();

        List<Order> savedLines = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : wanted.entrySet()) {

            Medicine medicine = medicines.get(entry.getKey());
            int quantity = entry.getValue();

            /* Every price comes from the DATABASE, never from the browser,
               so nobody can change a price or a discount from the page. */

            double price = value(medicine.getPrice());
            double purchasePrice = value(medicine.getPurchasePrice());
            double discountPercent = value(medicine.getDiscountPercent());

            double subtotal = round(price * quantity);                     // before discount
            double discount = round(subtotal * discountPercent / 100.0);
            double taxable = round(subtotal - discount);                   // goods amount
            double gst = round(taxable * GST_PERCENT / 100.0);             // GST after discount
            double total = round(taxable + gst);

            Order line = new Order();
            line.setBillNumber(billNumber);
            line.setUserId(request.getUserId());
            line.setUserEmail(request.getUserEmail());
            line.setCustomerName(request.getCustomerName().trim());
            line.setMobile(request.getMobile().trim());
            line.setMedicineId(medicine.getId());
            line.setMedicineName(medicine.getName());
            line.setPurchasePrice(purchasePrice);
            line.setQuantity(quantity);
            line.setPrice(price);
            line.setDiscountPercent(discountPercent);
            line.setSubtotal(subtotal);
            line.setDiscount(discount);
            line.setGst(gst);
            line.setTotal(total);
            line.setOrderDate(now);

            savedLines.add(orderRepository.save(line));

            /* ---------- 4. reduce the stock ---------- */

            medicine.setQuantity(medicine.getQuantity() - quantity);
            medicineRepository.save(medicine);
        }

        return ApiResponse.ok("Order placed successfully",
                new BillSummary(billNumber, savedLines));
    }

    /** Every order line in the system, newest first (admin sales report). */
    public List<Order> findAllLines() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    /** All bills in the system, newest first. */
    public List<BillSummary> findAllBills() {
        return groupIntoBills(orderRepository.findAllByOrderByOrderDateDesc());
    }

    /** Only the bills of one logged-in user (My Orders). */
    public List<BillSummary> findBillsOfUser(Integer userId) {
        return groupIntoBills(orderRepository.findByUserIdOrderByOrderDateDesc(userId));
    }

    /** One full bill, used by the printable bill page. */
    public Optional<BillSummary> findBill(String billNumber) {

        List<Order> lines = orderRepository.findByBillNumber(billNumber);

        if (lines.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new BillSummary(billNumber, lines));
    }

    /** Admin: delete a whole bill (all of its lines). */
    @Transactional
    public ApiResponse deleteBill(String billNumber) {

        List<Order> lines = orderRepository.findByBillNumber(billNumber);

        if (lines.isEmpty()) {
            return ApiResponse.fail("Bill not found");
        }

        orderRepository.deleteAll(lines);

        return ApiResponse.ok("Bill " + billNumber + " deleted");
    }

    /* ================= helpers ================= */

    /** Turns a flat list of order lines into a list of bills. */
    private List<BillSummary> groupIntoBills(List<Order> lines) {

        // LinkedHashMap keeps the newest-first order we got from the database
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

    /** Example: BILL-20260906-143210 */
    private String generateBillNumber() {

        String stamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

        return "BILL-" + stamp;
    }

    /** Keeps money values at 2 decimal places. */
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    /** Treats a missing (null) money value as 0. */
    private double value(Double number) {
        return number == null ? 0 : number;
    }

    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
