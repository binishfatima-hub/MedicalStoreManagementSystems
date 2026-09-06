package com.example.medicarebackend.controller;

import com.example.medicarebackend.dto.ApiResponse;
import com.example.medicarebackend.model.Medicine;
import com.example.medicarebackend.service.MedicineService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Medicines.
 *
 *   GET    /api/medicines                 list (user panel + admin panel)
 *   GET    /api/medicines/search?keyword= search by name or company
 *   GET    /api/medicines/{id}            one medicine
 *   POST   /api/medicines                 ADD      (admin)
 *   PUT    /api/medicines/{id}            EDIT     (admin)
 *   DELETE /api/medicines/{id}            DELETE   (admin)
 *   PUT    /api/medicines/{id}/stock      STOCK UPDATE (admin)
 */
@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping
    public List<Medicine> getAll() {
        return medicineService.findAll();
    }

    @GetMapping("/search")
    public List<Medicine> search(@RequestParam(required = false) String keyword) {
        return medicineService.search(keyword);
    }

    @GetMapping("/{id}")
    public ApiResponse getOne(@PathVariable Integer id) {

        return medicineService.findById(id)
                .map(medicine -> ApiResponse.ok("Found", medicine))
                .orElse(ApiResponse.fail("Medicine not found"));
    }

    @PostMapping
    public ApiResponse add(@RequestBody Medicine medicine) {
        return medicineService.add(medicine);
    }

    @PutMapping("/{id}")
    public ApiResponse update(@PathVariable Integer id, @RequestBody Medicine medicine) {
        return medicineService.update(id, medicine);
    }

    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable Integer id) {
        return medicineService.delete(id);
    }

    /** action = ADD | REMOVE | SET */
    @PutMapping("/{id}/stock")
    public ApiResponse updateStock(@PathVariable Integer id,
                                   @RequestParam String action,
                                   @RequestParam Integer amount) {
        return medicineService.updateStock(id, action, amount);
    }
}
