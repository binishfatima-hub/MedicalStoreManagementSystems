package com.example.medicarebackend.controller;

import com.example.medicarebackend.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

/**
 * Reports for the admin panel.
 *
 *   GET /api/reports/dashboard              the cards on the admin home page
 *   GET /api/reports/stock                  stock report
 *   GET /api/reports/sales?from=&to=        sales report (dates optional)
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return reportService.dashboard();
    }

    @GetMapping("/stock")
    public Map<String, Object> stock() {
        return reportService.stockReport();
    }

    @GetMapping("/sales")
    public Map<String, Object> sales(

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return reportService.salesReport(from, to);
    }
}
