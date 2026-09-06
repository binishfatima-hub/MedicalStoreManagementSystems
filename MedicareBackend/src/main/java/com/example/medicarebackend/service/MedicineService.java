package com.example.medicarebackend.service;

import com.example.medicarebackend.dto.ApiResponse;
import com.example.medicarebackend.model.Medicine;
import com.example.medicarebackend.repository.MedicineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/** Add / edit / delete medicines and update their stock. */
@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;

    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    public List<Medicine> findAll() {
        return medicineRepository.findAllByOrderByNameAsc();
    }

    public Optional<Medicine> findById(Integer id) {
        return medicineRepository.findById(id);
    }

    /** Search box - matches the medicine name or the company name. */
    public List<Medicine> search(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }

        String text = keyword.trim();

        return medicineRepository
                .findByNameContainingIgnoreCaseOrCompanyContainingIgnoreCase(text, text);
    }

    /** ADD - used by the "Add Medicine" form in the admin panel. */
    public ApiResponse add(Medicine medicine) {

        String error = validate(medicine);

        if (error != null) {
            return ApiResponse.fail(error);
        }

        medicine.setId(null);                 // let MySQL create the id
        medicine.setName(medicine.getName().trim());

        Medicine saved = medicineRepository.save(medicine);

        return ApiResponse.ok("Medicine added successfully", saved);
    }

    /** EDIT - used by the "Edit Medicine" form in the admin panel. */
    public ApiResponse update(Integer id, Medicine changes) {

        Optional<Medicine> found = medicineRepository.findById(id);

        if (found.isEmpty()) {
            return ApiResponse.fail("Medicine not found");
        }

        String error = validate(changes);

        if (error != null) {
            return ApiResponse.fail(error);
        }

        Medicine medicine = found.get();
        medicine.setName(changes.getName().trim());
        medicine.setCompany(changes.getCompany());
        medicine.setPurchasePrice(changes.getPurchasePrice());
        medicine.setPrice(changes.getPrice());
        medicine.setDiscountPercent(changes.getDiscountPercent());
        medicine.setQuantity(changes.getQuantity());
        medicine.setExpiryDate(changes.getExpiryDate());

        Medicine saved = medicineRepository.save(medicine);

        return ApiResponse.ok("Medicine updated successfully", saved);
    }

    /** DELETE. */
    public ApiResponse delete(Integer id) {

        if (!medicineRepository.existsById(id)) {
            return ApiResponse.fail("Medicine not found");
        }

        medicineRepository.deleteById(id);

        return ApiResponse.ok("Medicine deleted successfully");
    }

    /**
     * STOCK UPDATE - the admin panel's "Update Stock" button.
     *
     * action = "ADD"    -> new stock arrived, add to the quantity
     * action = "REMOVE" -> damaged / expired, subtract from the quantity
     * action = "SET"    -> after physical counting, replace the quantity
     */
    public ApiResponse updateStock(Integer id, String action, Integer amount) {

        Optional<Medicine> found = medicineRepository.findById(id);

        if (found.isEmpty()) {
            return ApiResponse.fail("Medicine not found");
        }

        if (amount == null || amount < 0) {
            return ApiResponse.fail("Quantity cannot be negative");
        }

        Medicine medicine = found.get();

        int current = medicine.getQuantity() == null ? 0 : medicine.getQuantity();
        int updated;

        if ("ADD".equalsIgnoreCase(action)) {
            updated = current + amount;

        } else if ("REMOVE".equalsIgnoreCase(action)) {
            updated = current - amount;

            if (updated < 0) {
                return ApiResponse.fail(
                        "Cannot remove " + amount + " units, only " + current + " in stock");
            }

        } else if ("SET".equalsIgnoreCase(action)) {
            updated = amount;

        } else {
            return ApiResponse.fail("Action must be ADD, REMOVE or SET");
        }

        medicine.setQuantity(updated);
        medicineRepository.save(medicine);

        return ApiResponse.ok(
                "Stock updated: " + current + " -> " + updated, medicine);
    }

    /**
     * Shared validation for add and edit.
     * Returns null when everything is fine, otherwise the error message
     * that the admin panel shows in red.
     *
     * The important rules are the last two: the store must never sell
     * below what it paid, not even after the discount.
     */
    private String validate(Medicine medicine) {

        if (medicine.getName() == null || medicine.getName().trim().isEmpty()) {
            return "Medicine name is required";
        }

        Double purchasePrice = medicine.getPurchasePrice();
        Double price = medicine.getPrice();
        Double discountPercent = medicine.getDiscountPercent();

        if (purchasePrice == null || purchasePrice < 0) {
            return "Purchase price must be 0 or more";
        }

        if (price == null || price < 0) {
            return "Selling price must be 0 or more";
        }

        if (discountPercent == null || discountPercent < 0 || discountPercent > 100) {
            return "Discount must be between 0 and 100 percent";
        }

        if (medicine.getQuantity() == null || medicine.getQuantity() < 0) {
            return "Quantity must be 0 or more";
        }

        /* ---- rule 1: selling price cannot be below the purchase price ---- */

        if (purchasePrice > 0 && price < purchasePrice) {

            return "Selling price (Rs. " + money(price) + ") cannot be less than "
                 + "the purchase price (Rs. " + money(purchasePrice) + "). "
                 + "The store would lose Rs. " + money(purchasePrice - price)
                 + " on every unit.";
        }

        /* ---- rule 2: the discount must not push it below cost either ---- */

        double discountAmount = price * discountPercent / 100.0;
        double finalPrice = round(price - discountAmount);

        if (purchasePrice > 0 && finalPrice < purchasePrice) {

            double maxDiscount = round((price - purchasePrice) / price * 100.0);

            return "A " + trim(discountPercent) + "% discount brings the price down to Rs. "
                 + money(finalPrice) + ", which is below the purchase price of Rs. "
                 + money(purchasePrice) + ". The highest discount you can give on this "
                 + "medicine is " + trim(maxDiscount) + "%.";
        }

        return null;                          // null means "no error"
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    /** 60.0 -> "60.00" */
    private String money(double value) {
        return String.format("%.2f", value);
    }

    /** 12.0 -> "12"   and   12.5 -> "12.5"  (no ugly trailing zeros) */
    private String trim(double value) {

        if (value == Math.floor(value)) {
            return String.valueOf((long) value);
        }

        return String.valueOf(round(value));
    }
}
